package com.github.ck_oio.javaatcjobs.fifthjob;

import com.github.ck_oio.javaatcjobs.depschool.Student;
import com.github.ck_oio.javaatcjobs.fifthjob.homework10.AdvancedJdbcStudentRepository;
import com.github.ck_oio.javaatcjobs.fifthjob.homework10.HikariJdbcStudentRepository;
import com.github.ck_oio.javaatcjobs.fifthjob.homework10.JdbcStudentRepository;
import com.github.ck_oio.javaatcjobs.fifthjob.homework10.RawJdbcStudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class Homework10Test {

    private Random rand = new Random();

    @Autowired
    private RawJdbcStudentRepository rawRepo;
    @Autowired
    private AdvancedJdbcStudentRepository advRepo;
    @Autowired
    private HikariJdbcStudentRepository hikRepo;

    @Test
    public void testStudentRepository() throws SQLException, IOException {
        // 测试RawJdbcRepository
        testStuJdbc(rawRepo);
        // 测试AdvancedJdbcStudentRepository
        testStuJdbc(advRepo);
        // 测试HikariJdbcStudentRepository
        testStuJdbc(hikRepo);

    }
    private void testStuJdbc(JdbcStudentRepository stuRepo) throws SQLException, IOException {
        Student str11 = Student.builder().name("str11").build();

        System.out.println("开始的数据:");
        List<Student> students = stuRepo.queryAll();
        System.out.println("\t" + students);

        str11 = stuRepo.insertStu(str11);
        System.out.println("插入str11后:");
        students = stuRepo.queryAll();
        System.out.println("\t" + students);


        str11.setName("11str");
        stuRepo.updateById(str11);
        System.out.println("修改str11为11str后:");
        students = stuRepo.queryAll();
        System.out.println("\t" + students);

        stuRepo.deleteById(str11.getId());
        System.out.println("删除str11后:");
        students = stuRepo.queryAll();
        System.out.println("\t" + students);

        System.out.println("批量插入前记录数:"+ stuRepo.getAllCount());
        List<String> nameList = new ArrayList<>();
        int stuCount = 201;
        for (int i = 0; i < stuCount; i++) {
            nameList.add(generateStr(3));
        }
        List<Student> stuList = new ArrayList<>(nameList.size());
        nameList.forEach(name->stuList.add(
                Student.builder().name(name).build()
        ));
        stuRepo.batchInsert(stuList);
        System.out.println("批量插入" + stuCount + "条数据后, 总数为:" + stuRepo.getAllCount());


    }

    // 生成指定长度小写字母字符串
    private String generateStr(int strLen){
        final int[] arr = rand.ints(strLen, 'a', 'z' + 1).toArray();
        StringBuilder sb = new StringBuilder(arr.length);
        for (int i :
                arr) {
            sb.append((char)i);
        }
        return sb.toString();
    }


}
