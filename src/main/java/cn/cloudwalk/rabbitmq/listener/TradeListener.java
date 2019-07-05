package cn.cloudwalk.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Service;

@Service
public class TradeListener implements ChannelAwareMessageListener {


    public void onMessage(Message message, Channel channel) throws Exception {

        System.out.println("received msg ==== " + new String(message.getBody(), "utf-8"));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
    }
}
