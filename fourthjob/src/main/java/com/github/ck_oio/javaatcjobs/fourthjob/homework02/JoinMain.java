package com.github.ck_oio.javaatcjobs.fourthjob.homework02;

import static com.github.ck_oio.javaatcjobs.fourthjob.homework02.Tools.sum;

public class JoinMain {
    public static void main(String[] args) {
        Tools.testMethod(JoinMain::mth);
    }

    private static int mth()  {
        final int iniInt = -1;
        final int[] intHandler = {iniInt};

        Thread thread = new Thread(() -> {
            intHandler[0] = sum();
        });
        thread.start();
        //确保主线程等待当前线程执行完毕.
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return intHandler[0];
    }
}
