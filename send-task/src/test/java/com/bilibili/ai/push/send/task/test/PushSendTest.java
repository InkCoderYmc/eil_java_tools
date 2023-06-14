package com.bilibili.ai.push.send.task.test;

import com.bilibili.ai.push.SendInfo;
import com.bilibili.ai.push.send.task.common.DiscoveryUtils;
import com.bilibili.ai.push.send.task.model.AvCandidateInfo;
import com.bilibili.ai.push.send.task.service.SendServerClient;
import com.google.common.io.Files;
import com.googlecode.protobuf.format.JsonFormat;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author xumengyuan
 * @date 2022/12/13
 */
public class PushSendTest {
    @Before
    public void init() {
        System.setProperty("region", "all");
        System.setProperty("zone", "sh001");
        System.setProperty("tree_id", "28");
        System.setProperty("deploy_env", "pre");
        System.setProperty("discovery_zone", "sh001");
        DiscoveryUtils.getNamingClient().start();
    }

    @Test
    public void testRpc() {
//# candidate = send_pb2.AVCandidate(avid=347893685, trigger_type='1', user_type='2', red_point='1', userid_group=['1121098813'], reason_type='0')
//# request = send_pb2.AVContentRequest(candidate_records=[candidate])
        List<String> uidGroup = new ArrayList<>(500);
        for (int i = 0; i < 500; i++) {
            uidGroup.add("1121098813");
        }
        AvCandidateInfo avCandidateInfo = new AvCandidateInfo();
        avCandidateInfo.setReasonType("0");
        avCandidateInfo.setUserIdGroup(new ArrayList<>(1));
        avCandidateInfo.getUserIdGroup().addAll(uidGroup);
        avCandidateInfo.setUserType("1");
        avCandidateInfo.setTriggerType("1");
        avCandidateInfo.setRedPoint("1");
        avCandidateInfo.setAvid(347893685);
        SendInfo.AVCandidate build = SendInfo.AVCandidate.newBuilder()
                .setRedPoint(avCandidateInfo.getRedPoint())
                .setReasonType(avCandidateInfo.getReasonType())
                .setAvid(avCandidateInfo.getAvid())
                .setTriggerType(avCandidateInfo.getTriggerType())
                .setUserType(avCandidateInfo.getUserType())
                .addAllUseridGroup(avCandidateInfo.getUserIdGroup())
                .build();
        SendInfo.AVContentRequest.Builder builder = SendInfo.AVContentRequest.newBuilder();
        for (int i = 0; i < 5; i++) {
            builder.addCandidateRecords(build);
        }


//        SendInfo.AVContentResponse avContent = SendServerClient.getAVContent(builder.build());
//        System.out.println(avContent.toByteArray().length);
//        //        System.out.println(JsonFormat.printToString(avContent));

    }

    @Test
    public void pressureTest() throws IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("request.txt");
        List<String> strings = Files.readLines(new File(resource.getPath()), StandardCharsets.UTF_8);
        List<SendInfo.AVContentRequest> collect = strings.stream().map(x -> {
            SendInfo.AVContentRequest.Builder builder = SendInfo.AVContentRequest.newBuilder();
            try {
                JsonFormat.merge(x, builder);
                return builder.build();
            } catch (JsonFormat.ParseException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(() -> {
            for (int i = 0; i < 100; i++) {
                for (SendInfo.AVContentRequest request : collect) {
                    long start = System.currentTimeMillis();
//                    SendInfo.AVContentResponse avContent = SendServerClient.getAVContent(request);
                    String t = Thread.currentThread().getName();
                    long current = System.currentTimeMillis();
                    System.out.println(t + " s:" + start + " e:" + current + " duration:" + (current - start));
                }
            }
        });


    }

    @Test
    public void testSize() throws JsonFormat.ParseException {
        String raw = "{\"av_content_records\":[{\"candidate_record\":{\"avid\":649810272,\"trigger_type\":\"0\",\"user_type\":\"2\",\"red_point\":\"1\",\"userid_group\":[\"693934825\",\"521561078\",\"1601921260\",\"1881032585\",\"650046288\",\"504159538\",\"1774103090\",\"496619355\",\"1773296322\",\"1220744767\",\"349380268\",\"50530891\",\"381371790\",\"275132554\",\"67463524\",\"1622621133\",\"1298759286\",\"497330090\",\"1310441416\",\"1768804500\",\"623656732\",\"1529472155\",\"1155402620\",\"1466785090\",\"517362138\",\"2143202317\",\"474170976\",\"1563640092\",\"646756775\",\"43189723\",\"409949731\",\"1376582221\",\"399687679\"],\"reason_type\":\"0\"},\"app_id\":1,\"link_type\":2,\"business_id\":51,\"group\":\"2\",\"trigger_type\":\"0\",\"link_value\":\"649810272\",\"alert_title\":\"满级神话DLQ33试玩！\",\"msg_cnt\":1,\"placeholders\":\"{\\\"upper_nickname\\\": \\\"白嫖氪学家\\\", \\\"archive_name\\\": \\\"满级神话DLQ33试玩！\\\", \\\"upper_portrait\\\": \\\"https://i1.hdslb.com/bfs/face/d753379d9b7f8ad7513b92dad95fd4234af5da65.jpg\\\", \\\"archive_cover1\\\": \\\"\\\", \\\"archive_cover2\\\": \\\"\\\", \\\"interact_act\\\": \\\"\\\", \\\"archive_suffix\\\": \\\"【5万播放·1千点赞】\\\", \\\"archive_suffix2\\\": \\\"（1千点赞）\\\"}\",\"alert_body\":\"【5万播放·1千点赞】人气视频推荐\",\"template_id\":126,\"mids\":[\"693934825\",\"521561078\",\"1601921260\",\"1881032585\",\"650046288\",\"504159538\",\"1774103090\",\"496619355\",\"1773296322\",\"1220744767\",\"349380268\",\"50530891\",\"381371790\",\"275132554\",\"67463524\",\"1622621133\",\"1298759286\",\"497330090\",\"1310441416\",\"1768804500\",\"623656732\",\"1529472155\",\"1155402620\",\"1466785090\",\"517362138\",\"2143202317\",\"474170976\",\"1563640092\",\"646756775\",\"43189723\",\"409949731\",\"1376582221\",\"399687679\"],\"uuid\":\"4238067297905101560819ced285eefa\",\"data_body\":\"{\\\"app_id\\\": 1, \\\"link_type\\\": 2, \\\"business_id\\\": 51, \\\"group\\\": \\\"2\\\", \\\"trigger_type\\\": \\\"0\\\", \\\"link_value\\\": \\\"649810272\\\", \\\"alert_title\\\": \\\"满级神话DLQ33试玩！\\\", \\\"msg_cnt\\\": 1, \\\"placeholders\\\": \\\"{\\\\\\\"upper_nickname\\\\\\\": \\\\\\\"白嫖氪学家\\\\\\\", \\\\\\\"archive_name\\\\\\\": \\\\\\\"满级神话DLQ33试玩！\\\\\\\", \\\\\\\"upper_portrait\\\\\\\": \\\\\\\"https://i1.hdslb.com/bfs/face/d753379d9b7f8ad7513b92dad95fd4234af5da65.jpg\\\\\\\", \\\\\\\"archive_cover1\\\\\\\": \\\\\\\"\\\\\\\", \\\\\\\"archive_cover2\\\\\\\": \\\\\\\"\\\\\\\", \\\\\\\"interact_act\\\\\\\": \\\\\\\"\\\\\\\", \\\\\\\"archive_suffix\\\\\\\": \\\\\\\"【5万播放·1千点赞】\\\\\\\", \\\\\\\"archive_suffix2\\\\\\\": \\\\\\\"（1千点赞）\\\\\\\"}\\\", \\\"alert_body\\\": \\\"【5万播放·1千点赞】人气视频推荐\\\", \\\"ai_follow\\\": 0, \\\"template_id\\\": 126, \\\"mids\\\": \\\"693934825,521561078,1601921260,1881032585,650046288,504159538,1774103090,496619355,1773296322,1220744767,349380268,50530891,381371790,275132554,67463524,1622621133,1298759286,497330090,1310441416,1768804500,623656732,1529472155,1155402620,1466785090,517362138,2143202317,474170976,1563640092,646756775,43189723,409949731,1376582221,399687679\\\", \\\"uuid\\\": \\\"4238067297905101560819ced285eefa\\\"}\"}],\"task_id\":-1,\"job_id\":2481,\"push_id\":4,\"push_type\":\"old_usr_st_prod\",\"job_date\":\"20230113\"}";
        SendInfo.AVContentResponse.Builder builder = SendInfo.AVContentResponse.newBuilder();
        JsonFormat.merge(raw, builder);

        String mid = "504159538";
        for (SendInfo.AVContent.Builder builder1 : builder.getContentRecordsBuilderList()) {
            while (builder1.getMidsCount() < 500) {
                builder1.addMids(mid);
            }
        }
        SendInfo.AVContent contentRecords = builder.getContentRecords(0);
        for (int i = 0; i < 4; i++) {
            builder.addContentRecords(contentRecords);
        }
        SendInfo.AVContentResponse build = builder.build();
        System.out.println(build.toByteArray().length);


    }

}
