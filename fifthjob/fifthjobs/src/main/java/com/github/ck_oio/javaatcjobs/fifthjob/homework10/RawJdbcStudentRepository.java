package com.github.ck_oio.javaatcjobs.fifthjob.homework10;

import com.github.ck_oio.javaatcjobs.depschool.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/*
使用JDBC的Statement, 实现增删改查
 */
@Repository
public class RawJdbcStudentRepository implements JdbcStudentRepository {

    @Autowired
    private DataBaseUtils dbUtils;

    @Override
    public Connection getConnection() throws SQLException {
        return dbUtils.getConnection();
    }

    public Student insertStu(Student stu) throws SQLException {
        final String insertStr = "insert into student(name)values('" + stu.getName() + "')";
        try (Connection conn = getConnection();
             Statement stat = conn.createStatement()) {
            stat.executeUpdate(insertStr, Statement.RETURN_GENERATED_KEYS);
            try (ResultSet rs = stat.getGeneratedKeys()) {
                if (rs.next())
                    stu.setId(rs.getInt(1));
            }

        }
        return stu;
    }

    public void deleteById(int id) throws SQLException {
        final String deleteStr = "delete from student where id=" + id;
        try (Connection conn = getConnection();
             Statement stat = conn.createStatement()) {
            stat.executeUpdate(deleteStr);
        }
    }

    public void updateById(Student stu) throws SQLException {
        final String updateStr = "update student set name='" + stu.getName() + "'  where id=" + stu.getId();
        try (Connection conn = getConnection();
             Statement stat = conn.createStatement()) {
            stat.executeUpdate(updateStr);
        }
    }

    public List<Student> queryAll() throws SQLException {
        final String queryStr = "select id, name from student";
        List<Student> list = null;
        try (Connection conn = getConnection();
             Statement stat = conn.createStatement()) {
            try (ResultSet rs = stat.executeQuery(queryStr)) {
                list = DataBaseTools.mapResultSetToStudent(rs);
            }
        }
        return list;
    }

    @Override
    public void batchInsert(List<Student> stus) throws SQLException {
        try(var conn = getConnection();
            Statement stat = conn.createStatement()) {
            String insertStr = null;
            for (int i = 1; i <= stus.size(); i++) {
                insertStr = "insert into student(name)values('" + stus.get(i - 1).getName() + "')";
                stat.addBatch(insertStr);
                // 避免内存使用过大
                if(i / 200 == 0)
                    stat.executeBatch();
            }
            // 执行剩下的insert
            stat.executeBatch();
        }

    }
}