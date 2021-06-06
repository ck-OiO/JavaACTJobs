package com.github.ck_oio.javaatcjobs.fifthjob.homework02;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
@Configuration 也会被@ComponentScan 扫描成为bean
 */
@Configuration
public class CreateBeanByJava {
    /*
    该bean默认懒加载为false, scope 为singleton, id为zhangsan, 根据类型自动注入
     */
    @Bean
    public Employee zhangsan(){
        return Employee.builder().name("zhangsan").id(11).build();
    }
}
