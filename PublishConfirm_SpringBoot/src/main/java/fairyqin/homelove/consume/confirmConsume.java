package fairyqin.homelove.consume;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author Black_ghost
 * @title: confirmConsume
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-23 12:14:05
 * @Description 消息执行者
 **/
@Component
@Slf4j
public class confirmConsume {
    //添加监听器
    @RabbitListener(queues = "confirm_queue")
    public void ConfirmListener(Message message, Channel channel) {
        //该案例是交换机收到消息
        log.info("从confirm_queue队列中接受的到的消息为：{},消息的id为:{},routingkey为 {}", new String(message.getBody(), StandardCharsets.UTF_8)
                , message.getMessageProperties().getMessageId(),message.getMessageProperties().getReceivedRoutingKey());
    }
}

