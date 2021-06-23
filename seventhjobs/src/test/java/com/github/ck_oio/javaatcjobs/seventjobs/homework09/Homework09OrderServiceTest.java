package com.github.ck_oio.javaatcjobs.seventjobs.homework09;

import com.github.ck_oio.javaatcjobs.seventjobs.homeworkd02.TestTools;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class Homework09OrderServiceTest {

    @Autowired
    private Homework09OrderService orderService;


    /**
     * 从输出日志可以看出使用了写数据库
     */
    @Test
    public void testInsertWithoutAnno(){
        log.info(Thread.currentThread().getStackTrace()[1].getMethodName());
        orderService.insertWithoutAnno(TestTools.createOrderWithoutId());
    }

    /**
     * 从输出日志可以看出使用了写数据库
     */
    @Test
    public void testInsertWithWriteAnno(){
        log.info(Thread.currentThread().getStackTrace()[1].getMethodName());
        orderService.insertWithWriteAnno(TestTools.createOrderWithoutId());
    }

    /**
     * 从输出日志可以看出使用了读数据库
     */
    @Test
    public void testSelectAll(){
        log.info(Thread.currentThread().getStackTrace()[1].getMethodName());
        orderService.selectAll();
    }
}
