package demo.zk.seckill.service.impl;

import demo.zk.seckill.entity.ShopItem;
import demo.zk.seckill.entity.User;
import demo.zk.seckill.mapper.ShopMapper;
import demo.zk.seckill.redis.CacheMangerJedisSingleAdapter;
import demo.zk.seckill.service.SeckillService;
import demo.zk.seckill.util.ZookeeperLock;
import demo.zk.seckill.view.Result;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by free on 18-7-14.
 */
@Service
@Lazy(value = false)
public class SeckillServiceImpl implements SeckillService , InitializingBean {

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    protected CacheMangerJedisSingleAdapter cacheManger;

    /** rabbitma 异步消息 */
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * zookeeper 有客户端实现 分布式锁了 只需要copy  copy
     */
    public Object fastOrders(User user) {
        boolean isLoadLock = false;
        Jedis jedis = null;
        try {
            isLoadLock = ZookeeperLock.getLock();
            if (isLoadLock) {
                /**
                 * 获得锁的线程, 根据业务处理
                 * 处理场景:
                 * 1.通过分布式缓存 更新库存
                 */
                jedis = cacheManger.getJedis();
                String itemId = user.getItemId();
                /**获取库存*/
                Double num = jedis.zscore("shop", itemId);
                if (num.intValue() == 0) {
                    return new Result("200", "已经卖完了, 试试其他商品吧");
                }

                Map<String, Double> members = new LinkedHashMap<String, Double>();
                members.put(itemId, --num);
                /**更新库存*/
                jedis.zadd("shop", members);
                /**2.rabbitma异步操作 数据库, 数据量稍大 分库, 每一个数据库对应 一个路由规则*/
                String message = "id=" + itemId + ":" + "num=" + num.intValue();
                amqpTemplate.convertAndSend("mq.shop.exchange",
                        "mq.shop.send", message);

                /**3.异步处理其他业务 */

                return Result.ok();
            } else {
                /**
                 * 没有获得锁的线程, 根据业务处理
                 * 处理场景:
                 * 1.返回友好的提示信息
                 * 2.客户端做好友好提示页面
                 */
                return Result.already();
            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        } finally {
            /***释放锁*/
            if (isLoadLock) {
                ZookeeperLock.releaseLock();
            }
            if (jedis != null) {
                jedis.close();
            }

        }

    }

    @Override
    public List<ShopItem> listShopItem() {
        return shopMapper.selectShopItemList();
    }

    public void updateShopItem(ShopItem shopItem) {
        shopMapper.updateShopItem(shopItem);
    }

    /**
     * 初始化 把库存和商品的基本信息放入redis 缓存
     * 这一块代码只是简单的实现,没有人任何的业务逻辑
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {

        List<ShopItem> shopItemList = listShopItem();

        Map<String, Double> members = new LinkedHashMap<String, Double>();
        for (ShopItem item : shopItemList) {

            members.put(item.getId().toString(),
                    item.getNum().doubleValue());
        }

        cacheManger.zadd("shop", members);
    }

}
