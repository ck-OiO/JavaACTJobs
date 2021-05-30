package com.github.ck_oio.javaatcjobs.fourthjob.homework02;

import static com.github.ck_oio.javaatcjobs.fourthjob.homework02.Tools.sum;

public class YieldMain {
    public static void main(String[] args) {

        Tools.testMethod(YieldMain::mth);
    }

    private static int mth() {
        final int iniInt = -1;
        final int[] intHandler = {iniInt};

        new Thread(() -> {
            intHandler[0] = sum();
        }).start();

        // IDEA 在main线程所在线程组中增加一个Ctrl-Break的线程, 活动线程数至少为两个.
        while (Thread.activeCount() > 2) {
            // 这里也可以使用sleep(), LockSupport 的几个定时阻塞方法停止当前线程.
            // Thread.sleep(10)
//            LockSupport.parkNanos(10000);
//            LockSupport.parkUntil(System.currentTimeMillis() + 10000);
//            LockSupport.parkNanos(new Object(), 10000);
//            LockSupport.parkUntil(new Object(), System.currentTimeMillis() + 10000);
            Thread.yield();
        }

        return intHandler[0];
    }
}
