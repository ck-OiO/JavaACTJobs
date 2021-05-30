package com.github.ck_oio.javaatcjobs.fourthjob.homework02;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static com.github.ck_oio.javaatcjobs.fourthjob.homework02.Tools.sum;
import static com.github.ck_oio.javaatcjobs.fourthjob.homework02.Tools.testMethod;

public class FutureCallableMain {
    public static void main(String[] args) {
        testMethod(FutureCallableMain::mth);
    }

    private static int mth() {
        final FutureTask<Integer> futureTask = new FutureTask<>(() -> sum());
        new Thread(futureTask).start();

        int result = -1;
        try {
            result = futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }
}
