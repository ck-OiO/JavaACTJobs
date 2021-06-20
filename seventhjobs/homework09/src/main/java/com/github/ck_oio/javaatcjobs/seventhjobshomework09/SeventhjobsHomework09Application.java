package com.github.ck_oio.javaatcjobs.seventhjobshomework09;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
})
@EnableAspectJAutoProxy
@Slf4j
public class SeventhjobsHomework09Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SeventhjobsHomework09Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
