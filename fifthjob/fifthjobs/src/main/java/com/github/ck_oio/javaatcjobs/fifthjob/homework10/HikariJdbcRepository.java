package com.github.ck_oio.javaatcjobs.fifthjob.homework10;

import com.github.ck_oio.javaatcjobs.depschool.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

// Hikari连接池改进
@Repository
public class HikariJdbcRepository {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DataBaseUtils dbUtils;

    public Student insertStu(Student stu) throws SQLException {
        final String insertStr = "insert into student(name)values( ?)";
        final boolean autoCommit;
        try (Connection conn = dataSource.getConnection()) {
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement stat = conn.prepareStatement(insertStr,Statement.RETURN_GENERATED_KEYS)) {
                stat.setString(1, stu.getName());
                stat.executeUpdate();
                final ResultSet rsKey = stat.getGeneratedKeys();
                if (rsKey.next()) {
                    stu.setId(rsKey.getInt(1));
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            conn.commit();
            conn.setAutoCommit(autoCommit);
            return stu;
        }
    }

    public void deleteById(int id) throws SQLException {
        final String deleteStr = "delete from student where id=?";
        final boolean autoCommit;
        try (Connection conn = dataSource.getConnection()) {
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try(PreparedStatement stat = conn.prepareStatement(deleteStr)){
                stat.setInt(1, id);
                stat.executeUpdate();
            }catch (SQLException e){
                conn.rollback();
                throw e;
            }
            conn.commit();
            conn.setAutoCommit(autoCommit);
        }
    }

    public void updateById(Student stu) throws SQLException {
        final String updateStr = "update student set name=? where id = ? ";
        final boolean autoCommit;
        try (Connection conn = dataSource.getConnection()){
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try(PreparedStatement stat = conn.prepareStatement(updateStr)) {
                stat.setString(1, stu.getName());
                stat.setInt(2, stu.getId());
                stat.executeUpdate();
            }catch (SQLException e){
                conn.rollback();
                throw e;
            }
            conn.commit();
            conn.setAutoCommit(autoCommit);

        }
    }

    public List<Student> queryAll() throws SQLException {
        final String queryStr = "select id, name from student";
        List<Student> list = null;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stat = conn.prepareStatement(queryStr)) {
            try (ResultSet rs = stat.executeQuery()) {
                list = dbUtils.mapResultSetToStudent(rs);
            }
        }
        return list;
    }

    // 批处理插入
    public void batchUpdate(List<Student> stus) throws SQLException {
        final String insertStr = "insert into student(name)values( ?)";
        final boolean autoCommit;
        try (Connection conn = dbUtils.getConnection()) {
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement stat = conn.prepareStatement(insertStr)) {
                for (int i = 1; i <= stus.size(); i++) {
                    stat.setString(1, stus.get(i).getName());
                    stat.addBatch();
                    // 每200条执行一次, 避免占用内存过大
                    if(i % 200 ==0)
                        stat.executeUpdate();
                }
                // 执行剩余的
                stat.executeUpdate();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            conn.commit();
            conn.setAutoCommit(autoCommit);
        }
    }
}
