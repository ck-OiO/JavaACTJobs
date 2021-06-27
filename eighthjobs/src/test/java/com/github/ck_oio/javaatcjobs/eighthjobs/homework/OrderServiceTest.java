package com.github.ck_oio.javaatcjobs.eighthjobs.homework;

import com.github.ck_oio.javaatcjobs.eighthjobs.mappers.OrderMapper;
import com.github.ck_oio.javaatcjobs.eighthjobs.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
public class OrderServiceTest {

    @Autowired
    private OrderMapper orderMapper;

    @BeforeEach
    public void beforeEach(){
        log.info("执行前:{}", orderMapper.selectAll());
    }

    @AfterEach
    public void afterEach(){
        log.info("执行后:{}", orderMapper.selectAll());
    }

    @Test
    public void testInsertThenId() {
        final Order order = TestTools.createOrderWithoutId();
        orderMapper.insertThenId(order);
        log.info("自动生成orderId:{}", order.getOrderId());

    }

    @Test
    public void insertWithId() {
        log.info("在每个数据库的每个表中插入一条数据:");
        final List<Order> orders = createOrders(true);
        for (Order order :
                orders) {
            orderMapper.insert(order);
        }
    }

    /**
     * @param hasId 为true时: 生成orderId在0-15 时, userId=0. orderId 在16-31 时, userId = 1 的32个order对象. 为false 时. 生成对象orderId为null.
     * @return
     */
    private List<Order> createOrders(boolean hasId) {
        List<Order> orders = new ArrayList<>(36);
        for (int i = 0; i < 36; i++) {
            final Order order = TestTools.createOrderWithoutId();

            if (hasId)
                order.setOrderId((long) i);
            // 插入orderId
            if (i / 16 == 0)
                order.setUserId(((long) 0));
            else
                order.setUserId(((long) 1));

            orders.add(order);
        }
        return orders;
    }

    @Test
    public void updateAllOrders() {
        log.info("将所有数据userId 为0的orderId改为2, userId为1的改为3");
        final List<Order> orders = orderMapper.selectAll();
        for (Order order :
                orders) {
            if (order.getUserId() == 0)
                order.setUserId(2L);
            else
                order.setUserId(3L);
            orderMapper.updateByPrimaryKey(order);
        }
    }

    @Test
    public void testDeleteOrder() {
        log.info("根据数据的orderId 删除数据");
        final List<Order> orders = orderMapper.selectAll();
        for (var order :
                orders) {
            orderMapper.deleteByPrimaryKey(order.getOrderId());
        }
    }

}
