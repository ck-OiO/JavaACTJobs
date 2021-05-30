package com.github.ck_oio.javaatcjobs.fourthjob.homework02;

import java.util.concurrent.CopyOnWriteArrayList;

import static com.github.ck_oio.javaatcjobs.fourthjob.homework02.Tools.sum;

public class CopyOnWriteArrayListMain {
    public static void main(String[] args) {
        Tools.testMethod(CopyOnWriteArrayListMain::mth);
//        Tools.test10000(CopyOnWriteArrayListMain::mth);

    }
    private static int mth() {
        final CopyOnWriteArrayList<Integer> arr = new CopyOnWriteArrayList<>();
        int intInit = -1;
        arr.add(intInit);
        new Thread(() -> arr.set(0, sum())).start();

        while(arr.get(0) == intInit)
            Thread.yield();
        return arr.get(0);
    }
}
