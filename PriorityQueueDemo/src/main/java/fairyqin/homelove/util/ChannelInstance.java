package fairyqin.homelove.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Black_ghost
 * @title: ChannelInstance
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-23 21:11:57
 * @Description 创建channel的工具
 **/
@Slf4j
public class ChannelInstance {
    public static Channel getChannelInstance()throws  Exception{
        //创建连接工厂
        ConnectionFactory connectionFactory=new ConnectionFactory();
        //配置连接信息
        connectionFactory.setHost("192.168.241.128");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");
        connectionFactory.setPort(5672);
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //创建channel
        Channel channel = connection.createChannel();
        return channel;
    }
}
