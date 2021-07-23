package com.github.ck_oio.javaatcjobs.eleventhjobs.homework09;

import com.github.ck_oio.javaatcjobs.eleventhjobs.homework08.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class PubSubReduceInventoryMsg {
    // 仓库名称.商品名称:增减的数量
    public static final String INVENTORY_CHANNEL_FORMET = "%s.%s";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private JedisPool jedisPool;

    public void publishReduceInventoryMsg(String storeName, String goodsName, int count) {
        redisTemplate.execute((RedisConnection conn) -> {
            // 指定消息发送频道
            conn.publish(String.format(INVENTORY_CHANNEL_FORMET, storeName, goodsName)
                            .getBytes(StandardCharsets.UTF_8)
                    // 指定消息
                    , (count + "").getBytes(StandardCharsets.UTF_8));
            return null;
        });
    }

    /*
    传入频道名称, 得到结果, 得到购买数量. 扣减库存
     */
    public void subscirbeReduceInventoryMsg(String storeName, String goodsName) {
        redisTemplate.execute((RedisConnection conn) -> {

            conn.subscribe((msg, channel) -> {
                // 判断是否是指定频道
//                if (new String(channel, StandardCharsets.UTF_8).equals(
//                        String.format(INVENTORY_CHANNEL_FORMET, storeName, goodsName))) {
                    try (Jedis jedis = jedisPool.getResource()) {
                        // 获取购买数量
                        int count = Integer.parseInt(new String(msg.getBody(), StandardCharsets.UTF_8));

                        log.info("购买前商品总数量{}", jedis.hget(storeName, goodsName));
                        inventoryService.reduceInventory(storeName, goodsName, count);
                        log.info("异步执行购买{}个商品后, 剩余的商品数量:{}",count, jedis.hget(storeName, goodsName));
                    }
//                }
                // 指定订阅频道
            }, String.format(INVENTORY_CHANNEL_FORMET, storeName, goodsName).getBytes(StandardCharsets.UTF_8));

            return null;
        });
    }

}
