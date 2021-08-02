package com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version3.client;

import com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version2.Order;
import com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version3.Constants;
import com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version3.Msg;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestVersion03 implements Constants {
    public static void main(String[] args) {
        final Producer producer = new Producer();
        producer.send(new Msg(topic, new Order(1L, "goods", 1.1)));
        producer.flush();

        final Consumer consumer = new Consumer();
        final Boolean subIsSuc = consumer.subscribe(name, Constants.topic);
        log.info("订阅是否成功:" + subIsSuc);
        final Msg msg = consumer.peek(name, topic);
        log.info("读取的消息为: " + msg);
        final Msg msg1 = consumer.offsetSeek(name, 0, topic);
        log.info("位置{}的消息:", 0, msg1);
        final Boolean commitIsSuc = consumer.commit(0, name);
        log.info("offset提交是否成功:{}" + commitIsSuc);
    }
}
