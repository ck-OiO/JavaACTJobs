package com.github.ck_oio.javaatcjobs.seventjobs.homework10;

import com.github.ck_oio.javaatcjobs.seventjobs.mappers.OrderMapper;
import com.github.ck_oio.javaatcjobs.seventjobs.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Profile("homework10")
@Service
public class Homework10OrderService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 插入有主键的order
     * @param order
     * @return 受影响的行数
     */
    public int insertWithId(Order order){
        return orderMapper.insert(order);
    }

    /**
     * 插入没有主键的order, shardingSphere 使用snowflake 产生主键
     * @param order
     * @return
     */
    public int insertWithoutId(Order order){
        return orderMapper.insert(order);
    }

    /**
     * shardingShpere 会查询所有关联的数据源获取数据.
     * @return
     */
    public List<Order> selectAll(){
        return orderMapper.selectAll();
    }

}
