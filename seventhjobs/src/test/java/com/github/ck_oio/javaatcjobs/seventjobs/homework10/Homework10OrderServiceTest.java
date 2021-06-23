package com.github.ck_oio.javaatcjobs.seventjobs.homework10;

import com.github.ck_oio.javaatcjobs.seventjobs.homeworkd02.TestTools;
import com.github.ck_oio.javaatcjobs.seventjobs.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.List;

import static com.github.ck_oio.javaatcjobs.seventjobs.homeworkd02.TestTools.createOrderWithId;
import static com.github.ck_oio.javaatcjobs.seventjobs.homeworkd02.TestTools.createOrderWithoutId;

@SpringBootTest
@Slf4j
public class Homework10OrderServiceTest {

    @Autowired
    private Homework10OrderService orderService;

    @Test
    public void testInsertWithId(){
        final Order order01UserId01 = createOrderWithId();
        order01UserId01.setOrderId(1L);
        order01UserId01.setUserId(1L);
        final Order order02UserId01 = createOrderWithId();
        order02UserId01.setOrderId(2L);
        order02UserId01.setUserId(1L);
        final Order order03UserId02 = createOrderWithId();
        order03UserId02.setOrderId(3L);
        order03UserId02.setUserId(2L);
        final Order order04UserId02 = createOrderWithId();
        order04UserId02.setOrderId(4L);
        order04UserId02.setUserId(2L);
        orderService.insertWithId(order01UserId01);
        orderService.insertWithId(order02UserId01);
        orderService.insertWithId(order03UserId02);
        orderService.insertWithId(order04UserId02);
    }

    @Test
    public void testInsertWithoutId(){
        orderService.insertWithoutId(createOrderWithoutId());
    }

    @Test
    public void testSelectAll(){
        final List<Order> orders = orderService.selectAll();
        orders.forEach(order ->log.info(order.toString()));
    }
}
