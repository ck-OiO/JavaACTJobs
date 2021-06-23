package com.github.ck_oio.javaatcjobs.seventjobs.homeworkd02;

import com.github.ck_oio.javaatcjobs.seventjobs.homework02.Homework02OrderService;
import com.github.ck_oio.javaatcjobs.seventjobs.model.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class Homework02OrderServiceTest {

    // 指定插入数据的条数
    static final int ORDER_SIZE = 100_0000;

    // 指定重复测试的次数
    private static final int count= 10;

    @Autowired
    private Homework02OrderService homework02OrderService;

    private static List<Order> orders;


    @BeforeAll
    public static void init(){
        orders = TestTools.createOrders(ORDER_SIZE);
    }

    @Test
    public void testInsertByIterator(){
        long[] times = new long[count];
        for (int i = 0; i < count; i++) {
            final LocalTime begin = LocalTime.now();
            homework02OrderService.insertByIterator(orders);
            final LocalTime end = LocalTime.now();
            times[i] = Duration.between(begin, end).toMillis();
        }
        long totalTime = 0;
        for (long t :
                times) {
            totalTime += t;
        }
        double avg = totalTime / ((double) count);
        System.out.printf("循环插入%d条数据平均时间(毫秒):%.2f. 每次的时间是(毫秒):%s%n", ORDER_SIZE, avg, Arrays.toString(times));
    }

    @Test
    public void testInsertByIteratorInOneTx(){
        long[] times = new long[count];
        for (int i = 0; i < count; i++) {
            final LocalTime begin = LocalTime.now();
            homework02OrderService.insertByIteratorInOneTx(orders);
            final LocalTime end = LocalTime.now();
            times[i] = Duration.between(begin, end).toMillis();
        }
        long totalTime = 0;
        for (long t :
                times) {
            totalTime += t;
        }
        double avg = totalTime / ((double) count);
        System.out.printf("共用一个事务的循环插入%d条数据平均时间(毫秒):%.2f. 每次的时间是(毫秒):%s%n", ORDER_SIZE, avg, Arrays.toString(times));
    }

    @Test
    public void testInsertByBatch(){
        long[] times = new long[count];
        for (int i = 0; i < count; i++) {
            final LocalTime begin = LocalTime.now();
            homework02OrderService.insertByBatch(orders);
            final LocalTime end = LocalTime.now();
            times[i] = Duration.between(begin, end).toMillis();
        }
        long totalTime = 0;
        for (long t :
                times) {
            totalTime += t;
        }
        double avg = totalTime / ((double) count);
        System.out.printf("批量插入%d条数据平均时间(毫秒):%.2f. 每次的时间是(毫秒):%s%n", ORDER_SIZE, avg, Arrays.toString(times));
    }

    @Autowired
    private JdbcTemplate rbsJdbcTemplate;


    // 执行一次测试, 清空表中数据, 避免表中数据过多, B+树超过三层会很慢.
    @AfterEach
    public void truncateAllOrders(){
        rbsJdbcTemplate.update("truncate table t_order");
    }
}
