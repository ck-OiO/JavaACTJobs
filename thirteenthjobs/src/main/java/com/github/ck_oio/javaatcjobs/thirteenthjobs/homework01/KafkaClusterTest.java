package com.github.ck_oio.javaatcjobs.thirteenthjobs.homework01;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

@Slf4j
public class KafkaClusterTest {
    public static void main(String[] args) throws IOException {
        final CloseableHttpClient client = HttpClientBuilder.create().build();
        final HttpGet httpGet = new HttpGet("http://localhost:8080/sendmsg");
        final CloseableHttpResponse response = client.execute(httpGet);
        final HttpEntity entity = response.getEntity();
        log.info("相应结果为:{}" + EntityUtils.toString(entity));
    }
}
