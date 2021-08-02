package com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version2;

public class CmqConsumer<T> {
    private String name;
    private CmqBroker broker;
    // 当前用户订阅的主题
    private CmqTopic<T> topic;

    /**
     * @param broker cmq的broker
     * @param name 用户名称
     */
    public CmqConsumer(CmqBroker broker, String name){
        this.broker = broker;
        this.name = name;
    }

    /**
     * 订阅名称为topicId 的主题
     *
     * @param topicId 主题名称
     * @throws Exception 没有注册成功
     */
    public void subscribe(String topicId) throws Exception {
        topic = broker.findTopic(topicId);
        if(null == topic){
            throw new Exception("Topic[" + topicId + "] doesn't exist");
        }
        topic.addCustomer(name);
    }

    /**
     * 返回队列头的消息
     * @param waitTime 单位ms
     * @return
     */
    public CmqMsg<T> peek(int waitTime){
        CmqMsg<T> msg = null;
        try {
            msg = topic.peek(name, waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return msg;
    }


    public CmqMsg<T> offsetSeek(String name, int offset){
        return topic.offsetSeek(name, offset);
    }

    public Boolean commit(String name, int offset){
        return topic.commit(name, offset);
    }
}
