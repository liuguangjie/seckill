package demo.zk.seckill.rabbitmq.queues;

import com.rabbitmq.client.*;
import demo.zk.seckill.util.RabbitmqUtil;

import java.io.IOException;

public class Worker2 {
	private static final String TASK_QUEUE_NAME = "task_queue";

	public static void main(String[] argv) throws Exception {

		final Connection connection = RabbitmqUtil.createConnection();
		final Channel channel = connection.createChannel();

		channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
		System.out.println("Worker2 [*] Waiting for messages. To exit press CTRL+C");
		// ÿ�δӶ����л�ȡ����
		channel.basicQos(1);

		final Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");

				System.out.println("Worker2 [x] Received '" + message + "'");
				try {
					doWork(message);
				} finally {
					System.out.println("Worker2 [x] Done");
					// ��Ϣ�������ȷ��
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};
		// ��Ϣ�������ȷ��
		channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
	}

	/**
	 * ������
	 * 
	 * @param task
	 *            void
	 */
	private static void doWork(String task) {
		try {
			Thread.sleep(1000); // ��ͣ1����
		} catch (InterruptedException _ignored) {
			Thread.currentThread().interrupt();
		}
	}
}
