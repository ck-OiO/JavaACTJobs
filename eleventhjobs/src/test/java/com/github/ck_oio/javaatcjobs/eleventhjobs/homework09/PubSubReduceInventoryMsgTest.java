package com.github.ck_oio.javaatcjobs.eleventhjobs.homework09;

import com.github.ck_oio.javaatcjobs.eleventhjobs.BaseTest;
import com.github.ck_oio.javaatcjobs.eleventhjobs.homework08.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

@Slf4j
public class PubSubReduceInventoryMsgTest extends BaseTest {

    @Autowired
    private PubSubReduceInventoryMsg pubSub;

    @Autowired
    private InventoryService inventoryService;

    @Test
    public void testpublishInventoryOpsMsg() {
        final int count = new Random().nextInt(100);
        pubSub.publishReduceInventoryMsg(stockName, goodsName, count);
        log.info("购买{}个商品的消息已发送", count);
    }

//    @Test
//    public void testsubscirbeInventoryOpsMsg() {
//        log.info("进入第二个测试");
//        pubSub.subscirbeReduceInventoryMsg((msg, channel) -> {
//            try (Jedis jedis = jedisPool.getResource()) {
//                final String msgStr = new String(msg.getBody(), StandardCharsets.UTF_8);
//                final String[] msgArr = msgStr.split(".");
//                int count = Integer.parseInt(msgArr[1]);
//                final String[] goodsFullName = msgArr[0].split(":");
//                log.info("购买前商品总数量{}", jedis.hget(goodsFullName[0], goodsFullName[1]));
//                inventoryService.reduceInventory(goodsFullName[0], goodsFullName[1], count);
////                Assertions.assertEquals(totalCount - count, jedis.hget(goodsFullName[0], goodsFullName[1]));
//                log.info("异步执行完后, 剩余的商品数量:{}", jedis.hget(goodsFullName[0], goodsFullName[1]));
//
//            }
//        }, stockName, goodsName);
//    }
}
