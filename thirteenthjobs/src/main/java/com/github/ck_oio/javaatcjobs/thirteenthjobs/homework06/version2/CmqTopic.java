package com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  一个CmqTopic 只能有一个生产者, 可以有多个消费者(一个消费者只能存在于一个线程中).
 */
public class CmqTopic<T> {
    private int iniCapacaty;
    private CmqMsg<T>[] msgs;
    /**
     * 队列头索引
     */
    private AtomicInteger header;
    private Map<String, Integer> consumerIndexes;
    /**
     * 每次增加容量的比例
     */
    private float increRatio;

    /**
     * 默认初始容量为1000, 数组满时增加比例为0.75
     */
    public CmqTopic(){
        this(1000, 0.75f);
    }

    /**
     * @param iniCapacaty 初始化容量
     * @param increRatio 数组满时增加的比例
     */
    public CmqTopic(int iniCapacaty, float increRatio){
        this.iniCapacaty = iniCapacaty;
        this.increRatio = increRatio;
        msgs = new CmqMsg[iniCapacaty];
        header = new AtomicInteger(0);
        consumerIndexes = new HashMap<>();
    }

    public boolean send(CmqMsg msg){
        try {
            if (header.get() == iniCapacaty) {
                iniCapacaty =(int) (msgs.length * (increRatio + 1));
                msgs = Arrays.copyOf(msgs, iniCapacaty);
            }
            msgs[header.getAndIncrement()] = msg;
            return true;
        } catch (Throwable t){
            t.printStackTrace();
            return false;
        }
    }

    public void addCustomer(String name){
        consumerIndexes.put(name, 0);
    }
    /**
     *  为name 用户返回队列头元素. 人路过队列没有新元素, 等待 waitTime ms.
     * @param name 用户名称
     * @param waitTime 单位:ms
     * @return
     * @throws InterruptedException
     * @throws RuntimeException 用户没有订阅该主题
     */
    public CmqMsg<T> peek(String name, int waitTime) throws InterruptedException {
        final Integer index = consumerIndexes.get(name);
        if(null ==index)
            throw new RuntimeException(name + " 没有订阅该主题");
        if(index >= header.get()){
            TimeUnit.MILLISECONDS.sleep(waitTime);
            if(index >= header.get())
                return null;
        }
        consumerIndexes.put(name, index + 1);
        return msgs[index];
    }

    public CmqMsg<T> offsetSeek(String name, int offset){
        final Integer index = consumerIndexes.get(name);
        if(null ==index)
            throw new RuntimeException(name + " 没有订阅该主题");
        if(index >= header.get()){
                return null;
        }
        return msgs[offset];
    }

    public Boolean commit(String name, int offset){
        final Integer index = consumerIndexes.get(name);
        if(null ==index)
            throw new RuntimeException(name + " 没有订阅该主题");
        if(index >= header.get()){
            return false;
        }
        return true;
    }

}
