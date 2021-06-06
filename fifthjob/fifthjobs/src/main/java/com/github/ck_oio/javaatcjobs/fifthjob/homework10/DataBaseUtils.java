package com.github.ck_oio.javaatcjobs.fifthjob.homework10;

import com.github.ck_oio.javaatcjobs.depschool.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// 导入application.yml 中数据库的配置
@EnableConfigurationProperties(DataBaseInfo.class)
@Component
public class DataBaseUtils {
    @Autowired
    private DataBaseInfo info;

    public Connection getConnection() throws SQLException {
            return DriverManager.getConnection(info.getUrl(), info.getUsername(), info.getPassword());
    }

    public List<Student> mapResultSetToStudent(ResultSet rs) throws SQLException {
        List<Student> list = new ArrayList();
        while (rs.next()) {
            list.add(
                    Student.builder()
                            .id(rs.getInt("id"))
                            .name(rs.getString("name"))
                            .build());
        }
        return list;
    }
    /**
     * 删除所有数据
     * @throws SQLException
     */
    @PreDestroy
    public void deleteData() throws SQLException {
        getConnection().createStatement().executeUpdate("drop table student");
    }

}
