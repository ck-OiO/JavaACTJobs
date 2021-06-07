package com.github.ck_oio.javaatcjobs.fifthjob.homework10;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;

/*
使用事务，PrepareStatement 方式，批处理方式，改进RawJdbcRepository操作
 */
@Repository
public class AdvancedJdbcStudentRepository implements JdbcStudentRepository {

    @Autowired
    private DataBaseUtils dbUtils;

    @Override
    public Connection getConnection() throws SQLException {
        return dbUtils.getConnection();
    }
}
