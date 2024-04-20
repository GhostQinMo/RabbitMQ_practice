package fairyqin.homelove.Publisher;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;
import fairyqin.homelove.util.ChannelInstance;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author Black_ghost
 * @title: Confirm_Publisher
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-20 11:19:55
 * @Description 发布确认发布者
 **/
@Slf4j
public class Confirm_Publisher {
    public static final String CONFIRM_QUEUE = "CONFIRM_QUEUE";
    public static final Integer MESSAGECOUNT = 1000;

    //单个发布确认模式1000条消息所有的时间为1988ms
    //批量发布确认模式1000条消息所有的时间为78ms
    // 异步发布确认模式1000条消息所有的时间为52ms
    public static void main(String[] args) throws Exception {
        //单个发布确认
        SinglePublish_Confirm();
        //批量发布确认模式
        MultiPublish_Confirm();
        //异步发布确认模式
        PublishConfirmModeAsync();
    }


    //单个发布确认 (同步)
    public static void SinglePublish_Confirm() throws Exception {
        //获取channel
        Channel channel = ChannelInstance.getInstance();
        //只要有了信道（channel）就可以开启发布确认模式
        AMQP.Confirm.SelectOk selectOk = channel.confirmSelect();
        //声明持久化队列
        boolean isDurable = true;
        channel.queueDeclare(CONFIRM_QUEUE, isDurable, false, true, null);
        //发布消息
        log.info("发布者发布消息：\n");
        long start = System.currentTimeMillis();
        for (int i = 0; i < MESSAGECOUNT; i++) {
            //演示同步效果
            //TimeUnit.SECONDS.sleep(1);
            String s = "发布者发布的消息" + i;
            //还是使用默认的交换机
            //设置消息持久化
            channel.basicPublish("", CONFIRM_QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN, s.getBytes(StandardCharsets.UTF_8));
            //waitForConfirms()必须在开启发布确认的channel中使用
            boolean b = channel.waitForConfirms();
            if (b) {
                log.info("{} 发布确认成功", s);
            }
        }
        long end = System.currentTimeMillis();
        log.info("单个发布确认模式1000条消息所有的时间为{}ms", end - start);
    }

    //批量发布确认模式  (同步)
    public static void MultiPublish_Confirm() throws Exception {
        //获取channel
        Channel channel = ChannelInstance.getInstance();
        //只要有了信道（channel）就可以开启发布确认模式
        AMQP.Confirm.SelectOk selectOk = channel.confirmSelect();
        //声明持久化队列
        boolean isDurable = true;
        channel.queueDeclare(CONFIRM_QUEUE, isDurable, false, true, null);
        //发布消息
        log.info("发布者发布消息：\n");
        long start = System.currentTimeMillis();
        //批量提交的数目
        int max_multi = 100;
        //记录当前未确认的消息数目
        int count = 0;
        for (int i = 0; i < MESSAGECOUNT; i++) {
            //计数
            count++;
            String s = "发布者发布的消息" + i;
            //还是使用默认的交换机
            //设置消息持久化
            channel.basicPublish("", CONFIRM_QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN, s.getBytes(StandardCharsets.UTF_8));
            //waitForConfirms()必须在开启发布确认的channel中使用
            if (count == max_multi) {
                boolean b = channel.waitForConfirms();
                if (b) {
                    log.info("{} 发布确认成功", s);
                }
                count = 0;
            }
        }
        if (count >= 0) {
            //如果最后的数目不是100，也需要返回确认信息
            channel.waitForConfirms();
        }
        long end = System.currentTimeMillis();
        log.info("批量发布确认模式1000条消息所有的时间为{}ms", end - start);
    }

    //异步发布确认模式
    public static void PublishConfirmModeAsync()throws Exception{
        //创建记录为被正确确认的消息的容器
        /*
        * 1. 支持并发访问，线程安全
        * 2. 无消息重复被记录
        **/
        ConcurrentSkipListMap<Long ,String> container=new ConcurrentSkipListMap<>();
        //获取channel
        Channel channel =ChannelInstance.getInstance();
        //开启发布确认
        AMQP.Confirm.SelectOk selectOk = channel.confirmSelect();
        System.out.println(selectOk.protocolMethodName());
        //声明队列，并且持久化队列
        boolean isdurable=true;
        channel.queueDeclare(CONFIRM_QUEUE,isdurable,false,true,null);
        //为信道（channel）添加监听器（listener）
        //成功接收接受消息的确认回调函数
        /**
         * 确认收到消息的一个回调
         * 1.消息序列号
         * 2.true 可以确认小于等于当前序列号的消息
         * false 确认当前序列号消息
         */
        ConfirmCallback ackCallback=(deliveryTag,multiple)->{
            //这里由于是异步操作，所以后面的消息可能会提前调用这个回调方法，
            //multiple 为true表示批量处理，因为消息是按照序列从0开始发送的，开始的时候讲过，会为每一个消息添加一个序列号（从0开始）
            if (multiple){
                //返回的是一个序列号严格消息指定值的原映射的视图（map），在这个视图上的更改会影响原视图，反之亦然
                //返回此地图部分的视图，其密钥严格小于 toKey 。
                ConcurrentNavigableMap<Long, String> longStringConcurrentNavigableMap =
                        container.headMap(deliveryTag);
                //从未确认的map中清除这些消息（这个map是线程安全的）
                longStringConcurrentNavigableMap.clear();
                log.info(">>>>>>>>>小于等于{}号的消息被确认发布",deliveryTag);
            } else{
                //检查是否有这个键（因为是异步操作，很可能这些键以及被清除了）
                boolean b = container.containsKey(deliveryTag);
                if (b){
                    container.remove(deliveryTag);
                }

                log.info(">>>>>>>>>>>>>消息id为{}的消息被确认发布",deliveryTag);
            }
        };
        //消息为被正确确认的回调函数
        ConfirmCallback nackCallback =(deliveryTag,multiple)->{
            log.error("消息序列号为{} 的消息未被正确发布确认",deliveryTag);
        };
        /**
         * 添加一个异步确认的监听器
         * 1.确认收到消息的回调
         * 2.未收到消息的回调
         */
        channel.addConfirmListener(ackCallback,nackCallback);
        //开始时间
        long start = System.currentTimeMillis();
        //发送消息
        for (int i=0;i<MESSAGECOUNT;i++){
            //代发的消息
            String message="message to you from fairyqin"+i;
            //发送之前得到代发消息的序列号
            long nextPublishSeqNo = channel.getNextPublishSeqNo();
            //先保存在发送
            container.put(nextPublishSeqNo,message);
            //还是使用默认交换机,使用队列名代替routingkey
            //设置消息持久化
            channel.basicPublish("",CONFIRM_QUEUE,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes(StandardCharsets.UTF_8));
            log.info("序列号为{}的消息发送，消息体为{}",nextPublishSeqNo,message);
        }
        long end = System.currentTimeMillis();
        log.info("》》》》》》》》》》》》》》》》》{}条消息发送成功，用时为{}ms",MESSAGECOUNT,end-start);
        log.info("》》》》》》》》》》》》》》》》》》》由于是异步操作，所以这里可以执行一些其他的操作");
    }
}
