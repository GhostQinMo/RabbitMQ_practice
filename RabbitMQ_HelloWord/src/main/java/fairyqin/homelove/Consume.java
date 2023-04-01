package fairyqin.homelove;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author Black_ghost
 * @title: Consume
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @CreateDate 2023-03-12 16:34:11
 * @Description 消息的消费者
 **/


@Slf4j
public class Consume {
    private final static String QUEUE_NAME = "hello_word";

    public static void main(String[] args) throws Exception {
        //创建  创建连接的工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //配置连接工厂的配置信息
        connectionFactory.setHost("192.168.241.128");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");
        //创建连接
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare(QUEUE_NAME, true, false, true, null);
        //等待接收消息
        //接收消息回调的方法
        log.info("等待接收消息");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收到的消息的标签为" + consumerTag);
            System.out.println("接收到的消息为===>" + new String(message.getBody()));
        };

        //未成功接收消息回调的方法
        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {
                System.out.println("消息未被正确接收");
            }
        };

        //设置消费者自动应答
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
