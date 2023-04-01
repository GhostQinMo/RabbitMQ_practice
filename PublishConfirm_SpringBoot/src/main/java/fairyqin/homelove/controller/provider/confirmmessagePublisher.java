package fairyqin.homelove.controller.provider;

import fairyqin.homelove.DemoConfig.SpringbooPublisher_confirm_Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

/**
 * @author Black_ghost
 * @title: confirmmessagePublisher
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-03-23 12:04:57
 * @Description 消息发布者
 **/

@RestController
@Slf4j
@RequestMapping("/confirm")
public class confirmmessagePublisher implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    RabbitTemplate.ConfirmCallback confirmeCallback;

    //需要设置交换机确认返回回调接口
    @PostConstruct
    public void init() {
//        rabbitTemplate.setConfirmCallback(confirmeCallback);
        rabbitTemplate.setConfirmCallback(this);
        //开启交换机的Mandatory,有义务将未路由到队列的消息回退给消费者
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(this);
    }

    //重写RabbitTemplate内部接口ConfirmCallback和ReturnCallback
    //交换机确认接收信息的回调方法
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        Assert.notNull(correlationData, "correlaionData为null");
        if (ack) {
            log.info("交换机接收到的消息id为：{}，绑定的routingkey为{}，消息的内容为:{}", correlationData.getId(),
                    correlationData.getReturnedMessage().getMessageProperties().getReceivedRoutingKey()
                    , new String(correlationData.getReturnedMessage().getBody(), StandardCharsets.UTF_8));
        } else {
            log.info("交换机没有接收到消息id为{}的消息，导致的原因是{}", correlationData.getId(), cause);
        }
    }

    //交换机未成功发送消息给路由队列，将消息退回给发送者的调用的回调函数，只有消息不可达目的是才会调用这个回调函数
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("交换机退回消息：{} 未将消息id为:{} 消息内容为：{} 发送到routingkey为：{}的队列",
                exchange,
                message.getMessageProperties().getMessageId(),
                new String(message.getBody(), StandardCharsets.UTF_8),
                routingKey);
        log.info("replyCode为{}", replyCode);
        log.info("replyText为{}", replyText);
    }


    //localhost:8081/confirm/resendMessage/welcometofairyhom,message01
    //消息提供者
    @RequestMapping("/resendMessage/{message}")
    public void publusherConfirm(@PathVariable("message") String message) {
        //这里的correlationData就是在交换机调用ConfirmCallback函数式接口使用的数据，如果这里不提供的话，交换机确认回调函数返回就没有数据
        //这个id不是消息的id，而是 交换机验证是使用的id
        CorrelationData correlationData1 = new CorrelationData("100");
        String returnkey1 = "routingkey为key1";
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setReceivedRoutingKey("key1");
        messageProperties.setMessageId("ID1");
        messageProperties.setPriority(5);//设置消息的优先级为5
        Message message1 = new Message(returnkey1.getBytes(StandardCharsets.UTF_8), messageProperties);
        correlationData1.setReturnedMessage(message1);
        //给key1发送消息
        rabbitTemplate.convertAndSend(SpringbooPublisher_confirm_Config.CONFIRM_EXCHANGE
                , SpringbooPublisher_confirm_Config.PUBLISHER_CONFIRM_ROUTINGKEY, message, correlationData1);
        log.info("发送消息给交换机：{}，消息体为：{}，routingkey为：{}", SpringbooPublisher_confirm_Config.CONFIRM_EXCHANGE, message
                , SpringbooPublisher_confirm_Config.PUBLISHER_CONFIRM_ROUTINGKEY);

        //给不存在routingkey=key2发送消息
        CorrelationData correlationData2 = new CorrelationData("200");
        String returnkey2 = "routingkey为key2";

        MessageProperties messageProperties2 = new MessageProperties();
        messageProperties2.setReceivedRoutingKey("key2");
        messageProperties2.setMessageId("ID2");
        Message message2 = new Message(returnkey2.getBytes(StandardCharsets.UTF_8), messageProperties2);
        correlationData2.setReturnedMessage(message2);

        rabbitTemplate.convertAndSend(SpringbooPublisher_confirm_Config.CONFIRM_EXCHANGE, "key2", message, correlationData2);
        log.info("发送消息给交换机：{}，消息体为：{},routingkey为：{}", SpringbooPublisher_confirm_Config.CONFIRM_EXCHANGE, message, "key2");
    }
}
