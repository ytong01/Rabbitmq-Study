package cn.cloudwalk.rabbitmq.listener;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Producer {

    private String queue = "multiple_queue";

    @Autowired
    private RabbitTemplate amqpTemplate;

    public void send(final String queue, Object object) {

        amqpTemplate.execute(new ChannelCallback<String>() {
            public String doInRabbit(Channel channel) throws Exception {
                channel.exchangeDeclare(queue, ExchangeTypes.TOPIC, true, false, null);
                channel.queueDeclare(queue, true, false, false, null);
                channel.queueBind(queue, queue, queue);

                int i = 100;
                while (i-- > 0) {
                    channel.basicPublish(queue, queue, true, null, ("阿森纳 ======== " + i).getBytes());
                }
                return null;
            }
        });
//        amqpTemplate.convertAndSend(queue, object);
    }

}
