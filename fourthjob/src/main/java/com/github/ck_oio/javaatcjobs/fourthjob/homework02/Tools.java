package com.github.ck_oio.javaatcjobs.fourthjob.homework02;

import java.util.function.IntSupplier;

public class Tools {

    public static final int FIBO_INIT_NUM = 6;
    public static final int EXPECT_ESULT = 13;

    public static void test10000(IntSupplier supplier){
        for (int i = 0; i < 10000; i++) {
            int expect = supplier.getAsInt();
            if ( expect != EXPECT_ESULT) {
                System.out.printf("期望:%d, 实际是:%d%n", EXPECT_ESULT, expect);
            }
        }

    }
    public static void testMethod(IntSupplier supplier) {

        long start = System.currentTimeMillis();

        int result = supplier.getAsInt();

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    public static int sum() {
        return fibo(FIBO_INIT_NUM);
    }

    private static int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }
}
