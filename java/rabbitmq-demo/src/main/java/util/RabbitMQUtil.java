package util;

import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQUtil {

    private static ConnectionFactory factory = new com.rabbitmq.client.ConnectionFactory();

    static {
        factory.setHost("10.10.10.224");
        factory.setUsername("guest");
        factory.setPassword("guest");
    }

    public static com.rabbitmq.client.Connection getConnection() throws IOException, TimeoutException {
        return factory.newConnection();
    }
}
