package com.github.ck_oio.javaatcjobs.eleventhjobs;

import com.github.ck_oio.javaatcjobs.eleventhjobs.homework08.InventoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.Random;

public class InventoryServiceTest extends BaseTest{

    @Autowired
    private InventoryService inventoryService;


    @Test
    public void testReduceInventory() {
        for (int i = 0; i < 100; i++) {
            final int reduceCount = new Random().nextInt(10);
            inventoryService.reduceInventory(stockName, goodsName, reduceCount);
            try (Jedis jedis = jedisPool.getResource()) {
                totalCount -= reduceCount;
                Assertions.assertEquals(totalCount, Long.parseLong(jedis.hget(stockName, goodsName)));
            }
        }
    }



}
