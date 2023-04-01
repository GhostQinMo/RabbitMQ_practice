package fairyqin.homelove.until;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Black_ghost
 * @title: getChannelDownStream
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-27 19:57:04
 * @Description 获取联邦交换机中的下游联channel
 **/
@Slf4j
public class getChannelUpStream {
    public static Channel getChannelUpStream() throws Exception {
        //创建 创建连接的工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //为工厂添加下游节点的信息
        connectionFactory.setPort(32797);
        connectionFactory.setUsername("admin");
        connectionFactory.setHost("192.168.241.128");
        connectionFactory.setPassword("123");
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //返回channel
        Channel channel = connection.createChannel();
        log.info("返回UpStream的channel");
        return channel;
    }
}
