package com.github.ck_oio.javaatcjobs.fifthjob.homework02;

public class EmployeeFactory {
    public Employee createEmployee(){
        return Employee.builder().name("wangwu31").id(31).build();
    }
}
