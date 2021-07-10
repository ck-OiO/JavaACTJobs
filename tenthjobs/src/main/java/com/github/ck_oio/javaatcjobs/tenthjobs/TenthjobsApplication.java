package com.github.ck_oio.javaatcjobs.tenthjobs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.JedisPool;

@SpringBootApplication
@EnableCaching
public class TenthjobsApplication {


    public static void main(String[] args) {
        SpringApplication.run(TenthjobsApplication.class, args);
    }

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool();
    }
}
