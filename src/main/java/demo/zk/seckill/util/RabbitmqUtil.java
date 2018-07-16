package demo.zk.seckill.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by free on 18-7-16.
 */
public abstract class RabbitmqUtil {



    public static Connection createConnection() throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(Route.HOST);
        factory.setVirtualHost(Route.VIRTUAL_HOST);
        factory.setPort(Route.PORT);
        factory.setUsername(Route.USER);
        factory.setPassword(Route.PASSWD);

        return factory.newConnection();
    }


    public final static void closeConnection(Connection connection) {

        if(connection != null ) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



}
