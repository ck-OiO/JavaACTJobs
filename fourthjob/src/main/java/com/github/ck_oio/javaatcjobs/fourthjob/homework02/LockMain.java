package com.github.ck_oio.javaatcjobs.fourthjob.homework02;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.github.ck_oio.javaatcjobs.fourthjob.homework02.Tools.sum;
import static com.github.ck_oio.javaatcjobs.fourthjob.homework02.Tools.test10000;

public class LockMain {
    public static void main(String[] args) {
        test10000(LockMain::mth);
//        Tools.testMethod(LockMain::mth);
    }

    private static int mth() {
        final Lock lock = new ReentrantLock();
        final Condition finish = lock.newCondition();
        final int iniInt = -1;
        final int[] intHandler = {iniInt};

        new Thread(() -> {
            lock.lock();
            try{
                intHandler[0] = sum();
            } finally {
                lock.unlock();
            }
        }).start();

        // 确保线程执行顺序落后与另一个线程
        lock.lock();
        try {
            if (intHandler[0] == iniInt) {
                try {
                    finish.await(1, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }finally {
            lock.unlock();
        }

        return intHandler[0];
    }
}
