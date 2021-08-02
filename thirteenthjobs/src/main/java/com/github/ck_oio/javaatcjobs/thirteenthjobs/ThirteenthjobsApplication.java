package com.github.ck_oio.javaatcjobs.thirteenthjobs;

import com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version2.CmqBroker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ThirteenthjobsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThirteenthjobsApplication.class, args);
    }

    @Bean
    public CmqBroker cmqBroker(){
        return new CmqBroker();
    }

}
