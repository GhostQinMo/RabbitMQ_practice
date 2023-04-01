package fairyqin.homelove;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import fairyqin.homelove.util.ChannelInstance;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author Black_ghost
 * @title: PriorityProvider
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-23 21:07:53
 * @Description 优先级消息提供者
 **/

@Slf4j
public class PriorityProvider {
    public static final String PRIORITYQUEUE = "priorityqueue";
    public static void main(String[] args) throws Exception {
        Channel channelInstance = ChannelInstance.getChannelInstance();
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().priority(2).build();
        log.info("开始发送消息");
        for (int i = 0; i < 10; i++) {
            String s = "消息体welcome to fairyqin " + i;
            //使用默认交换机
            if (i == 6) {
                AMQP.BasicProperties basicProperties9 = new AMQP.BasicProperties().builder().priority(9).build();
                channelInstance.basicPublish("", PRIORITYQUEUE, basicProperties9, s.getBytes(StandardCharsets.UTF_8));
                log.info("发送的消息为{}，消息的优先级为{}，是第{}个消息",s,basicProperties9.getPriority(),i);
            } else {
                channelInstance.basicPublish("", PRIORITYQUEUE, basicProperties, s.getBytes(StandardCharsets.UTF_8));
                log.info("发送的消息为{}，消息的优先级为{}，是第{}个消息",s,basicProperties.getPriority(),i);
            }

        }
    }
}
