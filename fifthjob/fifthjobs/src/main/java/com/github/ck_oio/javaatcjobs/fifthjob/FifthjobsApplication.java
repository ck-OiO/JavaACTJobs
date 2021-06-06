package com.github.ck_oio.javaatcjobs.fifthjob;

import com.github.ck_oio.javaatcjobs.depschool.Student;
import com.github.ck_oio.javaatcjobs.fifthjob.homework10.DataBaseUtils;
import com.github.ck_oio.javaatcjobs.fifthjob.homework10.RawJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
