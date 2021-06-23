package com.github.ck_oio.javaatcjobs.seventjobs.homework02;

import com.github.ck_oio.javaatcjobs.seventjobs.mappers.OrderMapper;
import com.github.ck_oio.javaatcjobs.seventjobs.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Profile("homework02")
@Service
public class Homework02OrderService {

@Autowired
private OrderMapper orderMapper;

    /**
     * 每个sql 语句用一个事务
     * @param orders
     */
    public void insertByIterator(List<Order> orders) {
        for (int i = 0; i < orders.size(); i++) {
            orderMapper.insert(orders.get(i));
        }
    }

    /**
     * 所有insert 语句共用一个事务
     * @param orders
     */
    @Transactional
    public void insertByIteratorInOneTx(List<Order> orders){
        for (int i = 0; i < orders.size(); i++) {
            orderMapper.insert(orders.get(i));
        }
    }

    /**
     * 批量插入
     *
     * mybatis 默认也是组装多条结果以multiple values 形式插入到数据库中.
     * 需要在mysql配置文件中[mysql]和[mysqld] 中设置max_allowed_pocket 值大于一次请求的数据量大小.
     * @param orders
     */
    public void insertByBatch(List<Order> orders) {
        orderMapper.batchInsert(orders);
    }

}
