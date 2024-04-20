package fairyqin.homelove.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author Black_ghost
 * @title: delayed_config
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-22 16:17:53
 * @Description 基于插件实现延迟队列
 **/

@Configuration
public class delayed_config {
    //使用插件完成延迟操作
   /* * DELAYED_EXCHANGE 延迟交换机
     DELAYED_ROUTINGKEY routingkey
     * DELAYED_QUEUE  处理队列名字
     * */
    public  static  final String DELAYED_EXCHANGE="delayed_exchange";
    public  static  final String DELAYED_ROUTINGKEY="delayed_routingkey";
    public  static  final String DELAYED_QUEUE="delayed_queue";

    //声明延迟队列
    @Bean(value="delayed_queue")
    public Queue delayed_queue(){
        return new Queue(DELAYED_QUEUE);
    }
    //声明自定义交换机
    @Bean(value="delay_exchange")
    public CustomExchange delay_exchange(){
        //为什么自定义交换机是直接类型，因为这里使用的routingkey是一个固定值（delayed_routingkey）而不是路由
        HashMap<String,Object> arguments=new HashMap<>(3);
        //key的指是固定的
        arguments.put("x-delayed-type", "direct");
        //第二个参数自己添加的交换机的类型
        //为什么这里又是自定义类型，而参数设置又是直接交换机（Direct）,我的理解是带有延迟设置的直接交换机
        /*
         在这段代码中，虽然使用了CustomExchange自定义交换机类型，但实际上在定义CustomExchange时，指定了参数"x-delayed-type"为"direct"，
         这意味着这个自定义交换机其实是一个带有延迟设置的直接交换机。

        通过设置"x-delayed-type"参数为"direct"，我们可以创建一个直接交换机，但是该交换机具有延迟消息的特性。
        这样就实现了一个自定义类型的交换机，同时又能保留直接交换机的特性，使得消息能够按照指定的routing key进行路由，并且具有延迟消息的功能。
        因此，在这种情况下，自定义交换机类型是带有延迟设置的直接交换机。

        */
        CustomExchange customExchange   =new CustomExchange(DELAYED_EXCHANGE,
                "x-delayed-message",
                true,false,arguments);
        return  customExchange;
    }

    //创建绑定关系
    @Bean
    public Binding  Delayed_binding(
            @Qualifier("delay_exchange") CustomExchange customExchange,
            @Qualifier("delayed_queue") Queue queue
            ){
        return BindingBuilder.bind(queue).to(customExchange).with(DELAYED_ROUTINGKEY).noargs();
    }
}
