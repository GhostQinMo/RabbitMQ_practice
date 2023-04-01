package fairyqin.homelove.Consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author Black_ghost
 * @title: delayedPlugin_Consume
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-22 17:58:47
 * @Description 基于插件实现的延迟消息的监听器
 **/
@Component
@Slf4j
public class delayedPlugin_Consume {
     //添加插件实现延时队列的监听器
    @RabbitListener(queues = {"delayed_queue"})
    public void Delayed_Queue(Message message, Channel channel){
        String s = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("死信队列：当前时间为{} ，死信队列收到的消息为> {}",new Date(),message);
    }
}
