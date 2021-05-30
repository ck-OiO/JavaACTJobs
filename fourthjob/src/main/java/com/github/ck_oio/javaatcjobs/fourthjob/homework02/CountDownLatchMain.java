package com.github.ck_oio.javaatcjobs.fourthjob.homework02;

import java.util.concurrent.CountDownLatch;

import static com.github.ck_oio.javaatcjobs.fourthjob.homework02.Tools.sum;

public class CountDownLatchMain {
    public static void main(String[] args) {
        Tools.test10000(CountDownLatchMain::mth);
//Tools.testMethod(CountDownLatchMain::mth);

    }

    private static int mth() {
        final CountDownLatch latch = new CountDownLatch(1);
        final int iniInt = -1;
        final int[] intHandler = {iniInt};
        new Thread(() -> {
            intHandler[0] = sum();
            latch.countDown();
        }).start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  intHandler[0];
    }
}
