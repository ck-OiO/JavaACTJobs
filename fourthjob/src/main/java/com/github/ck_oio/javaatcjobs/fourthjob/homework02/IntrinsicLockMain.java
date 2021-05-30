package com.github.ck_oio.javaatcjobs.fourthjob.homework02;

import static com.github.ck_oio.javaatcjobs.fourthjob.homework02.Tools.sum;

/**
 * 使用对象内部锁和sync调整线程执行顺序获取返回值.
 */
public class IntrinsicLockMain {
    public static void main(String[] args) {
        Tools.testMethod(IntrinsicLockMain::mth);
    }

    private static int mth(){
        final Object lock = new Object();
        final int iniInt = -1;
        final int[] intHandler = {iniInt};

        new Thread(()->{
            synchronized (lock){
                intHandler[0] = sum();
            }
        }).start();
        synchronized (lock){
            // 确保线程执行顺序落后与另一个线程
            if(intHandler[0] == iniInt) {
                try {
                    lock.wait(0, 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return intHandler[0];
    }



}
