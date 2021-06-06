package com.github.ck_oio.javaatcjobs.fifthjob.homework02;


public class EmployeeFactoryByStatic {

    public static Employee createEmployee(){
        return Employee.builder().name("wangwu32").id(32).build();
    }
}
