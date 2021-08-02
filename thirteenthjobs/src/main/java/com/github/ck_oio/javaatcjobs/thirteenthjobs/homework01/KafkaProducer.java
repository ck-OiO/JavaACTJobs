package com.github.ck_oio.javaatcjobs.thirteenthjobs.homework01;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTem;

    @RequestMapping("/sendmsg")
    public String sendMsg(String topic, String msg){
        kafkaTem.send(topic, msg);
        log.info("生产者在{} 发送了消息{}", topic, msg);
        return "suc";
    }
}
