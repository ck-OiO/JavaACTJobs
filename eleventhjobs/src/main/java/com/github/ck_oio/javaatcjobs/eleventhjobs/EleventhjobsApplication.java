package com.github.ck_oio.javaatcjobs.eleventhjobs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.JedisPool;

@SpringBootApplication
@EnableCaching
public class EleventhjobsApplication {


    public static void main(String[] args) {
        SpringApplication.run(EleventhjobsApplication.class, args);
    }

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool();
    }
}
