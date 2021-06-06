package com.github.ck_oio.javaatcjobs.fifthjob;

import com.github.ck_oio.javaatcjobs.fifthjob.homework02.CreateBeanByJava;
import com.github.ck_oio.javaatcjobs.fifthjob.homework02.Employee;
import com.github.ck_oio.javaatcjobs.fifthjob.homework02.Manager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Homework02Test {
    @Autowired
    @Qualifier("employee")
    private Employee employee;
    @Autowired
    @Qualifier("zhangsan")
    Employee zhangsan;
    @Autowired
    @Qualifier("lisi21")
    Employee lisi21;
    @Autowired
    @Qualifier("lisi22")
    Employee lisi22;
    @Autowired
    @Qualifier("lisi23")
    Employee lisi23;
    @Autowired
    @Qualifier("lisi24")
    Employee lisi24;
    @Autowired
    @Qualifier("lisi25")
    Employee lisi25;
    @Autowired
    @Qualifier("wangwu31")
    private Employee wangwu31;
    @Autowired
    @Qualifier("wangwu32")
    private Employee wangwu32;
    @Autowired
    @Qualifier("zhaoliu")
    private Employee zhaoliu;
    @Autowired
    @Qualifier("tianqi")
    private Employee tianqi;
    @Autowired
    @Qualifier("manager")
    private Manager manager;
    @Autowired
    private CreateBeanByJava createBeanByJava;

    @Test
    public void testHomework02(){
        System.out.println(employee);
        System.out.println(zhangsan);
        System.out.println(lisi21);
        System.out.println(lisi22);
        System.out.println(lisi23);
        System.out.println(lisi24);
        System.out.println(lisi25);
        System.out.println(wangwu31);
        System.out.println(wangwu32);
        System.out.println(zhaoliu);
        System.out.println(tianqi);
        System.out.println(manager);
        System.out.println(createBeanByJava);
    }
}
