package com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version3.client;

import com.alibaba.fastjson.JSON;
import com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version3.Constants;
import com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version3.Msg;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Producer implements Constants {
    List<Future> responses = new ArrayList<>();

    /**
     * 异步提交
     * @param msg
     * @return
     */
    public Future<String> send(Msg msg) {
        final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
                final HttpPost post = new HttpPost(localHost + "send");
                final String msgStr = JSON.toJSONString(msg);
                post.setEntity(new StringEntity(msgStr, StandardCharsets.UTF_8));
                post.setHeader("Content-Type", "application/json;charset=utf8");
                try (CloseableHttpResponse response = client.execute(post)) {
                    return EntityUtils.toString(response.getEntity());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "false";
        });
        responses.add(future);
        return future;
    }

    public void flush(){
        for (Future<String> f :
                responses) {
            try {
                f.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        responses.clear();
    }
}
