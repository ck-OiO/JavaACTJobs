package com.github.ck_oio.javaatcjobs.tenthjobs;

import com.github.ck_oio.javaatcjobs.tenthjobs.homework08.InventoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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
