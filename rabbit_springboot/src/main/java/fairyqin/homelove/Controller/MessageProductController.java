package fairyqin.homelove.Controller;

import fairyqin.homelove.config.TTLconfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author Black_ghost
 * @title: MessageProductController
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-21 22:36:02
 * @Description 消息发布接口
 **/
@Slf4j
@RestController
@RequestMapping("/fairy")
public class MessageProductController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    //localhost:9090/fairy/sendMessage/XA/向绑定Routingkey为XA的队列发送消息
    //localhost:9090/fairy/sendMessage/XB/向绑定Routingkey为XB的队列发送消息
    //在队列上设置TTL案例
    @RequestMapping("sendMessage/{routingkey}/{message}")
    public void sendMessage(@PathVariable("message") String message,
                            @PathVariable("routingkey") String routingkey) {
        log.info("消息发送者：当前时间{}，向绑定routingkey为{}的队列发送消息，消息体为>{}",
                new Date(),
                routingkey, message);
        rabbitTemplate.convertAndSend(TTLconfig.NORMAL_EXCHANGE, routingkey, message);
    }

    //在消息生产者中为每条消息单独设置TTL
    //localhost:9090/fairy/sendMessageAutoTTL/30000/message消息延迟30000ms
    //localhost:9090/fairy/sendMessageAutoTTL/10000/message消息延迟10000ms
    @RequestMapping("sendMessageAutoTTL/{ttl}/{message}")
    public void sendMessage(@PathVariable("message") String message,
                            @PathVariable("ttl") long ttl) {
        log.info("消息发送者：当前时间{}，向绑定routingkey为{}的队列发送消息，消息体为>{},消息的TTL为{}",
                new Date(), TTLconfig.QC_binding_X_routingkey
                , message, ttl);
        MessagePostProcessor messagePostProcessor = (swap_message) -> {
            swap_message.getMessageProperties().setExpiration(String.valueOf(ttl));
            return swap_message;
        };
        rabbitTemplate.convertAndSend(TTLconfig.NORMAL_EXCHANGE, TTLconfig.QC_binding_X_routingkey, message, messagePostProcessor);
    }

}
