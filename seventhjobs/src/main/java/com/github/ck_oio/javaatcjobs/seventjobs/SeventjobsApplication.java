package com.github.ck_oio.javaatcjobs.seventjobs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.github.ck_oio.javaatcjobs.seventjobs.mappers")
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class SeventjobsApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SeventjobsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
