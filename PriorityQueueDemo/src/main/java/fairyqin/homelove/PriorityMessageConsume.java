package fairyqin.homelove;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import fairyqin.homelove.util.ChannelInstance;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;

/**
 * @author Black_ghost
 * @title: PriorityMessageConsume
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-23 21:08:50
 * @Description 优先级消息提供者
 **/
@Slf4j
public class PriorityMessageConsume {
    public static void main(String[] args) throws Exception {
        //获取channel
        Channel channelInstance = ChannelInstance.getChannelInstance();
        //创建队列,设置优先级队列
        HashMap<String, Object> arguments = new HashMap<>(1);
        arguments.put("x-max-priority", 10);
        channelInstance.queueDeclare(PriorityProvider.PRIORITYQUEUE, true, false, false, arguments);
        //接收消息
        log.info("等待接收消息：");
        DeliverCallback deliverCallback = (ConsumerTag, delivery) -> {
            log.info("{} 队列接收到的消息体为：{},消息的标识为{},该消息与consumer相关的Tag为{},消息优先级为{}",
                    PriorityProvider.PRIORITYQUEUE, new String(delivery.getBody()),
                    delivery.getEnvelope().getDeliveryTag(), ConsumerTag
            ,delivery.getProperties().getPriority());
        };
        channelInstance.basicConsume(PriorityProvider.PRIORITYQUEUE, true, deliverCallback, (messageTag) -> {
        });

    }
}
