package com.github.ck_oio.javaatcjobs.fourthjob.homework02;

import java.util.Vector;

import static com.github.ck_oio.javaatcjobs.fourthjob.homework02.Tools.sum;

/*
HashTab 类似
 */
public class VectorMain {
    public static void main(String[] args) {

        //Tools.testMethod(VectorMain::mth);
        Tools.test10000(VectorMain::mth);
    }

    private static int mth() {
        final Vector<Integer> vector = new Vector(1);
        new Thread(()-> vector.add(sum())).start();
        while(Thread.activeCount() > 2)
            Thread.yield();
        return vector.get(0);
    }
}
