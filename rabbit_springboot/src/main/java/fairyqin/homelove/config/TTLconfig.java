package fairyqin.homelove.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Black_ghost
 * @title: TTLconfig
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-21 21:37:49
 * @Description 测试TTL的一个配置类，用于队列与交换机之间建立关系
 **/
@Configuration
public class TTLconfig {
    //所有的关于案例中的常量信息全部声明在此类中
    //队列TTL为10s
    public static final String QUEUEA = "QA";
    public static final Integer QATTL = 10000;
    //队列TTL为40s
    public static final String QUEUEB = "QB";
    public static final Integer QBTTL = 40000;
    //死信处理队列
    public static final String DEAD_QUEUE = "QD";
    //正常交换机
    public static final String NORMAL_EXCHANGE = "X";
    //死信交换机
    public static final String DEAD_EXCHANGE = "Y";
    //正常交换机与QUEUEA的routingkey
    public static final String QA_BINDING_NORNAL_EXCHANGE_ROUTINGKEY = "XA";
    //正常交换机与QUEUEB的routingkey
    public static final String QB_BINDING_NORNAL_EXCHANGE_ROUTINGKEY = "XB";
    //相关队列与死信交换机的绑定的routingkey
    public static final String DEAD_EXCHAGE_ROUTING_KEY = "YD";

    //创建两个交换机
    @Bean(value = "NORMAL_EXCHANGE")
    public DirectExchange NORMAL_EXCHANGE() {
        //这里有自动开箱操作
        return ExchangeBuilder.directExchange(NORMAL_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean(value = "DEAD_EXCHANGE")
    public DirectExchange DEAD_EXCHANGE() {
        //这里有自动开箱操作
        return ExchangeBuilder.directExchange(DEAD_EXCHANGE)
                .durable(true)
                .build();

    }

    //创建两个正常队列，并绑定死信交换机
    @Bean(value = "QUEUEA")
    public Queue QUEUEA() {
        //绑定死信交换机的参数
        HashMap<String, Object> arguments = new HashMap<>(3);
        //参数的key为固定值
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", DEAD_EXCHAGE_ROUTING_KEY);
        arguments.put("x-message-ttl", QATTL);
//        arguments.put("x-message-ttl", 2000);
        return QueueBuilder.durable(QUEUEA)
                .withArguments(arguments)
                .build();
    }

    @Bean(value = "QUEUEB")
    public Queue QUEUEB() {
        //绑定死信交换机的参数
        HashMap<String, Object> arguments = new HashMap<>(3);
        //参数的key为固定值
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", DEAD_EXCHAGE_ROUTING_KEY);
        arguments.put("x-message-ttl", QBTTL);
//        arguments.put("x-message-ttl", 5000);
        return QueueBuilder.durable(QUEUEB)
                .withArguments(arguments)
                .build();
    }

    //创建normal_exchange与正常队列的绑定时间
    @Bean("XA")
    public Binding XA(@Qualifier("NORMAL_EXCHANGE") DirectExchange NORMAL_EXCHANGE,
                      @Qualifier("QUEUEA") Queue QUEUEA) {
        Binding with = BindingBuilder.bind(QUEUEA).to(NORMAL_EXCHANGE).with(QA_BINDING_NORNAL_EXCHANGE_ROUTINGKEY);
        return with;
    }

    @Bean("XB")
    public Binding XB(@Qualifier("NORMAL_EXCHANGE") DirectExchange NORMAL_EXCHANGE,
                      @Qualifier("QUEUEB") Queue QUEUEA) {
        Binding with = BindingBuilder.bind(QUEUEA).to(NORMAL_EXCHANGE).with(QB_BINDING_NORNAL_EXCHANGE_ROUTINGKEY);
        return with;
    }

    //创建死信处理队列
    @Bean("DEAD_QUEUE")
    public Queue DEAD_QUEUE() {
        Queue build = QueueBuilder.durable(DEAD_QUEUE).build();
        return build;
    }

    //为死信队列绑定到死信交换机上
    @Bean("YD")
    public Binding YD(
            @Qualifier("DEAD_QUEUE") Queue queue,
            @Qualifier("DEAD_EXCHANGE") DirectExchange directExchange
    ) {
        Binding with = BindingBuilder.bind(queue).to(directExchange).with(DEAD_EXCHAGE_ROUTING_KEY);
        return with;
    }


    public static final String QUEUE_C = "QC";
    public static final String QC_binding_X_routingkey = "XC";

    //声明队列 C 死信交换机
    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> args = new HashMap<>(3);
    //声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_EXCHANGE);
    //声明当前队列的死信路由 key
        args.put("x-dead-letter-routing-key", DEAD_EXCHAGE_ROUTING_KEY);
    //没有声明 TTL 属性
        return QueueBuilder.durable(QUEUE_C).withArguments(args).build();
    }

    //声明队列 QC绑定 X 交换机
    @Bean
    public Binding queuecBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("NORMAL_EXCHANGE") DirectExchange NORMAL_EXCHANGE) {
        return BindingBuilder.bind(queueC).to(NORMAL_EXCHANGE).with(QC_binding_X_routingkey);
    }
}
