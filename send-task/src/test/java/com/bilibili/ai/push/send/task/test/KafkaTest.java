package com.bilibili.ai.push.send.task.test;

import com.bilibili.ai.push.SendInfo;
import com.bilibili.ai.push.send.task.common.ConfigUtils;
import com.bilibili.ai.push.send.task.common.StreamConfig;
import com.google.common.io.Files;
import com.googlecode.protobuf.format.JsonFormat;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.BytesSerializer;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

/**
 * @author xumengyuan
 * @date 2023/1/17
 */
public class KafkaTest {
    @Test
    public void sendCalcResult() throws IOException {
        StreamConfig config = ConfigUtils.getKafkaStreamConfig("push.sink");
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", config.getAddress());
        properties.setProperty("acks", "all");
        properties.setProperty("retries", "3");
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        KafkaProducer<byte[], byte[]> producer = new KafkaProducer<>(properties);
        SendInfo.SendPlatformDataCell.Builder builder = SendInfo.SendPlatformDataCell.newBuilder();
        URL resource = Thread.currentThread().getContextClassLoader().getResource("send_request.txt");
        List<String> strings = Files.readLines(new File(resource.getPath()), StandardCharsets.UTF_8);
        JsonFormat.merge(strings.get(0), builder);
        builder.setJobDate("20220110");
        builder.setPushId(1);
        builder.setTaskId(1111);
        builder.setIsSend(false);
        producer.send(new ProducerRecord<>(config.getTopic(), "".getBytes(StandardCharsets.UTF_8), builder.build().toByteArray()));


    }

}
