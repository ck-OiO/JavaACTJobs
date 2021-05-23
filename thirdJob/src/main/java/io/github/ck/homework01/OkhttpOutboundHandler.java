package io.github.ck.homework01;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OkhttpOutboundHandler {
    // 缓存客户端
    private static OkHttpClient okClient = new OkHttpClient.Builder()
            .callTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .build();

    // 发送get请求, 返回body 字符串
    public static String getAsString(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try(Response res = okClient.newCall(request).execute()){
            return res.body().string();
        }
    }
}


