package com.github.ck_oio.javaatcjobs.fourthjob.homework02;

import java.util.concurrent.ArrayBlockingQueue;

import static com.github.ck_oio.javaatcjobs.fourthjob.homework02.Tools.sum;
import static com.github.ck_oio.javaatcjobs.fourthjob.homework02.Tools.test10000;

public class BlockingQueueMain {
    public static void main(String[] args) {
//        testMethod(BlockingQueueMain::mth);
        test10000(BlockingQueueMain::mth);

    }
    private static int mth() {
        final ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1);
        new Thread(()->{
            try {
                queue.put(sum());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        int result = 0;
        try {
            result = queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;

    }
}
