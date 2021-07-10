package com.github.ck_oio.javaatcjobs.tenthjobs.homework08;

import org.apache.catalina.webresources.StandardRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

// 对仓库库存的操作
@Service
public class InventoryService {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private RedisLock redisLock;

    /*
    根据仓库名称, 商品名称减去一定数量库存
    加成功返回-1, 仓储数量不够:仓储数量, 其他原因没有成功-2.
     */
    public long reduceInventory(String storeName, String goodsName, int count){
        long expiredTime = 500;
        long acquiredTimeout = 500;
        String lockKey = storeName +"." + goodsName;
        final String lock = redisLock.lock(lockKey, expiredTime, acquiredTimeout);
        if(lock == null)
            return Long.valueOf(-2);
        final Jedis jedis = jedisPool.getResource();
        try{
            final String total = jedis.hget(storeName, goodsName);
            final long longTotal = Long.parseLong(total);
            if(longTotal < count)
                return longTotal;
            jedis.hset(storeName, goodsName, (longTotal - count) + "");
            return Long.valueOf(-1);
        }finally {
            jedis.close();
            redisLock.unlock(lockKey, lock);
        }


    }
}
