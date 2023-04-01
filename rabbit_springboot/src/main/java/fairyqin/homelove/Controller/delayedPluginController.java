package fairyqin.homelove.Controller;

import fairyqin.homelove.config.delayed_config;
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
 * @title: delayedPluginController
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-22 17:55:41
 * @Description 依赖于插件实现消息延迟
 **/
@RestController
@Slf4j
@RequestMapping("/fairy")
public class delayedPluginController {
    @Autowired
    RabbitTemplate rabbitTemplate;
    //使用插件实现TTL
    //localhost:9090/fairy/sendDelayedMessage/30000/message消息延迟30000ms
    //localhost:9090/fairy/sendDelayedMessage/10000/message消息延迟10000ms
    @RequestMapping("sendDelayedMessage/{TTL}/{message}")
    public void  sendDelayedMessage(
            @PathVariable("TTL") Integer ttl,
            @PathVariable("message") String message
    ){
        log.info("消息发送者：当前时间{}，向绑定routingkey为{}的队列发送消息，消息体为>{},消息的TTL为{}",
                new Date(), delayed_config.DELAYED_ROUTINGKEY
                , message, ttl);
        //消息的后置处理器
        MessagePostProcessor messagePostProcessor1=(ProcessMessage)->{
            //这里不是设置过期时间，而是设置延迟时间
            ProcessMessage.getMessageProperties().setDelay(ttl);
            return ProcessMessage;
        };
        rabbitTemplate.convertAndSend(delayed_config.DELAYED_EXCHANGE
                , delayed_config.DELAYED_ROUTINGKEY
                , message,messagePostProcessor1);
    }
}
