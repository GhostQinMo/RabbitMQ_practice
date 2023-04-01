package fairyqin.homelove.DownConsume;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import fairyqin.homelove.until.getChannelDownStream;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Black_ghost
 * @title: downStream_FederationExchange
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-27 20:27:46
 * @Description 联邦交换机中的下游
 **/
@Slf4j
public class downStream_FederationExchange {
    public static final String DOWNFEDERATIONEXCHANGE="federation_exchange";
    public static final String FEDERATION_QUEUE="federation_queue";
    public static final String FEDERATION_ROUTINGKE = "fed";
    public static void main(String[] args) throws Exception {
        log.info("创建联邦交换机");
        //获取下游的节点的连接
        Channel channelDownStream = getChannelDownStream.getChannelDownStream();
        //由于federation exchange模式中，下游的必须先创建联邦交换机
        channelDownStream.exchangeDeclare(DOWNFEDERATIONEXCHANGE, BuiltinExchangeType.DIRECT);
        //声明队列，绑定队列
        channelDownStream.queueDeclare(FEDERATION_QUEUE,true,false,false,null);
        channelDownStream.queueBind(FEDERATION_QUEUE, DOWNFEDERATIONEXCHANGE, FEDERATION_ROUTINGKE);
        log.info("下游基本配置基本完成");
    }
}
