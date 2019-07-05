package cn.cloudwalk;

import cn.cloudwalk.rabbitmq.listener.MultipleConsumer;
import cn.cloudwalk.rabbitmq.listener.Producer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class MultiPelConsumerTest extends RabbitmqTest {
    private String queue = "multiple_queue";
    @Autowired
    private Producer producer;
    @Autowired
    private MultipleConsumer consumer;

    @Test
    public void test() throws InterruptedException, IOException {
        final CountDownLatch latch = new CountDownLatch(1);
        new Thread(new Runnable() {
            public void run() {
                producer.send(queue, null);
                latch.countDown();
            }
        }).start();

        latch.await();
        for (int i = 0; i < 2; i++) {
            new Thread(new Runnable() {
                public void run() {
                    consumer.consume();
                }
            }).start();
        }

        System.in.read();
    }
}
