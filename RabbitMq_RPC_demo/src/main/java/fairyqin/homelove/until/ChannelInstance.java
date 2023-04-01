package fairyqin.homelove.until;

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
 * @CreateDate 2023-03-30 11:47:54
 * @Description 获取Channel的连接
 **/
@Slf4j
public class ChannelInstance {
    public static Channel getInstance()throws Exception{
        //创建 创建连接的工厂
        ConnectionFactory connectionFactory =new ConnectionFactory();
        //为工厂添加配置
        connectionFactory.setHost("192.168.241.128");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");
        //如果为设置prot,则为默认端口5672
        connectionFactory.setPort(5672);
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //返回channel
        return connection.createChannel();
    }

}
