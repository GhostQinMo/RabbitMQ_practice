package fairyqin.homlove;

import com.rabbitmq.client.Channel;
import fairyqin.homlove.until.ChannelInstance;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

/**
 * @author Black_ghost
 * @title: Quorum_declare
 * @projectName RabbitMQ_practice
 * @description :616  An unchanging God  Qin_Love
 * @vesion 1.0.0
 * @CreateDate 2023-04-01 17:22:32
 * @Description 仲裁队列的声明
 **/
@Slf4j
public class Quorum_declare {
    //这里的编译前异常可以通过构造方法抛出去
    final Channel instance = ChannelInstance.getInstance();

    public Quorum_declare() throws Exception {
    }

    //这里不是在集群中，仅仅是声明一个仲裁队列而且，一个节点没有分区容忍性
    public static void main(String[] args) throws Exception {
        try (Channel channel = new Quorum_declare().instance) {
            log.info("声明仲裁队列 。。。");
//            x-queue-type=quorum 设置仲裁队列
            channel.queueDeclare("quorum_queue", true, false, false,
                    Collections.singletonMap("x-queue-type", "quorum"));
        }
    }
}
