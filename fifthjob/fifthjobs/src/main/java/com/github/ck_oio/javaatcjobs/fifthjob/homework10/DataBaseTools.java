package com.github.ck_oio.javaatcjobs.fifthjob.homework10;

import com.github.ck_oio.javaatcjobs.depschool.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseTools {
    /**
     *
     * @param rs student 表查询返回结果
     * @return
     * @throws SQLException
     */
    public static List<Student> mapResultSetToStudent(ResultSet rs) throws SQLException {
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
}
