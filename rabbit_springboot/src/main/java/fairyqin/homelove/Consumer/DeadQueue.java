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
 * @title: DeadQueue
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-21 22:49:22
 * @Description 死信处理队列
 **/
@Slf4j
@Component
public class DeadQueue {
    //添加监听器
    //参数返回 message 和 channel
    @RabbitListener(queues = {"QD"})
    public void Dead_letter_Queue(Message message, Channel channel){
        String s = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("死信队列：当前时间为{} ，死信队列收到的消息为> {}",new Date().toString(),message);
    }
}
