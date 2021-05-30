package com.github.ck_oio.javaatcjobs.fourthjob.homework02;

import java.util.concurrent.CompletableFuture;

import static com.github.ck_oio.javaatcjobs.fourthjob.homework02.Tools.test10000;

public class CompletableFutureMain {
    public static void main(String[] args) {
//        Tools.testMethod(CompletableFutureMain::mth);
        test10000(CompletableFutureMain::mth);
    }
    private static int mth(){
        final CompletableFuture<Integer> future = CompletableFuture.supplyAsync(Tools::sum);
        return future.join();
    }
}
