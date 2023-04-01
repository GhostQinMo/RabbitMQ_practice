package fairyqin.homelove.UpStreamConsume;

import com.rabbitmq.client.Channel;
import fairyqin.homelove.until.getChannelUpStream;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Black_ghost
 * @title: upStream_FederationExchange
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-27 21:24:06
 * @Description 上游
 **/
@Slf4j
public class upStream_FederationExchange {
    public static void main(String[] args) throws Exception{
        log.info("获取上游的连接");
        Channel channelUpStream = getChannelUpStream.getChannelUpStream();
        //测试由docker启动的rabbitmq是否可以连接
         channelUpStream.queueDeclare("Docker_queue",true,false,false,null);
        log.info("连接测试成功");
    }
}
