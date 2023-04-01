package fairyqin.homelove.until;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Black_ghost
 * @title: ChannelFactory
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-21 08:58:29
 * @Description 获取channel的工具类
 **/
@Slf4j
public class ChannelFactory {
    private static Connection connection;
    static{
        //创建 创建连接的工厂
        ConnectionFactory connectionFactory=new ConnectionFactory();
        //配置工厂信息
        connectionFactory.setHost("192.168.241.128");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");

        //获创建连接
        try {
            connection= connectionFactory.newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
    public static  Channel getChannelInstance() throws Exception{
        //这里为什么需要使用这个静态方法来获取连接而不是直接将Connection用public修饰，直接通过类名来获取
        //原因是如果使用类来获取的话，不能再获取connection之前做一些操作，使用方法可以做一些额外的操作
        log.info("获取channel之前做一些操作");
        //创建channel
        return connection.createChannel();
    }
}
