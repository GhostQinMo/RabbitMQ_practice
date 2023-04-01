package fariyqin.homelove.until;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author Black_ghost
 * @title: ChannelInstance
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-20 20:13:18
 * @Description 获取连接的工具类
 **/
public class ChannelInstance {
    public static Channel getInstance() throws Exception{
        //创建 创建连接的工厂
        ConnectionFactory connectionFactory=new ConnectionFactory();
        //配置连接的基本信息
        connectionFactory.setHost("192.168.241.128");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");
        //创建连接
        Connection connection=connectionFactory.newConnection();
        //创建channel;
        Channel channel = connection.createChannel();
        return channel;
    }
}
