package fairyqin.homelove.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author Black_ghost
 * @title: ChannelInstance
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-19 20:52:13
 * @Description RabbitMQ获取连接的信道的方法
 **/
public class ChannelInstance {
    public static Channel getInstance() throws  Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.241.128");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");
        Connection connection = connectionFactory.newConnection();
        return connection.createChannel();
    }
}
