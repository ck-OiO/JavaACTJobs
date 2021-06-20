package com.github.ck_oio.javaatcjobs.seventhjobshomework10;

import com.github.ck_oio.javaatcjobs.seventhjobshomework10.homework10.dao.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
class SeventhjobsHomework10ApplicationTests {

    @Autowired
    private OrderRepository orderRepository;

    static int userId = 1;
    static String address = "花果山水帘洞一号王坐";
    static BigDecimal totalValue = new BigDecimal("11.11");
    // 创建一个order对象.
    public static Order orderWithId1 = Order.builder().userId(userId).totalValue(totalValue).address(address).build();

    // 会自动根据雪花算法生成主键
    @Test
    public void testInsertWithoutId() {
        Order orderWithoutId = Order.builder().userId(userId).totalValue(totalValue).address(address).build();
        orderRepository.insertWithoutId(orderWithoutId);
    }

    @Test
    public void testInsertWithId() {
        Order orderWithId1 = Order.builder().orderId(1).userId(userId).totalValue(totalValue).address(address).build();
        Order orderWithId2 = Order.builder().orderId(2).userId(userId).totalValue(totalValue).address(address).build();
        Order orderWithId3 = Order.builder().orderId(3).userId(userId).totalValue(totalValue).address(address).build();
        Order orderWithId4 = Order.builder().orderId(4).userId(userId).totalValue(totalValue).address(address).build();
        orderRepository.insertWithId(orderWithId1);
        orderRepository.insertWithId(orderWithId2);
        orderRepository.insertWithId(orderWithId3);
        orderRepository.insertWithId(orderWithId4);
    }

    @Test
    public void testFindAll(){
        final List<Order> orders = orderRepository.findAll();
        System.out.println(orders);
    }

    @Autowired
    private JdbcTemplate rbsJdbcTemplate;


    //  需要时, 清空t_order
    @Test
    public void truncateAllOrders(){
        rbsJdbcTemplate.update("truncate table t_order");
    }
}
