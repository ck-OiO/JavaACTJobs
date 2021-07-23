package com.github.ck_oio.javaatcjobs.eleventhjobs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.JedisPool;

@SpringBootTest
public class BaseTest {
    @Autowired
    public JedisPool jedisPool;


    public final String stockName = "stock01:computer";
    public final String goodsName = "AppleComputer";
    public long totalCount = 100000;

    @BeforeEach
    public void initCount() {
        jedisPool.getResource().hset(stockName, goodsName, totalCount + "");
    }  //

    @Test
    public void forRun(){

    }
//    @AfterEach
//    public void clearGoodsCount() {
//        try (Jedis jedis = jedisPool.getResource()) {
//            jedis.hdel(stockName, goodsName);
//        }
//    }

}
