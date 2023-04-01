package fairyqin.homelove;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import fairyqin.homelove.until.ChannelInstance;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author Black_ghost
 * @title: RPCServer
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-28 12:31:46
 * @Description rpc服务提供者
 **/
@Slf4j
public class RPCServer {
    public static final String RPC_QUEUE="rpc_queue";
        public static Integer getfibonacci(Integer n){
            if(n==0) {
                return 1;
            }
            if(n==1) {
                return 1;
            }
            return  getfibonacci(n-1)+getfibonacci(n-2);
        }
    public static void main(String[] args) throws Exception{
        /*1. 从Rpc_queue中接收任务
        * 2. 处理任务（使用斐波那契数列来模拟处理过程）
        * 3. 根据客户端提供的回调队列（replyTo）返回响应
        * */
        //1.从Rpc_queue接收任务
        log.info("从Rpc_queue接收任务");
        Channel instance = ChannelInstance.getInstance();
        //清空测试的rep_queue
        instance.queuePurge(RPC_QUEUE);
        //设置不公平分发
        instance.basicQos(1);
        instance.queueDeclare(RPC_QUEUE,true,false,false,null);
        //2.处理任务+返回响应数据
        DeliverCallback deliverCallback=(consumerTag,delivery)->{
            log.info("正在处理任务之中");
            //获取要求的斐波那契数列的数
            String s = new String(delivery.getBody(),StandardCharsets.UTF_8);
            log.info("服务器接收的数据是：{}",s);
            int n = Integer.parseInt(s);
            Integer getfibonacci = getfibonacci(n);
            String response=String.valueOf(getfibonacci);
            //返回响应数据 使用默认交换机（直接类型）
            //服务端需要设置AMQP的14个属性中的CorrelationId，不然客户端那边接收消息的时候会报空指针异常
            AMQP.BasicProperties basicProperties=new AMQP.BasicProperties().builder()
                    .correlationId(delivery.getProperties().getCorrelationId())
                    .build();
            instance.basicPublish("",delivery.getProperties().getReplyTo(),
                    basicProperties,response.getBytes(StandardCharsets.UTF_8));
            //手动应答,并且不批量应答
            instance.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            log.info("处理了回调队列为{}的响应",delivery.getProperties().getReplyTo());
        };
        //改为手动应答
        boolean isAutoAck=false;
        instance.basicConsume(RPC_QUEUE,isAutoAck,deliverCallback,consumerTag -> {});
    }
}
