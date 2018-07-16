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

	/** ����������*/
	private static final String EXCHANGE_NAME = "direct_logs";
	/**·�ɹؼ���*/
	private static final String[] routingKeys = new String[]{"info" ,"warning", "error"};
	
	public static void main(String[] argv) throws Exception {


		Connection connection = RabbitmqUtil.createConnection();
		Channel channel = connection.createChannel();
		/**����������*/
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		String queueName = "test_queue";
		/**����һ������ -- ��RabbitMQ�У������������ݵ��Եģ�һ���ݵȲ������ص�����������ִ����������Ӱ�����һ��ִ�е�Ӱ����ͬ����
		 * Ҳ����˵����������ڣ��ʹ�����������ڣ�������Ѿ����ڵĶ��в����κ�Ӱ��*/
		channel.queueDeclare(queueName, true, false, false, null);



		/***����·�ɹؼ��ֽ��ж��ذ�*/
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
