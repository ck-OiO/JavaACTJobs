package com.github.ck_oio.javaatcjobs.thirteenthjobs.homework01;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {
    @Autowired
    private KafkaTemplate kafkaTem;

    @KafkaListener(topics = "test32")
    public void consume(ConsumerRecord<String, String> rec){
        log.info("kafka 消费者接受消息的topic:{}. 接受的消息为:{}", rec.topic(), rec.value());
    }

}
