package com.github.ck_oio.javaatcjobs.fifthjob;

import com.github.ck_oio.javaatcjobs.depschool.Student;
import com.github.ck_oio.javaatcjobs.fifthjob.homework10.AdvancedJdbcRepository;
import com.github.ck_oio.javaatcjobs.fifthjob.homework10.HikariJdbcRepository;
import com.github.ck_oio.javaatcjobs.fifthjob.homework10.RawJdbcRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.List;

@SpringBootTest
public class Homework10Test {
    @Autowired
    private RawJdbcRepository rawRepo;

    // 测试RawJdbcRepository
    @Test
    public void testRawJdbc() throws SQLException {
        Student str11 = Student.builder().name("str11").build();

        System.out.println("开始的数据:");
        List<Student> students = rawRepo.queryAll();
        System.out.println("\t" + students);

        str11 = rawRepo.insertStu(str11);
        System.out.println("插入str11后:");
        students = rawRepo.queryAll();
        System.out.println("\t" + students);


        str11.setName("11str");
        rawRepo.updateById(str11);
        System.out.println("修改str11为11str后:");
        students = rawRepo.queryAll();
        System.out.println("\t" + students);

        rawRepo.deleteById(str11.getId());
        System.out.println("删除str11后:");
        students = rawRepo.queryAll();
        System.out.println("\t" + students);

    }

    @Autowired
    private AdvancedJdbcRepository advaRepo;

    // 测试AdvancedJdbcRepository
    @Test
    public void testAdvancedJdbc() throws SQLException {
        Student str11 = Student.builder().name("str11").build();

        System.out.println("开始的数据:");
        List<Student> students = rawRepo.queryAll();
        System.out.println("\t" + students);

        str11 = advaRepo.insertStu(str11);
        System.out.println("插入str11后:");
        students = advaRepo.queryAll();
        System.out.println("\t" + students);


        str11.setName("11str");
        advaRepo.updateById(str11);
        System.out.println("修改str11为11str后:");
        students = advaRepo.queryAll();
        System.out.println("\t" + students);

        advaRepo.deleteById(str11.getId());
        System.out.println("删除str11后:");
        students = advaRepo.queryAll();
        System.out.println("\t" + students);
    }


    @Autowired
    private HikariJdbcRepository hikaRepo;

    // 测试AdvancedJdbcRepository
    @Test
    public void testHikariJdbc() throws SQLException {
        Student str11 = Student.builder().name("str11").build();

        System.out.println("开始的数据:");
        List<Student> students = rawRepo.queryAll();
        System.out.println("\t" + students);

        str11 = hikaRepo.insertStu(str11);
        System.out.println("插入str11后:");
        students = hikaRepo.queryAll();
        System.out.println("\t" + students);


        str11.setName("11str");
        hikaRepo.updateById(str11);
        System.out.println("修改str11为11str后:");
        students = hikaRepo.queryAll();
        System.out.println("\t" + students);

        advaRepo.deleteById(str11.getId());
        System.out.println("删除str11后:");
        students = hikaRepo.queryAll();
        System.out.println("\t" + students);
    }
}
