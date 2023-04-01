package fairyqin.homelove;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import fairyqin.homelove.until.ChannelInstance;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author Black_ghost
 * @title: RPCClient
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-30 12:32:30
 * @Description 客户端
 **/
@Slf4j
public class RPCClient {
    /*
     * 1. 发送请求到Rpc_queueh中
     * 2. 等待接收响应 */

    public static Channel instance;

    public static void main(String[] args) throws Exception {
        //获取连接发送请求
        log.info("客户端开启请求RPC服务.......");
        instance = ChannelInstance.getInstance();
        for (int i = 0; i < 32; i++) {
            String Task_correlationId = UUID.randomUUID().toString();
            //声明回调队列
            String replyqueuename = instance.queueDeclare().getQueue();
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                    .replyTo(replyqueuename)
                    .correlationId(Task_correlationId)
                    .build();
            //发送请求
            log.info("客户端发送的数据是：{}", i);
            instance.basicPublish("", RPCServer.RPC_QUEUE, properties, String.valueOf(i).getBytes(StandardCharsets.UTF_8));
            final CompletableFuture<String> completableFuture = new CompletableFuture<>();
            ;
            //接收响应,需要启动自动应答才行，不然会因为手动应答阻塞
            String consumerTag = instance.basicConsume(replyqueuename, true, (messageTag, delivery) -> {
                //使用completableFuture来实现异步并行计算，提高效率 ,这里只是演示，实际上不推荐使用CompletableFuture的无参数构造方法
                if (delivery.getProperties().getCorrelationId().equals(Task_correlationId)) {
                    boolean complete = completableFuture.complete(new String(delivery.getBody(), StandardCharsets.UTF_8));
                    log.info("队列的该阶段的任务是否转换为完成{}", complete);
                }
            }, consumerTag01 -> {
            });
            //接收处理的结果
            String s = completableFuture.get();
            log.info("{}队列接收到的rpc服务器处理的结果为{}", replyqueuename, s);
            //更具consumerTag（标识）取消队列的订阅
            instance.basicCancel(consumerTag);
        }
       /* //关闭channel
        instance.close();*/
    }
}
