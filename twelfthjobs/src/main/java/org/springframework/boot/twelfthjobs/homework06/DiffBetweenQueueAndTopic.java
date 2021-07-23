package org.springframework.boot.twelfthjobs.homework06;


import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.*;
import java.util.concurrent.atomic.AtomicInteger;

/*
生产者总共生产50条消息. 每个消费者注册一个监听器.
使用topic类型destination 时, 每个监听器接收50条消息.
使用queue类型destination 时, 每个监听器接收10条消息.

topic类型destination, MQ会将每个消息发送给<strong>每个</strong>消费者
queue类型destination, MQ会将每个消息发送给<strong>某个</strong>消费者.
 */
public class DiffBetweenQueueAndTopic {
    public static void main(String[] args) {
        // 定义topic 类型Destination
        Destination topicDest = new ActiveMQTopic("test.topic");
        // 定义queue 类型Destination
        Destination queueDest = new ActiveMQQueue("test.queue");

        testDestination(topicDest);
        testDestination(queueDest);
    }

    public static void testDestination(Destination destination) {
        try {
            // 创建连接和会话
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
            ActiveMQConnection conn = (ActiveMQConnection) factory.createConnection();
            conn.start();
            // 自动确认, 不需要另外处理
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // 创建5个消费者, 并绑定监听者
            for (int i = 0; i < 5; i++) {
                final AtomicInteger count = new AtomicInteger(0);
                int num = i;
                final String consumerName = String.format("consumer%02d", num);
                MessageConsumer consumer = session.createConsumer(destination);
                MessageListener listener = new MessageListener() {
                    public void onMessage(Message message) {
                        // 打印所有的消息内容
                        System.out.println(consumerName + " => receive " +
                                count.incrementAndGet() + "th from " + destination.toString() + ": " + message);
                    }
                };
                // 绑定消息监听器
                consumer.setMessageListener(listener);
            }


            // 创建生产者，生产50个消息
            MessageProducer producer = session.createProducer(destination);
            int index = 0;
            while (index++ < 50) {
                TextMessage message = session.createTextMessage(index + " message.");
                producer.send(message);
            }

            Thread.sleep(2000);
            session.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
