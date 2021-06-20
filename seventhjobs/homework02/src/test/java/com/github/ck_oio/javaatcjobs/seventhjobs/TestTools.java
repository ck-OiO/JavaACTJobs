package com.github.ck_oio.javaatcjobs.seventhjobs;

import com.github.ck_oio.javaatcjobs.seventhjobs.homework02.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TestTools {
    static int userId = 1;
    static String address = "花果山水帘洞一号王坐";
    static BigDecimal totalValue = new BigDecimal("11.11");

    /**
     * 创建指定数量的Order对象
     * @param size
     * @return
     */
    public static List<Order> createOrders(int size) {
        List<Order> orders = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            orders.add(Order.builder().userId(userId).totalValue(totalValue).address(address).build());
        }
        return orders;
    }

    /**
     * 创建多值multiple values insert 语句.
     * @param size 要插入记录的数量
     * @return
     */
    public static String createMultiValueInsertStat(long size){
        final String insertOrder = "insert into t_order(f_user_id, f_total_value, f_address) values";
        String insertValues = "(" + userId + "," + totalValue + ", \'" + address + "\')";
        StringBuilder sb = new StringBuilder();
        sb.append(insertOrder);
        for (long i = 0; i < size; i++) {
            sb.append(insertValues);
            if(i != size - 1){
                sb.append(",");
            } else {
                sb.append(";");
            }
        }
        return sb.toString();
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
