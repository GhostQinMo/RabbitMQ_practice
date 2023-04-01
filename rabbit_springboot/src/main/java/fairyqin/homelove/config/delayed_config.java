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
