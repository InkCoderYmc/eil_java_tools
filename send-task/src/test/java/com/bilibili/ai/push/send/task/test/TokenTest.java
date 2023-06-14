package com.bilibili.ai.push.send.task.test;

import com.bilibili.ai.push.send.task.common.ConfigUtils;
import com.bilibili.ai.push.send.task.utils.RedisUtils;
import org.junit.Test;
import redis.clients.jedis.JedisCluster;

/**
 * @author Bangkura
 */
public class TokenTest {
    private static final String REDIS_CLUSTER_BROKERS = ConfigUtils.getProperty("redis.token.brokers", "");
    @Test
    public void test() {
        JedisCluster jedisCluster = RedisUtils.createJedisCluster(REDIS_CLUSTER_BROKERS);
        String jobTokenKey = "job_token_cnt_" + 12345;
        if (!jedisCluster.exists(jobTokenKey)) {
            Long result = jedisCluster.setnx(jobTokenKey, "0");
            if (result == 1) {
                jedisCluster.expire(jobTokenKey, 7200);
            }
        }
        jedisCluster.incr(jobTokenKey);
        long count = Long.parseLong(jedisCluster.get(jobTokenKey));
        System.out.println(count);
    }
}
