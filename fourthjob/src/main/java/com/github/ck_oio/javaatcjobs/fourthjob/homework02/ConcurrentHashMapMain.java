package com.github.ck_oio.javaatcjobs.fourthjob.homework02;

import java.util.concurrent.ConcurrentHashMap;

import static com.github.ck_oio.javaatcjobs.fourthjob.homework02.Tools.sum;
import static com.github.ck_oio.javaatcjobs.fourthjob.homework02.Tools.test10000;

public class ConcurrentHashMapMain {
    public static void main(String[] args) {

        //testMethod(ConcurrentHashMapMain::mth);
        test10000(ConcurrentHashMapMain::mth);
    }
    private static int mth(){
        final ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap();
        Integer handlerKey = 0;
        Integer intInit = -1;
        map.put(handlerKey, intInit);
        new Thread(()-> map.put(handlerKey, sum())).start();
        while (map.get(handlerKey).equals(intInit)) {
            Thread.yield();
        }
        return map.get(handlerKey);
    }
}
