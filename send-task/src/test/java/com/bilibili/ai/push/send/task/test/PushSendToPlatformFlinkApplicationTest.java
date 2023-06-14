package com.bilibili.ai.push.send.task.test;

import com.alibaba.fastjson.JSON;
import com.bilibili.ai.push.SendInfo;
import com.bilibili.ai.push.send.task.common.Log;
import com.bilibili.ai.push.send.task.func.AsyncSendToPlatformMapFunction;
import com.bilibili.ai.push.send.task.model.*;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author bangkuura
 * @date 2022/12/20
 */
public class PushSendToPlatformFlinkApplicationTest {
    private static final Log Logger = Log.createLogger(PushSendToPlatformFlinkApplicationTest.class);
    private final StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

    private List<byte[]> buildInput() {
        SendInfo.AVContent.Builder avContentBuilder = SendInfo.AVContent.newBuilder();
        SendInfo.AVCandidate.Builder avCandidateBuilder = SendInfo.AVCandidate.newBuilder();
        avCandidateBuilder.setAvid(347893685);
        avCandidateBuilder.setTriggerType("1");
        avCandidateBuilder.setUserType("2");
        avCandidateBuilder.setRedPoint("1");
        avCandidateBuilder.addUseridGroup("431681029");
        avCandidateBuilder.setReasonType("0");
        avCandidateBuilder.setPushId(2);
        avContentBuilder.setCandidateRecord(avCandidateBuilder.build());

        avContentBuilder.setAppId(1);
        avContentBuilder.setLinkType(2);
        avContentBuilder.setBusinessId(51);
        avContentBuilder.setGroup("2");
        avContentBuilder.setTriggerType("1");
        avContentBuilder.setLinkValue("347893685");
        avContentBuilder.setAlertTitle("良田田田: 第一次在运动会当众被表白了，气氛都到这了。。。");
        avContentBuilder.setMsgCnt(1);
        avContentBuilder.setPlaceholders("{\"upper_nickname\": \"良田田田\", \"archive_name\": \"第一次在运动会当众被表白了，气氛都到这了。。。\", \"upper_portrait\": \"\", \"archive_cover1\": \"\", \"archive_cover2\": \"\", \"interact_act\": \"\", \"archive_suffix\": \"【72万播放·10万点赞】\", \"archive_suffix2\": \"（10万点赞）\"}");
        avContentBuilder.setAlertBody("你关注的up主近期新作【72万播放·10万点赞】");
        avContentBuilder.setAiFollow(1);
        avContentBuilder.setTemplateId(128);
        avContentBuilder.addMids("1121098813");
        avContentBuilder.setUuid("f1140e3de01547b94763b70767a49ddc");

        SendInfo.InteractContent.Builder interactContentBuilder = SendInfo.InteractContent.newBuilder();
        SendInfo.InteractCandidate.Builder interactCandidateBuilder = SendInfo.InteractCandidate.newBuilder();
        interactCandidateBuilder.setMid("275241347");
        interactCandidateBuilder.setRedPoint("0");
        interactCandidateBuilder.setUserType("1");
        interactCandidateBuilder.setInteractType("like");

        interactContentBuilder.setCandidateRecord(interactCandidateBuilder);

        interactContentBuilder.setInteractType("like");
        interactContentBuilder.setAppId(1);
        interactContentBuilder.setBusinessId(112);
        interactContentBuilder.setLinkType(26);
        interactContentBuilder.setLinkValue("{\"type\": \"3\"}");
        interactContentBuilder.addMids("275241347");
        interactContentBuilder.setUuid("58aa8d2a66d42f249407696801796cdd");
        interactContentBuilder.setAlertTitle("你 有未读的点赞消息，点击查看>>");

        SendInfo.SendPlatformDataCell.Builder dataCellBuilder =  SendInfo.SendPlatformDataCell.newBuilder();
        dataCellBuilder.addInteractContentRecords(interactContentBuilder.build());
        dataCellBuilder.addAvContentRecords(avContentBuilder.build());
        SendInfo.SendPlatformDataCell sendPlatformDataCell = dataCellBuilder.build();


        List<byte[]> result = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            result.add(sendPlatformDataCell.toByteArray());
        }

        return result;
    }

    public void start(String taskName) {
        prepare(executionEnvironment);
        // print work plan
        System.out.println(executionEnvironment.getExecutionPlan());
        try {
            executionEnvironment.execute(taskName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepare(StreamExecutionEnvironment env) {
        int sourceParallelism = 4;
        int processParallelism = 40;

        SingleOutputStreamOperator<byte[]> dataStream = env.fromCollection(buildInput());

        SingleOutputStreamOperator<SendPlatformDataCell> blockStream = dataStream.map(x -> {
            SendInfo.SendPlatformDataCell sendPlatformDataCellPb = SendInfo.SendPlatformDataCell.parseFrom(x);
            SendPlatformDataCell sendPlatformDataCell = new SendPlatformDataCell(sendPlatformDataCellPb.getIsSend());
            for (SendInfo.AVContent avContentPb : sendPlatformDataCellPb.getAvContentRecordsList()) {
                AVContent avContent = new AVContent();

                AvCandidateInfo avCandidateInfo = new AvCandidateInfo();
                avCandidateInfo.setAvid(avContentPb.getCandidateRecord().getAvid());
                avCandidateInfo.setTriggerType(avContentPb.getCandidateRecord().getTriggerType());
                avCandidateInfo.setUserType(avContentPb.getCandidateRecord().getUserType());
                avCandidateInfo.setRedPoint(avContentPb.getCandidateRecord().getRedPoint());
                avCandidateInfo.setUserIdGroup(new ArrayList<>(avContentPb.getCandidateRecord().getUseridGroupList()));
                avCandidateInfo.setReasonType(avContentPb.getCandidateRecord().getReasonType());
                avCandidateInfo.setPushId(avContentPb.getCandidateRecord().getPushId());
                avContent.setAvCandidateInfo(avCandidateInfo);

                avContent.setAppId(avContentPb.getAppId());
                avContent.setLinkType(avContentPb.getLinkType());
                avContent.setBusinessId(avContentPb.getBusinessId());
                avContent.setGroup(avContentPb.getGroup());
                avContent.setTriggerType(avContentPb.getTriggerType());
                avContent.setLinkValue(avContentPb.getLinkValue());
                avContent.setAlertTitle(avContentPb.getAlertTitle());
                avContent.setMsgCnt(avContentPb.getMsgCnt());
                avContent.setPlaceholders(avContentPb.getPlaceholders());
                avContent.setAlertBody(avContentPb.getAlertBody());
                avContent.setAiFollow(avContentPb.getAiFollow());
                avContent.setTemplateId(avContentPb.getTemplateId());
                avContent.setImageUrl(avContentPb.getImageUrl());
                avContent.setUserIds(new ArrayList<>(avContentPb.getMidsList()));
                avContent.setUuid(avContentPb.getUuid());
                avContent.setDataBody(avContentPb.getDataBody());
                sendPlatformDataCell.addAvContent(avContent);
            }

            for (SendInfo.InteractContent interactContentPb: sendPlatformDataCellPb.getInteractContentRecordsList()) {
                InteractContent interactContent = new InteractContent();

                InteractCandidateInfo interactCandidateInfo = new InteractCandidateInfo();
                interactCandidateInfo.setMid(interactContentPb.getCandidateRecord().getMid());
                interactCandidateInfo.setRedPoint(interactContentPb.getCandidateRecord().getRedPoint());
                interactCandidateInfo.setPushId(interactContentPb.getCandidateRecord().getPushId());
                interactCandidateInfo.setUserType(interactContentPb.getCandidateRecord().getUserType());
                interactCandidateInfo.setInteractType(interactContentPb.getCandidateRecord().getInteractType());
                interactContent.setInteractCandidateInfo(interactCandidateInfo);

                interactContent.setInteractType(interactContentPb.getInteractType());
                interactContent.setAppId(interactContentPb.getAppId());
                interactContent.setBusinessId(interactContentPb.getBusinessId());
                interactContent.setLinkType(interactContentPb.getLinkType());
                interactContent.setLinkValue(interactContentPb.getLinkValue());
                interactContent.setMids(new ArrayList<>(interactContentPb.getMidsList()));
                interactContent.setUuid(interactContentPb.getUuid());
                interactContent.setAlertTitle(interactContentPb.getAlertTitle());
                interactContent.setAlertBody(interactContentPb.getAlertBody());
                interactContent.setMsgCnt(interactContentPb.getMsgCnt());
                interactContent.setDataBody(interactContentPb.getDataBody());
                sendPlatformDataCell.addInteractContent(interactContent);
            }

            return sendPlatformDataCell;
        }).name("map").setParallelism(processParallelism);

        SingleOutputStreamOperator<SendResult> sendStream = AsyncDataStream.unorderedWait(blockStream
                        , new AsyncSendToPlatformMapFunction(), 10, TimeUnit.SECONDS)
                .setParallelism(processParallelism)
                .name("async_get_av_content");

        sendStream.addSink(new SinkFunction<SendResult>() {
            @Override
            public void invoke(SendResult value, Context context) throws Exception {
                System.out.println(JSON.toJSONString(value));
            }
        });
    }

    @Test
    public void test() {
        PushSendToPlatformFlinkApplicationTest application = new PushSendToPlatformFlinkApplicationTest();
        application.start("test_result");
    }
}
