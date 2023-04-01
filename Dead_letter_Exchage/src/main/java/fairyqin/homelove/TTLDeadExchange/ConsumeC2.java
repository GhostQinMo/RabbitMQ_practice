package fairyqin.homelove.TTLDeadExchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import fairyqin.homelove.until.ChannelFactory;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author Black_ghost
 * @title: ConsumeC2
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-21 10:39:31
 * @Description 死信的消费者
 **/
@Slf4j
public class ConsumeC2 {
    public static void main(String[] args)  throws Exception{
        Channel channelInstance = ChannelFactory.getChannelInstance();
        //死信交换机在Consumec1中声明过了
        //声明死死信队列(这个需要在ConsumeC1中声明才行)
        boolean isDurable=false;
        channelInstance.queueDeclare(ConsumeC1.DEAD_QUEUE,isDurable,false,false,null);
        //接收消息
        //自动应答
        DeliverCallback deliverCallback= (consumerTag,delivery)->{
            log.info("与消息相关的标签为{}", consumerTag);
            System.out.println("接收到的消息为》" + new String(delivery.getBody(), StandardCharsets.UTF_8) +
                    "消息的tag为》" + delivery.getEnvelope().getDeliveryTag());
        };
        channelInstance.basicConsume(ConsumeC1.DEAD_QUEUE,true,deliverCallback,(consumerTag)->{
            log.warn("与消费者关联的tag为{}", consumerTag);
        });
        log.info("一些其他的处理");
    }
}
