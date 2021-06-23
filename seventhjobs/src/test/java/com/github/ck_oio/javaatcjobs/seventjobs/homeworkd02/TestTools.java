package com.github.ck_oio.javaatcjobs.seventjobs.homeworkd02;

import com.github.ck_oio.javaatcjobs.seventjobs.model.Order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestTools {
    static long userId = 1;
    static String address = "zhonghuarenmingongheguo-beijingshi-chaoyangqu";
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
     * 创建有主键的order
     * @return
     */
    public static Order createOrderWithId(){
        return Order.builder().orderId(1L).userId(userId).totalValue(totalValue).address(address).build();
    }

    /**
     * 没有主键的order
     * @return
     */
    public static Order createOrderWithoutId(){
        return Order.builder().userId(userId).totalValue(totalValue).address(address).build();
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
}
