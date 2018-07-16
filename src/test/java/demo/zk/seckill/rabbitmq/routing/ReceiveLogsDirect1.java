package demo.zk.seckill.rabbitmq.routing;

/**
 * @author hushuang
 *
 */

import com.rabbitmq.client.*;
import demo.zk.seckill.util.RabbitmqUtil;

import java.io.IOException;

public class ReceiveLogsDirect1 {

	private static final String TASK_QUEUE_NAME = "task_queue";

	private final static String VIRTUAL_HOST = "test_vhosts";

	/** 交换器名称*/
	private static final String EXCHANGE_NAME = "direct_logs";
	/**路由关键字*/
	private static final String[] routingKeys = new String[]{"info" ,"warning", "error"};
	
	public static void main(String[] argv) throws Exception {


		Connection connection = RabbitmqUtil.createConnection();
		Channel channel = connection.createChannel();
		/**声明交换器*/
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		String queueName = "test_queue";
		/**声明一个队列 -- 在RabbitMQ中，队列声明是幂等性的（一个幂等操作的特点是其任意多次执行所产生的影响均与一次执行的影响相同），
		 * 也就是说，如果不存在，就创建，如果存在，不会对已经存在的队列产生任何影响*/
		channel.queueDeclare(queueName, true, false, false, null);



		/***根据路由关键字进行多重绑定*/
		for (String severity : routingKeys) {
			channel.queueBind(queueName, EXCHANGE_NAME, severity);
			System.out.println("ReceiveLogsDirect1 exchange:"+EXCHANGE_NAME+", queue:"+queueName+", BindRoutingKey:" + severity);
		}
		System.out.println("ReceiveLogsDirect1 [*] Waiting for messages. To exit press CTRL+C");

		Consumer consumer = new DefaultConsumer(channel) {

			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
			}
		};
		channel.basicConsume(queueName, true, consumer);
	}
}
