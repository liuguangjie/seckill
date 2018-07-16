package demo.zk.seckill.rabbitmq.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import demo.zk.seckill.util.RabbitmqUtil;

/**
 * @author hushuang
 *
 */
public class RoutingSendDirect {

    private static final String EXCHANGE_NAME = "direct_logs";
 // 路由关键字
 	private static final String[] routingKeys = new String[] {"info" ,"warning", "error"};
 	
    public static void main(String[] argv) throws Exception {


        Connection connection = RabbitmqUtil.createConnection();
        Channel channel = connection.createChannel();
//		声明交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = "test_queue";

        channel.queueDeclare(queueName, true, false, false, null);
//		发送消息
        for(String severity :routingKeys){
        	String message = "Send the message level: " + severity;
        	channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
        	System.out.println(" [x] Sent '" + severity + "':'" + message + "'");
        }
        channel.close();
        connection.close();
    }
}