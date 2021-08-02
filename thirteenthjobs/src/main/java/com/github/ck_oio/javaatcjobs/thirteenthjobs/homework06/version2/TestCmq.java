package com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version2;

import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicBoolean;

public class TestCmq {
    @SneakyThrows
    public static void main(String[] args) {
        String topicId = "cmq.test";
        final CmqBroker broker = new CmqBroker();
        broker.<Order>createTopic(topicId);

        String consumerName = "zhangsan";
        final CmqConsumer zhangsan = broker.createConsumer(consumerName);
        zhangsan.subscribe(topicId);
        AtomicBoolean flag = new AtomicBoolean(true);
        new Thread(() -> {
            while (flag.get()) {
                final CmqMsg<Order> msg = (CmqMsg<Order>) zhangsan.peek(1000);
                if (null != msg) {
                    System.out.println(msg.getBody());
                }
            }
            System.out.println("程序退出.");
        }).start();
        final CmqProducer producer = broker.createProducer();
        for (int i = 0; i < 1000; i++) {
            Order order = new Order(1000L + i, "price" + i, 6.51d);
            producer.send(topicId, new CmqMsg<Order>(null, order));
        }
        Thread.sleep(500);
        System.out.println("点击任何键，发送一条消息；点击q或e，退出程序。");
        while (true) {
            char c = (char) System.in.read();
            if (c > 20) {
                System.out.println(c);
                producer.send(topicId, new CmqMsg<Order>(null, new Order(100000L + c, c + "",  6.52d)));
            }

            if (c == 'q' || c == 'e') break;
        }

        flag.set(false);

    }
}
