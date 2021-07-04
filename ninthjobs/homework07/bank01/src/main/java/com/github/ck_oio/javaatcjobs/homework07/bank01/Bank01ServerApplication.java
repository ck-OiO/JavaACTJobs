package com.github.ck_oio.javaatcjobs.homework07.bank01;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.RestController;

@ImportResource({"classpath:spring-dubbo.xml"})
@SpringBootApplication
@RestController
@MapperScan("com.github.ck_oio.javaatcjobs.homework07.bank01.mapper")
public class Bank01ServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(Bank01ServerApplication.class, args);
    }

}
