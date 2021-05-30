package com.github.ck_oio.javaatcjobs.fourthjob.homework02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.ck_oio.javaatcjobs.fourthjob.homework02.Tools.sum;
import static com.github.ck_oio.javaatcjobs.fourthjob.homework02.Tools.testMethod;

public class CollectorsSynchronizedMain {
    public static void main(String[] args) {

        testMethod(CollectorsSynchronizedMain::mth);
//    test10000(CollectorsSynchronizedMain::mth);
}
    private static int mth(){
        final ArrayList<Integer> list = new ArrayList<>(1);
        final List<Integer> syncList = Collections.synchronizedList(list);

        new Thread(()->syncList.add(sum())).start();
        while(Thread.activeCount() > 2)
            Thread.yield();

        return syncList.get(0);
    }
}
