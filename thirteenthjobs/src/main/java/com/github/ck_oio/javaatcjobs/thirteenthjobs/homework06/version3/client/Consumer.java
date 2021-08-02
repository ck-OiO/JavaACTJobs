package com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version3.client;

import com.alibaba.fastjson.JSON;
import com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version3.Constants;
import com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version3.Msg;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Consumer implements Constants {
    /**
     * 将当前客户名称注册到broker 中.
     *
     * @param name
     */
    public Boolean subscribe(String name, String topicId) {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            final HttpGet get = new HttpGet(localHost + "subscribe?" +
                    "name=" + URLEncoder.encode(name, StandardCharsets.UTF_8) +
            "&topicId" + URLEncoder.encode(topicId, StandardCharsets.UTF_8));
            try (CloseableHttpResponse response = client.execute(get)) {
                final String entityStr = EntityUtils.toString(response.getEntity());
                return JSON.parseObject(entityStr, Boolean.class);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 按照添加顺序不断获取消息. 自动提交
     *
     * @return
     */
    public Msg peek(String name, String topicId) {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            final HttpGet get = new HttpGet(localHost + "peek?" +
                    "name=" + URLEncoder.encode(name, StandardCharsets.UTF_8) +
            "&topicId" + URLEncoder.encode(topicId, StandardCharsets.UTF_8));
            try (CloseableHttpResponse response = client.execute(get)) {
                String resStr = EntityUtils.toString(response.getEntity());
                return JSON.parseObject(resStr, Msg.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 按照offset 获取消息.
     * @param offset
     * @return
     */
    public Msg offsetSeek(String name, int offset, String topicId) {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            final HttpGet get = new HttpGet(localHost + "offsetseek?" +
                    "name=" + URLEncoder.encode(name, StandardCharsets.UTF_8)+
                    "&offset=" + offset);
            try (CloseableHttpResponse response = client.execute(get)) {
                String resStr = EntityUtils.toString(response.getEntity());
                return JSON.parseObject(resStr, Msg.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
/*

 */
    public Boolean commit(int offset, String name) {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            final HttpGet get = new HttpGet(localHost + "commit?" +
                    "name=" + URLEncoder.encode(name, StandardCharsets.UTF_8)+
                    "&offset=" + offset);
            try (CloseableHttpResponse response = client.execute(get)) {
                String resStr = EntityUtils.toString(response.getEntity());
                return JSON.parseObject(resStr, Boolean.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
