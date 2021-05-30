package com.github.ck_oio.javaatcjobs.fourthjob.homework02;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.github.ck_oio.javaatcjobs.fourthjob.homework02.Tools.sum;

public class SingleThreadPoolMain {
    public static void main(String[] args) {

        Tools.testMethod(SingleThreadPoolMain::mth);
//        test10000(SingleThreadPoolMain::mth);
    }

    private static int mth()  {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();

        final Future<Integer> resultFuture = executorService.submit(() -> sum());
        executorService.shutdown();


        Integer result = -1;
        try {
            result = resultFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }
}
