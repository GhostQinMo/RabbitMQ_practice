package fairyqin.homelove.MessageProvide;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import fairyqin.homelove.until.ChannelInstance;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


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
    public  final static  String TOPIC_EXCHANGE="TOPIC_EXCHANGE";
    public final  static  String orange_routingkey="*.orange.*";
    public final  static  String rabbit_routingkey="*.*.rabbit";
    public final  static  String lazy_routingkey="lazy.#";
    public static void main(String[] args)  throws Exception{
        Channel channel = ChannelInstance.getInstance();
        //声明Topics_EXCHANGE类型的交换机
        /**
         * 声明一个 exchange
         * 1.exchange 的名称
         * 2.exchange 的类型
         */
        channel.exchangeDeclare(TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC);
        /**
         * Q1-->绑定的是
         * 中间带 orange 带 3 个单词的字符串(*.orange.*)
         * Q2-->绑定的是
         * 最后一个单词是 rabbit 的 3 个单词(*.*.rabbit)
         * 第一个单词是 lazy 的多个单词(lazy.#)
         */
        Map<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
        bindingKeyMap.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
        bindingKeyMap.put("quick.orange.fox","被队列 Q1 接收到");
        bindingKeyMap.put("lazy.brown.fox","被队列 Q2 接收到");bindingKeyMap.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
        bindingKeyMap.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
        bindingKeyMap.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
        bindingKeyMap.put("lazy.orange.male.rabbit","是四个单词但匹配 Q2");
        //往交换机中发布消息
        log.info("发布消息：");
        //更具匹配的主题发送
        for (Map.Entry<String,String> temp: bindingKeyMap.entrySet()) {
            channel.basicPublish(TOPIC_EXCHANGE,
                    temp.getKey(),
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    temp.getValue().getBytes(StandardCharsets.UTF_8));
            log.info("routingkey为{}的队列发送的消息为{}",temp.getKey(),temp.getValue());
        }

    }
}
