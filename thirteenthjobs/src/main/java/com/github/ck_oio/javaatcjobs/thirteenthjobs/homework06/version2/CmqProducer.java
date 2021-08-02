package com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version2;

public class CmqProducer {
    private CmqBroker broker;

    public CmqProducer(CmqBroker broker){
        this.broker = broker;
    }

    public <T> void send(String topicId, CmqMsg<T> msg){
        final CmqTopic<T> topic = broker.findTopic(topicId);
        if(null == topic){
            throw new RuntimeException(topicId + "不存在");
        }
        topic.send(msg);
    }

}
