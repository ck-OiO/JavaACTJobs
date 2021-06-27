package com.github.ck_oio.javaatcjobs.eighthjobs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(
        exclude = DataSourceAutoConfiguration.class
)
@MapperScan("com.github.ck_oio.javaatcjobs.eighthjobs.mappers")
@EnableTransactionManagement
public class EighthjobsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EighthjobsApplication.class, args);
    }

}
