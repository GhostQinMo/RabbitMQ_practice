package fariyqin.homelove.MessageProvide;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import fariyqin.homelove.until.ChannelInstance;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

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
    public  final static  String FANOUT_EXCHANGE="FANOUT_EXCHANGE";
    public final  static  String QUEUE="logs";
    public static void main(String[] args)  throws Exception{
        Channel channel = ChannelInstance.getInstance();
        //声明fanout类型的交换机
        /**
         * 声明一个 exchange
         * 1.exchange 的名称
         * 2.exchange 的类型
         */
        channel.exchangeDeclare(FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT);
        //往交换机中发布消息
        log.info("发布消息：");
        Scanner scanner=new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.nextLine();
            //消息持久化
            channel.basicPublish(FANOUT_EXCHANGE,"", MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
