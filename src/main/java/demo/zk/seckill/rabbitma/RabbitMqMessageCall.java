package demo.zk.seckill.rabbitma;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.zk.seckill.entity.ShopItem;
import demo.zk.seckill.service.SeckillService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;


/**
 * Created by free on 18-7-16.
 * 1.更新数据库库存
 * 2.写日志等
 */
public class RabbitMqMessageCall implements MessageListener {

    private SeckillService seckillService;

    /**
     * rabbitma 异步消息
     */
    private AmqpTemplate amqpTemplate;

    /**json 序列*/
    private ObjectMapper objectMapper = new ObjectMapper();


    public void onMessage(Message message) {
        try {

            String json = handlerString(new String(message.getBody(), "utf-8"));
            ShopItem shopItem = objectMapper.readValue(json, ShopItem.class);
            /**更新库存*/
            seckillService.updateShopItem(shopItem);
        } catch (Exception e) {
            e.printStackTrace();
            //amqpTemplate.
        }

    }

    /** id=1000:num=7 to {"id":"1000", "num":"7"}*/
    public String handlerString(String pre) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");

        String[] its = pre.split(":");
        String sign = "=";
        int index = 0;
        for (int i = 0; i < its.length; i++) {
            stringBuilder.append("\"");
            String str = its[i];
            index = str.indexOf(sign);
            stringBuilder.append(str.substring(0, index));
            stringBuilder.append("\":\"");
            stringBuilder.append(str.substring(index + 1, str.length()));
            stringBuilder.append("\"");
            if (i + 1 != its.length) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("}");
        return  stringBuilder.toString();
    }

    public void setSeckillService(SeckillService seckillService) {
        this.seckillService = seckillService;
    }

    public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

}
