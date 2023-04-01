package fairyqin.homelove;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author Black_ghost
 * @title: MessageProduct
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @CreateDate 2023-03-19 18:15:04
 * @Description 消息生产者
 **/
@Slf4j
public class MessageProduct {
    private final static String QUEUE_NAME = "hello_word";

    public static void main(String[] args) throws Exception {
        //创建一个创建连接的工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置连接工厂的一些配置信息包括 ip username passowrd
        factory.setHost("192.168.241.128");
        factory.setUsername("admin");
        factory.setPassword("123");
        //创建连接
        Connection connection = factory.newConnection();
        //获取信道（channel）
        Channel channel = connection.createChannel();
        /**
         * 1.队列名称
         * 2.队列里面的消息是否持久化 默认消息存储在内存中
         * 3.该队列是否只供一个消费者进行消费 是否进行共享 fase可以多个消费者消费
         * 4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME, true, false, true, null);
        String message = "welcome RabbitMQ";
        /**
         *
         发送一个消息
         * 1.发送到那个交换机
         * 2.routingkey是哪个
         * 3.其他的参数信息
         * 4.发送消息的消息体
         */
        //使用默认的交换机，当使用默认的交换机的时候可以用队列名来代替routingkey
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
        log.info("生产者往{}队列中发送一条消息,消息的内容为=>>>{}", QUEUE_NAME, message);
    }
}
