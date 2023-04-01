package fariyqin.homelove.MessageUsers;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import fariyqin.homelove.MessageProvide.EmitLog;
import fariyqin.homelove.until.ChannelInstance;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author Black_ghost
 * @title: ReceiveLogs01
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-20 20:33:03
 * @Description 日志接收者1
 **/

@Slf4j
public class ReceiveLogs01 {
    public static void main(String[] args)throws Exception {
        Channel channel = ChannelInstance.getInstance();
        //创建随机队列
        String queue = channel.queueDeclare().getQueue();
        //绑定交换机 队列与交换机通过routingkey绑定，这里的routingkey为”“
        channel.queueBind(queue, EmitLog.FANOUT_EXCHANGE,"");
        //接收消息
        channel.basicQos(1);
        log.info("准备接收日志");
        DeliverCallback deliverCallback=(consumeTag,delivery)->{
            log.info("消息id为{}的消息体为{}",consumeTag,new String(delivery.getBody(), StandardCharsets.UTF_8));
        };
        CancelCallback cancelCallback=(consumeTag)->{
        };
        channel.basicConsume(queue,true,deliverCallback,cancelCallback);
    }
}
