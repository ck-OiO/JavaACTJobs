package com.github.ck_oio.javaatcjobs.tenthjobs.homework08;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.UUID;

@Service
@Slf4j
public class RedisLock {

    @Autowired
    private JedisPool jedisPool;

    private static SetParams params = SetParams.setParams().nx();

    /**
     * 获取redis 中的锁
     *
     * @param lockKey 锁的键名
     * @param expiredTime 锁的过期时间 单位:毫秒
     * @param acquriedTimeout 获取锁的超时时间 单位:毫秒
     * @return 锁的value
     */
    public String lock(String lockKey, long expiredTime, long acquriedTimeout){
        final Jedis jedis = jedisPool.getResource();
        params = params.px(expiredTime);
        final long begin = System.currentTimeMillis();
        while(true){
            try{
                String lockValue = UUID.randomUUID().toString();
                final String result = jedis.set(lockKey, lockValue, params);
                if("OK".equals(result))
                    return lockValue;
                if(System.currentTimeMillis() > begin + acquriedTimeout)
                    return null;
            } finally {
                jedis.close();
            }
        }

    }

    /**
     * 为指定的锁解锁
     *
     * @param lockKey 锁名
     * @param lockValue 锁值
     * @return 是否解锁成功
     */
    public Boolean unlock(String lockKey, String lockValue){
        final Jedis jedis = jedisPool.getResource();
            String unlockScript =
                    "if redis.call('get',KEYS[1]) == ARGV[1] then" +
                            "   return redis.call('del',KEYS[1]) " +
                            "else" +
                            "   return 0 " +
                            "end";
        try{
            final Object result = jedis.eval(unlockScript, Collections.singletonList(lockKey), Collections.singletonList(lockValue));
            if("1".equals(result.toString())){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }finally {
            jedis.close();
        }
    }

}
