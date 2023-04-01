package fairyqin.homelove.consume;

import fairyqin.homelove.DemoConfig.SpringbooPublisher_confirm_Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author Black_ghost
 * @title: backupExchangeModeConsumer
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-23 15:32:29
 * @Description 备份交换机模式中的消费者
 **/

@Component
@Slf4j
public class backupExchangeModeConsumer {
    //监听backup.queue
    @RabbitListener(queues = SpringbooPublisher_confirm_Config.BACKUP_QUEUE)
    //注意这里的第二参数是String类型的，不是channel类型的了，不然会报转换Error
    public void backupConsumer(Message message, String messagebody){
        log.info("back.queue队列从备份交换机接收到的消息为 {},消息的ReceivedRoutingKey为 {}",
                new String(message.getBody(), StandardCharsets.UTF_8)
        ,message.getMessageProperties().getReceivedRoutingKey());

    }
    //监听backup.queue
    @RabbitListener(queues =SpringbooPublisher_confirm_Config.WARNING_QUEUE)
    public void warningConsumer(Message message,String string){
        log.warn("warning.queue队列从备份队列中接收的消息为 {}",new String(message.getBody(),StandardCharsets.UTF_8));
        log.info("接收到的string类型的属性为 {}",string);
    }
}
