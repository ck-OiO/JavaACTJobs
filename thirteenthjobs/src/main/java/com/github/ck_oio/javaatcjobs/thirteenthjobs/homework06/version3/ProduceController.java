package com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version3;

import com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version2.CmqBroker;
import com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version2.CmqMsg;
import com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version2.CmqProducer;
import com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version2.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pro/")
public class ProduceController {

    @Autowired
    private CmqBroker cmqBroker;

    @PostMapping("send")
    public Boolean send(Msg<Order> msg){
        final CmqProducer producer = cmqBroker.createProducer();
        producer.send(msg.getTopicId(), new CmqMsg<>(null, msg.getT()));
        return true;
    }
}
