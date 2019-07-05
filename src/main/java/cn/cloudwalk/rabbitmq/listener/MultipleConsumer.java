package cn.cloudwalk.rabbitmq.listener;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MultipleConsumer {

    private String queue = "multiple_queue";
    private int batchSize = 512;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void consume() {
        while (true) {
            rabbitTemplate.execute(new ChannelCallback<String>() {
                public String doInRabbit(Channel channel) throws Exception {

                    AMQP.Queue.DeclareOk ok = channel.queueDeclare(queue, true, false, false, null);
                    if (ok.getMessageCount() == 0) {
                        return null;
                    }

                    channel.basicQos(batchSize);
                    QueueingConsumer consumer = new QueueingConsumer(channel);
                    channel.basicConsume(queue, false, "test consumer", consumer);
                    long messageId = -1;
                    int i = batchSize;
                    int dealCount = 0;
                    while (i-- > 0) {
                        QueueingConsumer.Delivery delivery = consumer.nextDelivery(batchSize);
                        if (delivery == null) {
                            break;
                        }
                        dealCount++;
                        System.out.println(new String(delivery.getBody(), "utf-8"));
                        messageId = delivery.getEnvelope().getDeliveryTag();
                        if (dealCount % 5 == 0) {
                            channel.basicAck(messageId, true);
                            messageId = -1;
                        }
                    }
                    if (messageId > 0) {
                        channel.basicAck(messageId, true);
                    }
                    return null;
                }
            });
        }
    }
}
