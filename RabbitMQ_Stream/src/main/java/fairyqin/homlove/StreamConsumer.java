package fairyqin.homlove;

import com.rabbitmq.client.Channel;
import fairyqin.homlove.until.ChannelInstance;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Black_ghost
 * @title: StreamConsumer
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-04-01 10:28:14
 * @Description 使用消费者获取Stream中的数据
 **/
@Slf4j
public class StreamConsumer {
    /*
    * 1.获取Stream中的第一条日志（消息）*/
    @Test
    public void getStreamFirst()throws Exception{
        //注意这里的rabbitmq的版本必须在3.9及以上
        Channel instance = ChannelInstance.getInstance();
        instance.basicQos(1); //预取值必须设置，不然很容易宕机
        log.info("开始获取Stream中的第一个可用的消息");
        //Stream中的第一条日志
        boolean isAutoAck=false;
        instance.basicConsume(StreamTest.STREAM_QUEUE,
                isAutoAck,
                Collections.singletonMap("x-stream-offset","first"),
                (consumerTag,delivery)->{
                log.info("Stream中的第一个可用的日志的内容是{}", new String(delivery.getBody(), StandardCharsets.UTF_8));
                instance.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                },consumerTag -> {});
        TimeUnit.SECONDS.sleep(100000000);
    }

    @Test
    public void getStreamDefault()throws  Exception{
        //注意这里的rabbitmq的版本必须在3.9及以上
        Channel instance = ChannelInstance.getInstance();
        instance.basicQos(1); //预取值必须设置，不然很容易宕机
        log.info("开始获取Stream中的第一个可用的消息");
        //Stream中的第一条日志
        boolean isAutoAck=false;
        instance.basicConsume(StreamTest.STREAM_QUEUE,
                isAutoAck,
                (consumerTag,delivery)->{
                    log.info("Stream中的第一个可用的日志的内容是{}", new String(delivery.getBody(), StandardCharsets.UTF_8));
                    instance.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                },consumerTag -> {});
        TimeUnit.SECONDS.sleep(100000000);
    }

    @Test
    public void getStreamLast()throws Exception{
        //注意这里的rabbitmq的版本必须在3.9及以上
        Channel instance = ChannelInstance.getInstance();
        instance.basicQos(1); //预取值必须设置，不然很容易宕机
        log.info("开始获取Stream中的第一个可用的消息");
        //Stream中的第一条日志
        boolean isAutoAck=false;
        instance.basicConsume(StreamTest.STREAM_QUEUE,
                isAutoAck,
                Collections.singletonMap("x-stream-offset","last"),
                (consumerTag,delivery)->{
                    log.info("Stream中的第一个可用的日志的内容是{}", new String(delivery.getBody(), StandardCharsets.UTF_8));
                    instance.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                },consumerTag -> {});
        TimeUnit.SECONDS.sleep(100000000);
    }


    //自定义一个特殊的偏移量,从指定的消息索引为开始读取
    @Test
    public void getStreamCustom()throws Exception{
        //注意这里的rabbitmq的版本必须在3.9及以上
        Channel instance = ChannelInstance.getInstance();
        instance.basicQos(1); //预取值必须设置，不然很容易宕机
        log.info("开始获取Stream中的第一个可用的消息");
        //Stream中的第一条日志
        boolean isAutoAck=false;
        instance.basicConsume(StreamTest.STREAM_QUEUE,
                isAutoAck,
                Collections.singletonMap("x-stream-offset",11),
                (consumerTag,delivery)->{
                    log.info("Stream中的第一个可用的日志的内容是{}", new String(delivery.getBody(), StandardCharsets.UTF_8));
                    instance.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                },consumerTag -> {});
        TimeUnit.SECONDS.sleep(100000000);
    }

    //从相对于当前时间来查找消息
    @Test
    public void getStreamTime()throws Exception{
        //注意这里的rabbitmq的版本必须在3.9及以上
        Channel instance = ChannelInstance.getInstance();
        instance.basicQos(100); //预取值必须设置，不然很容易宕机
//        Date date=new Date(System.currentTimeMillis()-1000*60*60*(8+5)); //因为我是5小时前存放的数据
        Date date=new Date(System.currentTimeMillis());
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>{}",date);
        log.info("开始获取Stream中的第一个可用的消息");
        //Stream中的第一条日志
        boolean isAutoAck=false;
        instance.basicConsume(StreamTest.STREAM_QUEUE,
                isAutoAck,
                Collections.singletonMap("x-stream-offset",date),
                (consumerTag,delivery)->{
                    log.info("Stream中的第一个可用的日志的内容是{}", new String(delivery.getBody(), StandardCharsets.UTF_8));
                    instance.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                },consumerTag -> {});
        TimeUnit.SECONDS.sleep(100000000);
    }
}
