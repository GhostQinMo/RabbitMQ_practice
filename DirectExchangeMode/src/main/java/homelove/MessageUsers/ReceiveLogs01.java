package homelove.MessageUsers;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import homelove.MessageProvide.EmitLog;
import homelove.until.ChannelInstance;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author Black_ghost
 * @title: ReceiveLogs01
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-20 20:33:03
 * @Description console队列
 **/

@Slf4j
public class ReceiveLogs01 {
    private  static  final  String CONSOLE_QUEUE="console";
    public static void main(String[] args)throws Exception {
        Channel channel = ChannelInstance.getInstance();
        //创建console队列
        channel.queueDeclare(CONSOLE_QUEUE,false,false,true,null);
        //绑定交换机 队列与交换机通过routingkey绑定，
        channel.queueBind(CONSOLE_QUEUE, EmitLog.DIRECT_EXCHANGE,EmitLog.error_routingkey);
        //接收消息
        channel.basicQos(1);
        log.info("{}准备接收日志",CONSOLE_QUEUE);
        DeliverCallback deliverCallback=(consumeTag,delivery)->{
            log.info("消息id为{}的消息体为{}",consumeTag,new String(delivery.getBody(),StandardCharsets.UTF_8));
        };
        CancelCallback cancelCallback=(consumeTag)->{
        };
        //自动应答
        channel.basicConsume(CONSOLE_QUEUE,true,deliverCallback,cancelCallback);
    }
}
