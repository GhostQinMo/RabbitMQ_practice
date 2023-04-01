package fairyqin.homelove.product;

import com.rabbitmq.client.Channel;
import fairyqin.homelove.util.ChannelInstance;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @author Black_ghost
 * @title: WorkerProduct
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-19 21:13:06
 * @Description 任务发布者  测试消息手动应答 消息重新入队案例
 **/
@Slf4j
public class WorkerProduct {
    private final static String UNAUTOACK_QUEUE = "UnAutoAck_queue";

    public static void main(String[] args) throws Exception {
        //通过工具类获取连接
        Channel instance = ChannelInstance.getInstance();
        //声明队列 ，如果队列不存在会创建队列 注意:这里需要设置一个队列中的是非exclusive的(非独享的)
        // TODO 也可以把声明队列也进行封装
        //如果设置了autoDelte =true  如果队列是durable =true 断开连接也不会删除队列
        instance.queueDeclare().getQueue()
//        instance.queueDeclare(UNAUTOACK_QUEUE, true, false, false, null);
        //从控制台中发送消息
        Scanner scanner = new Scanner(System.in);
        log.info("请输入消息：");
        /*while (scanner.hasNext()) {
            String s = scanner.nextLine();
            //这里还是使用默认交换机
            instance.basicPublish("", UNAUTOACK_QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN, s.getBytes(StandardCharsets.UTF_8));
        }*/
        int i=0;
       /* while (i<100) {
            i++;
            String s = "消息"+i;
            //这里还是使用默认交换机
            instance.basicPublish("", UNAUTOACK_QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN, s.getBytes(StandardCharsets.UTF_8));
        }*/
    }
}
