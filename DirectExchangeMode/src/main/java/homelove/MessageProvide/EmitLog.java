package homelove.MessageProvide;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import homelove.until.ChannelInstance;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

;

/**
 * @author Black_ghost
 * @title: EmitLog
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-20 20:21:10
 * @Description日志发布者
 **/
@Slf4j
public class EmitLog {
    public  final static  String DIRECT_EXCHANGE="DIRECT_EXCHANGE";
    public final  static  String QUEUE="logs";
    public final  static  String info_routingkey="info";
    public final  static  String error_routingkey="error";
    public final  static  String warning_routingkey="warning";
    public static void main(String[] args)  throws Exception{
        Channel channel = ChannelInstance.getInstance();
        //声明DIRECT_EXCHANGE类型的交换机
        /**
         * 声明一个 exchange
         * 1.exchange 的名称
         * 2.exchange 的类型
         */
        channel.exchangeDeclare(DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);
        //往交换机中发布消息
        log.info("发布消息：");
        Scanner scanner=new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.nextLine();
            //如果消息中含有info 和warning 的日志放入console队列
            //如果消息中含有error的日志放入disk队列
            if (message.contains(info_routingkey)){
                //消息持久化
                channel.basicPublish(DIRECT_EXCHANGE,info_routingkey,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        message.getBytes(StandardCharsets.UTF_8));
            }else if (message.contains(warning_routingkey)) {
                //消息持久化
                channel.basicPublish(DIRECT_EXCHANGE, warning_routingkey,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        message.getBytes(StandardCharsets.UTF_8));
            }else if (message.contains(error_routingkey)){
                channel.basicPublish(DIRECT_EXCHANGE,error_routingkey,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        message.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
