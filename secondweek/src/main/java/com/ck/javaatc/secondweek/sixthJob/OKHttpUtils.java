package com.ck.javaatc.secondweek.sixthJob;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OKHttpUtils {
    // 缓存客户端
    private static OkHttpClient okClient = new OkHttpClient();

    // 发送get请求, 返回body 字符串
    public static String getAsString(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try(Response res = okClient.newCall(request).execute()){
            return res.body().string();
        }
    }

    public static void main(String[] args) throws IOException {
        String url = "http://localhost:8801";
        String body = getAsString(url);
        System.out.println("url: " + url + "; response:" + body);
    }

}
