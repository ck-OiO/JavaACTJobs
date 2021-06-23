package com.github.ck_oio.javaatcjobs.seventjobs.homework09;

import com.github.ck_oio.javaatcjobs.seventjobs.homework09.selectorConfig.DataSourceSelector;
import com.github.ck_oio.javaatcjobs.seventjobs.homework09.selectorConfig.DataSourceType;
import com.github.ck_oio.javaatcjobs.seventjobs.mappers.OrderMapper;
import com.github.ck_oio.javaatcjobs.seventjobs.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Profile("homework09")
@Service
public class Homework09OrderService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 插入数据到默认数据库中,
     * @param order
     * @return 受影响的行数
     */
    public int insertWithoutAnno(Order order){
        return orderMapper.insert(order);
    }

    /**
     * 插入数据到写数据源. 也是默认数据源
     * @param order
     * @return 受影响的行数
     */
    @DataSourceSelector(DataSourceType.WRITER)
    public int insertWithWriteAnno(Order order){
        return orderMapper.insert(order);
    }

    /**
     * 从读数据源中获取数据
     * @return
     */
    @DataSourceSelector(DataSourceType.READER)
    public List<Order> selectAll(){
        return orderMapper.selectAll();
    }
}
