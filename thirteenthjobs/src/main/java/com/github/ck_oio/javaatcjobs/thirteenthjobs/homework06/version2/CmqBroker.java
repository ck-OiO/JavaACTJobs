package com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version2;

import java.util.concurrent.ConcurrentHashMap;

public class CmqBroker {
    private ConcurrentHashMap<String, CmqTopic> topics = new ConcurrentHashMap<>();

    public <T> CmqTopic<T> findTopic(String topicId) {
        return topics.get(topicId);
    }

    public <R> void createTopic(String topicId){
        CmqTopic<R> topic = new CmqTopic();
        topics.put(topicId, topic);
    }
    public CmqProducer createProducer(){
        return new CmqProducer(this);
    }
    public CmqConsumer createConsumer(String consumerName){
        return new CmqConsumer(this, consumerName);
    }
}
