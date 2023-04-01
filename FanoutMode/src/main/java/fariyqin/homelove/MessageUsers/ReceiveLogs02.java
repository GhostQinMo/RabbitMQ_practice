package fariyqin.homelove.MessageUsers;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import fariyqin.homelove.MessageProvide.EmitLog;
import fariyqin.homelove.until.ChannelInstance;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author Black_ghost
 * @title: ReceiveLogs01
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-20 20:33:03
 * @Description 将接收的日志写入磁盘中
 **/

@Slf4j
public class ReceiveLogs02 {
    public static void main(String[] args)throws Exception {
        Channel channel = ChannelInstance.getInstance();
        //创建随机队列
        String queue = channel.queueDeclare().getQueue();
        //绑定交换机 队列与交换机通过routingkey绑定，这里的routingkey为”“
        channel.queueBind(queue, EmitLog.FANOUT_EXCHANGE,"");
        //接收消息
        channel.basicQos(1);
        log.info("准备接收日志");
        //这里需要使用绝对路径，相对路径找不到
        File file=new File("D:\\FairyHomeWorkSpace\\RabbitMQ_practice\\FanoutMode\\src\\main\\resources\\logs.txt");
        DeliverCallback deliverCallback=(consumeTag,delivery)->{
            //这里使用了apache的io工具报
            FileUtils.writeStringToFile(file,new String(delivery.getBody()),StandardCharsets.UTF_8,true);
            log.info("消息id为{}的消息体为{}",consumeTag,new String(delivery.getBody(), StandardCharsets.UTF_8));
        };
        CancelCallback cancelCallback=(consumeTag)->{
        };
        channel.basicConsume(queue,true,deliverCallback,cancelCallback);
    }
}
