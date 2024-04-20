package fairyqin.homelove.Consume;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import fairyqin.homelove.util.ChannelInstance;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Black_ghost
 * @title: WorkConsume
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-19 21:01:17
 * @Description 工作任务处理线程
 **/

@Slf4j
public class WorkConsume {
    private final static String QUEUE_NAME = "work_queue";

    /*这里测试工作队列模式，使用了idea中的多实例启动方式，所以这里只写了一个消费者的带代码*/
    public static void main(String[] args) throws Exception {
        Channel instance = ChannelInstance.getInstance();
        //声明队列 ，如果队列不存在会创建队列  这里可以不用声明队列了 注意:这里需要设置一个队列中的是非exclusive的(非独享的)
        // TODO 也可以把声明队列也进行封装
        //声明队列之前判断是该队列已经存在
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
        //获取队列中的任务进行消费
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("consume1接收到的任务是>>" + new String(message.getBody()));
//            System.out.println("consume2接收到的任务是>>" + new String(message.getBody()));
        };
        CancelCallback cancelCallback = (consumeTag) -> {
            log.error("标签为{}的任务未被正确消费", consumeTag);
        };
        log.info("等待被分配消息");
        //这里设置自动应答 容器丢失消息
        instance.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
