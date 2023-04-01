package homelove.MessageUsers;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import homelove.MessageProvide.EmitLog;
import homelove.until.ChannelInstance;
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
    private  static  final  String DISK_QUEUE="disk";
    public static void main(String[] args)throws Exception {
        Channel channel = ChannelInstance.getInstance();
        //创建console队列
        channel.queueDeclare(DISK_QUEUE,false,false,true,null);
        //绑定交换机 队列与交换机通过routingkey绑定，
        channel.queueBind(DISK_QUEUE, EmitLog.DIRECT_EXCHANGE,EmitLog.info_routingkey);
        channel.queueBind(DISK_QUEUE, EmitLog.DIRECT_EXCHANGE,EmitLog.warning_routingkey);
        //接收消息
        channel.basicQos(1);
        log.info("{}准备接收日志",DISK_QUEUE);
        //这里需要使用绝对路径，相对路径找不到
        File file=new File("D:\\FairyHomeWorkSpace\\RabbitMQ_practice\\DirectExchangeMode\\src\\main\\resources\\logs.txt");
        DeliverCallback deliverCallback=(consumeTag,delivery)->{
            //这里使用了apache的io工具报
            FileUtils.writeStringToFile(file,new String(delivery.getBody())+"\r\n",StandardCharsets.UTF_8,true);
            log.info("消息id为{}的消息体为{}",consumeTag,new String(delivery.getBody(), StandardCharsets.UTF_8));
        };
        CancelCallback cancelCallback=(consumeTag)->{
        };
        //自动应答
        boolean isAutoAck=true;
        channel.basicConsume(DISK_QUEUE,isAutoAck,deliverCallback,cancelCallback);
    }
}
