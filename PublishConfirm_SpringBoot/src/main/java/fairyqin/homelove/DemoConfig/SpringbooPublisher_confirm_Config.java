package fairyqin.homelove.DemoConfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Black_ghost
 * @title: SpringbooPublisher_confirm_Config
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-23 11:00:13
 * @Description springboot实现发布确认模式，保证消息不丢失
 **/
@Configuration
@Slf4j
public class SpringbooPublisher_confirm_Config {
    //直接类型的交换机名称
    public static final String CONFIRM_EXCHANGE = "confirm_exchange";
    //确认队列名称
    public static final String CONFIRM_QUEUE = "confirm_queue";
    //routingkey
    public static final String PUBLISHER_CONFIRM_ROUTINGKEY = "key1";

    //注入交换机
    @Bean(value = "confirm_exchange")
    public DirectExchange confirm_exchange() {
        //只需要根据交换机的名字来绑定就可以了，不需要注入back_exchange
        ExchangeBuilder exchangeBuilder = ExchangeBuilder.directExchange(CONFIRM_EXCHANGE)
                .durable(true)
        .withArgument("alternate-exchange", BACKUP_EXCHANGE);
        Exchange build = exchangeBuilder.build();
//        build.addArgument("alternate-exchange", BACKUP_EXCHANGE);  //这样也可以
        return (DirectExchange) build;
    }

    //注入队列
    @Bean("confirm_queue")
    public Queue confirm_queue() {
        Queue build = QueueBuilder.durable(CONFIRM_QUEUE).build();
        return build;
    }

    //绑定
    @Bean
    public Binding CreateBinding(
            @Qualifier("confirm_exchange") DirectExchange confirmexchange,
            @Qualifier("confirm_queue") Queue confirmqueue
    ) {
        Binding with = BindingBuilder.bind(confirmqueue).to(confirmexchange).with(PUBLISHER_CONFIRM_ROUTINGKEY);
        return with;
    }

    //注入交换机接收消息的回调方法
    //这里只是为测试没有处理分离出去
    @Bean("confirmCallback")
    public RabbitTemplate.ConfirmCallback inputConfirmCallBack() {
        /**交换机确认回调方法
         1.发消息交换机接收到了回调
         1.1 correlationData保存回调消息的ID及相关信息*
         1.2交换机收到消息ack = true
         1.3 causenull
         2.发消息交换机接收失败了回调
         2.1 correlationData保存回调消息的ID及相关信息
         2.2交换机收到消息ack = false
         2.3 cause失败的原因
         */
//        void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause);
        return (correlationData, ack, cause) -> {
            //注意了这里需要判断correlationData是否为空指针问题，如果忘记了，调用correlationData.getId()时会有warning
            //参数二为错误信息
            String id = correlationData != null ? correlationData.getId() : null;
            if (ack) {
                log.info("交换机接收到消息,消息的id为：{}", id);
                if (id == "100") {
                    log.info("返回消息的内容为：{}", new String(correlationData.getReturnedMessage().getBody()).toString());
                }
            } else {
                log.info("还未接收到id为{}的消息,为接收到的原因是 {}", id, cause);
            }
        };
    }




    //备份交换机案例
    /*
    * 1. 需要把前面消息退回案例中使用的confirm_exchange删除
    * 2. 需要为confirm_exchange绑定*/
    public  static  final String BACKUP_EXCHANGE="backup_exchange";
    public  static  final String BACKUP_QUEUE="backup_queue";
    public  static  final String WARNING_QUEUE="warning_queue";

    //创建交换机和绑定关系
    @Bean("backup_exchange")
    public FanoutExchange backup_exchange(){
        return new FanoutExchange(BACKUP_EXCHANGE);
    }
    @Bean("back_queue")
    public Queue back_queue(){
        return new Queue(BACKUP_QUEUE);
    }
    @Bean("warning_queue")
    public Queue warning_queue(){
        return new Queue(WARNING_QUEUE);
    }

    @Bean
    public Binding backup_queue_to_backup_exchange(
            @Qualifier("back_queue") Queue back_queue,
            @Qualifier("backup_exchange") FanoutExchange backup_exchange
    ){
        //这里为什么没有with了,因为这里的交换机是fanout(扇出)，订阅模式
        return BindingBuilder.bind(back_queue).to(backup_exchange);

    }
    @Bean
    public Binding warning_queue_to_backup_exchange(
            @Qualifier("warning_queue") Queue warn_queue,
            @Qualifier("backup_exchange") FanoutExchange backup_exchange
    ){
        //这里为什么没有with了,因为这里的交换机是fanout(扇出)，订阅模式
        return BindingBuilder.bind(warn_queue).to(backup_exchange);
    }

    //声明优先级队列
    public final  static   String  PRIORITYQUEUE="priorityqueue";
    @Bean
    public Queue createPriorityQueue(){
        Queue build = QueueBuilder.durable(PRIORITYQUEUE).withArgument("x-max-priority", 10).build();
        return build;
    }

}
