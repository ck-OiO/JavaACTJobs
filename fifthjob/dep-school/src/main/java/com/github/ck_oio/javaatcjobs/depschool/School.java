package com.github.ck_oio.javaatcjobs.depschool;

import lombok.Data;

import java.util.List;


@Data
public class School{
   private List<Klass> klasses;

    public void ding(){
        int stuCount = 0;
        for (Klass tmp :
                klasses) {
            stuCount += tmp.getStudents().size();
        }
        System.out.println("School have " + stuCount + " students");
        
    }
    
}
