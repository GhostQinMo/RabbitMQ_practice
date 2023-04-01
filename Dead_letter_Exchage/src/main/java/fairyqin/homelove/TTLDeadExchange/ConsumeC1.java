package fairyqin.homelove.TTLDeadExchange;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import fairyqin.homelove.until.ChannelFactory;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Black_ghost
 * @title: ConsumeC1
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-21 09:22:58
 * @Description 正常消息的消费者 (如果消息超时，将消息放入设置的死信队列中)
 **/
@Slf4j
public class ConsumeC1 {
    private static final String NORMAL_ROUTINGKEY = "zhangsan";
    public static final String DEAD_ROUTINGKEY = "lisi";
    private static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";
    private static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] args) throws Exception {
        //获取channel
        Channel channelInstance = ChannelFactory.getChannelInstance();
        //创建死信处理队列,并设置队列绑定到私信交换机
        //1. 创建direct类型的死信交换机
        channelInstance.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //这里是学习联邦交换机的时候随便找了个地方创建联邦交换机(这里使用直接类型的交换机，其他的类型的交换机也应该可以)
//        connectionFactory.setHost("192.168.241.128"); 该节点作为下游（downstram）
        channelInstance.exchangeDeclare("federation_exchange",BuiltinExchangeType.DIRECT);

        //这个需要在ConsumeC1中声明才行,且需要在binding之前声明
        boolean isDurable=false;
        channelInstance.queueDeclare(ConsumeC1.DEAD_QUEUE,isDurable,false,false,null);

        //2. 死信交换机绑定对死信处理队列(但是为先声明DEAD_QUEUE)
        channelInstance.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, DEAD_ROUTINGKEY);

        //创建队列并为正常队列绑定到死信交换机
        // 这里很多参数的key是固定的，需要上官网查
        HashMap<String, Object> arguments = new HashMap<>(3);
        /*因为这些全部在正常队列中设置
        1. 正常队列设置死信交换机 参数key是固定值
        * 2. 设置消息过期时间，但是不建议再这里设置
        * 3. 正常队列设置死信routing-key,参数key是固定值
        * 4. 设置正常队列的最大缓冲值
        */
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //过期时间，单位为ms
        //arguments.put("x-message-ttl",100000);
        arguments.put("x-dead-letter-routing-key", DEAD_ROUTINGKEY);
        //设置缓冲值 测试拒绝策略是关闭限制缓冲
//        arguments.put("x-max-length",6);
        channelInstance.queueDeclare(NORMAL_QUEUE, isDurable, false, false, arguments);
        //normal_queue(正常队列)绑定到正常交换机上
        channelInstance.exchangeDeclare(producer.NORMAL_EXCHANGE,BuiltinExchangeType.DIRECT);
        channelInstance.queueBind(NORMAL_QUEUE, producer.NORMAL_EXCHANGE, NORMAL_ROUTINGKEY);
        //接收消息
        //消息正常处理的回调函数
        log.info("Consume1等待接收消息：");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message =new String(delivery.getBody(),StandardCharsets.UTF_8);
            //拒绝消息队列中的第5条消息
            if(delivery.getEnvelope().getDeliveryTag()==5){
                log.info(">>>>>>>>>>>>>>>>>>>>拒绝消息Tag为{}，消息体为>>>{}",
                        delivery.getEnvelope().getDeliveryTag(),
                        new String(delivery.getBody(),StandardCharsets.UTF_8));
                //requeue 设置为 false 代表拒绝重新入队 该队列如果配置了死信交换机将发送到死信队列中
                channelInstance.basicReject(delivery.getEnvelope().getDeliveryTag(),false);
            }else{
            //log.info("与消息相关的标签为{}", consumerTag);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("接收到的消息为》" + new String(delivery.getBody(), StandardCharsets.UTF_8) +
                        "消息的tag为》" + delivery.getEnvelope().getDeliveryTag());
                channelInstance.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            }
        };
        //消息未正常接收的回调函数
        CancelCallback cancelCallback = (consumeTag) -> {
            log.warn("与消费者关联的tag为{}", consumeTag);
        };
        //测试决绝策略，必须关闭自动应答
        boolean isAutoAck=false;
        channelInstance.basicConsume(NORMAL_QUEUE, isAutoAck,deliverCallback, cancelCallback);
    }
}
