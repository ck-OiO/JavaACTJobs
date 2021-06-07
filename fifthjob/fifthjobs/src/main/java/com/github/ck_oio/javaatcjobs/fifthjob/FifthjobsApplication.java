package com.github.ck_oio.javaatcjobs.fifthjob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import javax.sql.DataSource;

@SpringBootApplication
@ImportResource("application-beans.xml")
public class FifthjobsApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(FifthjobsApplication.class, args);
    }


    @Autowired
    DataSource dataSource;
    @Override
    public void run(String... args) throws Exception {
        System.out.println(dataSource);

    }
}
