package fairyqin.homelove.product;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import fairyqin.homelove.util.ChannelInstance;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author Black_ghost
 * @title: WorkerProduct
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-19 21:13:06
 * @Description 任务发布者
 **/
@Slf4j
public class WorkerProduct {
    private final static String QUEUE_NAME = "work_queue";

    public static void main1(String[] args) throws Exception {
        //通过工具类获取连接
        Channel instance = ChannelInstance.getInstance();
        //声明队列 ，如果队列不存在会创建队列 注意:这里需要设置一个队列中的是非exclusive的(非独享的)
        // TODO 也可以把声明队列也进行封装
        //声明队列之前 使用passive判断队列是否存在
        AMQP.Queue.DeclareOk declareOk = instance.queueDeclarePassive(QUEUE_NAME);
        try {
            if (declareOk.getMessageCount() > 0) {
                log.info("{} 队列中有消息，消息数量为{}", QUEUE_NAME, declareOk.getMessageCount());
            }else{
                log.info("{}存在，但是没有消息", QUEUE_NAME);
            }
        } catch (Exception e) {
            log.error("{}队列不存在", QUEUE_NAME);
        }
        instance.queueDeclare(QUEUE_NAME, true, false, false, null);
        //从控制台中发送消息
        Scanner scanner = new Scanner(System.in);
        log.info("请输入消息：");
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            //这里还是使用默认交换机
            instance.basicPublish("", QUEUE_NAME, null, s.getBytes(StandardCharsets.UTF_8));
        }
    }


    
}
