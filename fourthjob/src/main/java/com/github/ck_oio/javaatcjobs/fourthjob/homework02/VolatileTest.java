package com.github.ck_oio.javaatcjobs.fourthjob.homework02;

import java.util.concurrent.locks.LockSupport;

import static com.github.ck_oio.javaatcjobs.fourthjob.homework02.Tools.testMethod;

public class VolatileTest {
    public static void main(String[] args) {
        testMethod(VolatileTest::mth);
//        test10000(VolatileTest::mth);
    }

    private static volatile int result = -1;
    private static int mth() {
        new Thread(()->{
            result = Tools.sum();
        }).start();
        while(result == -1){
            LockSupport.parkNanos(10000000);
        }
        return result;
    }
}
