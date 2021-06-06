package com.github.ck_oio.javaatcjobs.fifthjob;

import com.github.ck_oio.javaatcjobs.depschool.School;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Homework08Test {
    @Autowired
    private School school;

    @Test
    public void testAutoConfig(){
        System.out.println(school);
    }
}
