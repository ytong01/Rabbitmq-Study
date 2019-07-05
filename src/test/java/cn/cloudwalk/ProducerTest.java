package cn.cloudwalk;

import cn.cloudwalk.rabbitmq.listener.Producer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProducerTest extends RabbitmqTest {

    @Autowired
    private Producer producer;

    @Test
    public void test() {

        producer.send("queue_trade_repay", "阿森納");
    }
}
