package com.bilibili.ai.push.send.task.test;

import org.apache.flink.calcite.shaded.org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Bangkura
 */
public class SyncTest {
    @Test
    public void test() {
        String result = signature("key", "secret");
        System.out.println("test");
    }

    private String signature(String appKey, String appSecret) {
        long ts = System.currentTimeMillis() / 1000;
        String param = String.format("appkey=%s&ts=%s", appKey, ts);
        String token = DigestUtils.md5Hex((param + appSecret).getBytes(StandardCharsets.UTF_8));

        return String.format("sign=%s&%s", token, param);
    }

    @Test
    public void testPool() throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            Future<String> submit = executorService.submit(() -> null);
            String s = submit.get();
            System.out.println(null == s);
        }
    }

}
