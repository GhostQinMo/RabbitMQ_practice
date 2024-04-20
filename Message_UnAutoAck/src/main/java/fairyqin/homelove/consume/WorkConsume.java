package fairyqin.homelove.consume;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import fairyqin.homelove.util.ChannelInstance;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

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
    private final static String UNAUTOACK_QUEUE = "UnAutoAck_queue";

    public static void main(String[] args) throws Exception {
        Channel instance = ChannelInstance.getInstance();
        //声明队列 ，如果队列不存在会创建队列  这里可以不用声明队列了 注意:这里需要设置一个队列中的是非exclusive的(非独享的)
        // TODO 也可以把声明队列也进行封装
//        instance.queueDeclare(UNAUTOACK_QUEUE, true, false, true, null);
        //获取队列中的任务进行消费
        /**
         *   consumerTag表示与消费者关联的标签
         *   message 表示传递的消息
         */
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            //假设消息处理需要一定的时间
            try {
                //通过添加虚拟机参数 设置可以多实例运行
                TimeUnit.SECONDS.sleep(1);
//                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("consume接收到的任务是>>" + new String(message.getBody()));
            //手动应答消息处理完成  通过message(传递的消息)可以获取的消息的标签
            instance.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };
        CancelCallback cancelCallback = (consumeTag) -> {
            log.error("标签为{}的任务未被正确消费", consumeTag);
        };
        log.info("等待被分配消息");
        //关闭自动应答，改为手动应答
        Boolean auto = false;
        //参数的取值1是为不公平分发，其实其他参数也可以   0表示不受限制
//        instance.basicQos(256);
        instance.basicConsume(UNAUTOACK_QUEUE, auto, deliverCallback, cancelCallback);
        //通过回调方法，实现异步操作，java中只能使用多线程实现异步操作
        log.info("处理一些操作");

    }
}
