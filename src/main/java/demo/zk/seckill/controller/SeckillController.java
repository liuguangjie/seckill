package demo.zk.seckill.controller;

import demo.zk.seckill.entity.User;
import demo.zk.seckill.view.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品秒杀 模板
 */
@RestController
@RequestMapping("/seckill")
public class SeckillController extends BaseController {

    /**
     * 解决核心问题思路:
     * 1.一个应用集群部署 分布式锁的问题 (还需要考虑锁的性能问题)
     * 2.库存数量 保证一致性 (尽量用读写一致的分布式缓存, 根据需求选择吧)
     * 3.选择合适的 异步消息中间件 来更新 数据库
     * 4*.最终还是要根据业务把大业务分割成不同的小业务, 最终解决问题
     */
    @RequestMapping("/shopping")
    public Object fastShopping(User user) {
        try {

            /**zookeeper 分布式锁在 service 有介绍,

             根据项目需求也可以换别的分布式锁实现,选择一种最合适的.

             网上实现的锁有 redis | Kafka 等 我知道的就这么多,
             我也是参考别人的项目, 把核心的思想 引出来(举一反三).
             参考项目地址:
             https://gitee.com/52itstyle/spring-boot-seckill.git

             注意: 这个列子的真实应用场景 适合少量几百的并发请求
             */

            return seckillService.fastOrders(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }

    }



}
