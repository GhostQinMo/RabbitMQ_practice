package fairyqin.homelove.TTLDeadExchange;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import fairyqin.homelove.until.ChannelFactory;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author Black_ghost
 * @title: producer
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-21 08:56:40
 * @Description 任务的生产者
 **/
@Slf4j
public class producer {
    public static final  String NORMAL_EXCHANGE="normal_exchange";
    private static final  String NORMAL_ROUTINGKEY="zhangsan";
    public static void main(String[] args)throws Exception{
        //获取连接
        Channel channelInstance = ChannelFactory.getChannelInstance();
        // 声明direct类型的交换机，
        channelInstance.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        //发布消息
        log.info("即将发送消息：");
        //设置消息TTL过期时间(这里是设置每个消息的过期时间为定值)
        //TODO 这里AMQP的注释文档没有找到
        //expiration的参数是一个String类型的单位为ms的过期时间
//        AMQP.BasicProperties properties=new AMQP.BasicProperties().builder().expiration("10000").build();
//        String delay="100000";
//        AMQP.BasicProperties properties=new AMQP.BasicProperties().builder().expiration(delay).build();
        AMQP.BasicProperties properties=new AMQP.BasicProperties().builder().expiration("0").build();
        for (int i = 0; i < 10; i++) {
            String  message="消息体"+i;
            //TODO　这里获取的下一个消息的发布序列总是0啊,但是在如何处理异步为确认消息 获取序列是变化的
            long nextPublishSeqNo = channelInstance.getNextPublishSeqNo();
            log.info("下一个消息发布的消息序列》{}，消息体为》{}的消息",nextPublishSeqNo,message);
            //消息持久化
            //测试TTL 造成的死信
            channelInstance.basicPublish(NORMAL_EXCHANGE,
                                        NORMAL_ROUTINGKEY,
                                        properties,
                    message.getBytes(StandardCharsets.UTF_8));
            /*//测试限制队列长队造成的死信和消息拒绝
            channelInstance.basicPublish(NORMAL_EXCHANGE,
                    NORMAL_ROUTINGKEY,
                    null,
                    message.getBytes(StandardCharsets.UTF_8));*/
//            delay="0";
        }

    }
}
