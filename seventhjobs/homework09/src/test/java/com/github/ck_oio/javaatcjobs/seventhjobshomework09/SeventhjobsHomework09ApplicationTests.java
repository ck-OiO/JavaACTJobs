package com.github.ck_oio.javaatcjobs.seventhjobshomework09;

import com.github.ck_oio.javaatcjobs.seventhjobshomework09.homework09.Order;
import com.github.ck_oio.javaatcjobs.seventhjobshomework09.homework09.dao.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class SeventhjobsHomework09ApplicationTests {

    static int userId = 1;
    static String address = "花果山水帘洞一号王坐";
    static BigDecimal totalValue = new BigDecimal("11.11");
    // 创建一个order对象.
    public static Order order = Order.builder().userId(userId).totalValue(totalValue).address(address).build();

    @Autowired
    private OrderRepository orderRepo;

    /*
    测试使用默认数据源情况
     */
    @Test
    public void testUseDefaultDataSource(){
        orderRepo.insert(order);
    }

    // 测试是否是指定的写数据源
    @Test
    public void testUseWriterDataSource(){
        orderRepo.insertOrders(orderToArr(List.of(order)));
    }

    // 测试是否是读数据源
    @Test
    public void testUseReaderDataSource(){
        final Order result = orderRepo.findById(1);
        Assertions.assertEquals(address, result.getAddress());
        Assertions.assertEquals(totalValue.doubleValue(), result.getTotalValue().doubleValue());
    }
    /**
     * 从Order中提取userId, totalValue, address 组成对象数组.
     *
     * @param orders
     * @return
     */
    public static List<Object[]> orderToArr(List<Order> orders) {
        final ArrayList<Object[]> objects = new ArrayList<>(orders.size());
        orders.forEach(o -> {
            objects.add(new Object[]{o.getUserId(), o.getTotalValue(), o.getAddress()});
        });
        return objects;
    }
}
