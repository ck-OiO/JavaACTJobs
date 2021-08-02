package com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version3;

import com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version2.CmqBroker;
import com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version2.CmqConsumer;
import com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version2.CmqMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("consume/")
public class ConsumController {

    @Autowired
    private CmqBroker cmqBroker;

    @GetMapping("subscribe")
    public boolean subscribe(String name, String topicId){
        final CmqConsumer consumer = cmqBroker.createConsumer(name);
        try {
            consumer.subscribe(topicId);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @GetMapping("peek")
    public Msg peek(String name, String topicId){
        final CmqConsumer consumer = cmqBroker.createConsumer(name);
        final CmqMsg cmqMsg = consumer.peek(500);
        return  new Msg(topicId, cmqMsg.getBody());
    }
    @GetMapping("offsetseek")
    public Msg offsetSeek(String name, int offset, String topicId) {
        final CmqConsumer consumer = cmqBroker.createConsumer(name);
        return new Msg(topicId, consumer.offsetSeek(name, offset).getBody());
    }

    @GetMapping("commit")
    public Boolean commit(int offset, String name) {
        final CmqConsumer consumer = cmqBroker.createConsumer(name);
        return consumer.commit(name, offset);
    }

}
