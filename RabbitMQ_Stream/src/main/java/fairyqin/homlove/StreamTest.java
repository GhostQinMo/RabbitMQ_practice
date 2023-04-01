package fairyqin.homlove;

import com.rabbitmq.client.Channel;
import fairyqin.homlove.until.ChannelInstance;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Black_ghost
 * @title: StreamTest
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-31 22:46:49
 * @Description Rabbitmq中的Stream数据结构的使用
 **/
//温馨提示：Stream是rabbitmq3.9版中才引入的一个数据结构
@Slf4j
public class StreamTest {
    public static final String STREAM_QUEUE="Stream_queue";
    public static void main(String[] args) throws Exception {
        log.info("测试创建Stream ");
        Channel instance = ChannelInstance.getInstance();
        //使用Stream必须 声明Stream,像声明队列一样
        //这里key和value的值是固定的
        instance.queueDeclare("Stream_queue", true, false, false,
                Collections.singletonMap("x-queue-type", "stream"));
    }

    /*
     * Stream支持三个动态设置的参数
     * arguments.put("x-queue-type", "stream");
     *arguments.put("x-max-length-bytes", 20_000_000_000); // maximum stream size: 20 GB
     * x-max-age  //Stream的最大期限 默认不需要设置
     * arguments.put("x-stream-max-segment-size-bytes", 100_000_000); // size of segment files: 100 MB
     * */
    @Test
    public void ArgumentTest() throws Exception{
        log.info("测试创建Stream ");
        Channel instance = ChannelInstance.getInstance();
        Map<String,Object > argument=new HashMap<>(3);
        argument.put("x-queue-type","stream");  //这里的key和value是一样的
        argument.put("x-max-length-bytes",2000000000);
        argument.put("x-stream-max-segment-size-bytes",100_000_000);   //单位为字节
        //使用Stream必须 声明Stream,像声明队列一
        log.info("开始发布消息");
        instance.queueDeclare(STREAM_QUEUE, true, false, false,argument);
        for (int i = 0; i < 10; i++) {
            String message="fairyqinmessage"+i+"timenew";
            instance.basicPublish("",STREAM_QUEUE,null,message.getBytes(StandardCharsets.UTF_8));
            log.info("发布消息的消息体为{}",message);
            TimeUnit.SECONDS.sleep(5);
        }
        TimeUnit.SECONDS.sleep(10000000);
    }

}
