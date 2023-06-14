package com.bilibili.ai.push.send.task.test;

import com.alibaba.fastjson.JSON;
import com.bilibili.ai.push.send.task.common.ConfigUtils;
import com.bilibili.ai.push.send.task.model.*;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bangkura
 */
public class SendTest {
    String testStr = "{\"avContentList\":[{\"aiFollow\":1,\"alertBody\":\"你关注的up主近期新作【7万播放·5千点赞】\",\"alertTitle\":\"小婷食日记: 扫荡夜市小吃街\",\"appId\":1,\"avCandidateInfo\":{\"avid\":905353211,\"jobId\":0,\"pushId\":0,\"reasonType\":\"0\",\"redPoint\":\"1\",\"taskId\":0,\"triggerType\":\"1\",\"userIdGroup\":[\"285776264\"],\"userType\":\"23\"},\"businessId\":51,\"dataBody\":\"{\\\"app_id\\\": 1, \\\"link_type\\\": 2, \\\"business_id\\\": 51, \\\"group\\\": \\\"23\\\", \\\"trigger_type\\\": \\\"1\\\", \\\"link_value\\\": \\\"905353211\\\", \\\"alert_title\\\": \\\"小婷食日记: 扫荡夜市小吃街\\\", \\\"msg_cnt\\\": 1, \\\"placeholders\\\": \\\"{\\\\\\\"upper_nickname\\\\\\\": \\\\\\\"小婷食日记\\\\\\\", \\\\\\\"archive_name\\\\\\\": \\\\\\\"扫荡夜市小吃街\\\\\\\", \\\\\\\"upper_portrait\\\\\\\": \\\\\\\"\\\\\\\", \\\\\\\"archive_cover1\\\\\\\": \\\\\\\"\\\\\\\", \\\\\\\"archive_cover2\\\\\\\": \\\\\\\"\\\\\\\", \\\\\\\"interact_act\\\\\\\": \\\\\\\"\\\\\\\", \\\\\\\"archive_suffix\\\\\\\": \\\\\\\"【7万播放·5千点赞】\\\\\\\", \\\\\\\"archive_suffix2\\\\\\\": \\\\\\\"（5千点赞）\\\\\\\"}\\\", \\\"alert_body\\\": \\\"你关注的up主近期新作【7万播放·5千点赞】\\\", \\\"ai_follow\\\": 1, \\\"template_id\\\": 128, \\\"mids\\\": \\\"399806542\\\", \\\"uuid\\\": \\\"54237538e23c60436a52f0fc171fb8a5\\\"}\",\"group\":\"23\",\"imageUrl\":\"\",\"linkType\":2,\"linkValue\":\"905353211\",\"mids\":[\"399806542\"],\"msgCnt\":1,\"placeholders\":\"{\\\"upper_nickname\\\": \\\"小婷食日记\\\", \\\"archive_name\\\": \\\"扫荡夜市小吃街\\\", \\\"upper_portrait\\\": \\\"\\\", \\\"archive_cover1\\\": \\\"\\\", \\\"archive_cover2\\\": \\\"\\\", \\\"interact_act\\\": \\\"\\\", \\\"archive_suffix\\\": \\\"【7万播放·5千点赞】\\\", \\\"archive_suffix2\\\": \\\"（5千点赞）\\\"}\",\"templateId\":128,\"triggerType\":\"1\",\"uuid\":\"54237538e23c60436a52f0fc171fb8a5\"}]}";
    String interactStr = "{\"avContentList\":[],\"interactContentList\":[{\"alertBody\":\"\",\"alertTitle\":\"你有未读的点赞消息，点击查看>>\",\"appId\":1,\"businessId\":112,\"dataBody\":\"{\\\"app_id\\\": 1, \\\"business_id\\\": 112, \\\"link_type\\\": 26, \\\"link_value\\\": \\\"{\\\\\\\"type\\\\\\\": \\\\\\\"3\\\\\\\"}\\\", \\\"mids\\\": \\\"399806542\\\", \\\"uuid\\\": \\\"13cfceb9d709fc4dffda4d50c27b7126\\\", \\\"alert_title\\\": \\\"哔哩哔哩\\\", \\\"alert_body\\\": \\\"你有未读的点赞消息，点击查看>>\\\", \\\"msg_cnt\\\": 1}\",\"interactCandidateInfo\":{\"interactType\":\"like\",\"mid\":\"399806542\",\"pushId\":2,\"redPoint\":\"1\",\"userType\":\"1\"},\"interactType\":\"like\",\"linkType\":26,\"linkValue\":\"{\\\"type\\\": \\\"3\\\"}\",\"mids\":[\"399806542\"],\"msgCnt\":1,\"uuid\":\"13cfceb9d709fc4dffda4d50c27b7126\"}]}";
    private static final String APP_KEY = ConfigUtils.getProperty("send.platform.http.app_key",
            "");
    private static final String SECRET = ConfigUtils.getProperty("send.platform.http.app_secret",
            "");

    private static final String AV_TOKEN = ConfigUtils.getProperty("send.platform.http.av.token",
            "");
    private static final String AV_URL = ConfigUtils.getProperty("send.platform.http.av.url",
            "");

    private static final String REPLY_TOKEN = ConfigUtils.getProperty("send.platform.http.reply.token",
            "");
    private static final String REPLY_URL = ConfigUtils.getProperty("send.platform.http.reply.url",
            "");

    private static final String LIKE_TOKEN = ConfigUtils.getProperty("send.platform.http.like.token",
            "");
    private static final String LIKE_URL = ConfigUtils.getProperty("send.platform.http.like.url",
            "");
    private transient OkHttpClient okHttpClient = new OkHttpClient();;

    @Test
    public void test() {
        SendPlatformDataCell sendPlatformDataCell = JSON.parseObject(testStr, SendPlatformDataCell.class);
        System.out.println("test");
        SendResult sendResult = new SendResult();
        for (AVContent avContent : sendPlatformDataCell.getAvContentList()) {

            AVSendResult avSendResult = doAVContentSend(avContent);

            sendResult.addAVSendResult(avSendResult);
        }
        System.out.println("test");
    }

    @Test
    public void testInteract() throws Exception {
        SendPlatformDataCell sendPlatformDataCell = JSON.parseObject(interactStr, SendPlatformDataCell.class);
        System.out.println("test");
        SendResult sendResult = new SendResult();

        for (InteractContent interactContent : sendPlatformDataCell.getInteractContentList()) {
            InteractSendResult interactSendResult = doInteractContentSend(interactContent, sendPlatformDataCell.isSend());
            if (interactSendResult != null) {
                sendResult.addInteractSendResult(interactSendResult);
            }
        }
        System.out.println("test");
    }

    private InteractSendResult doInteractContentSend(InteractContent interactContent, boolean isSend) throws Exception {
        Map<String, Object> data_body = parseDataBody(interactContent.getDataBody());
        if ("reply".equals(interactContent.getInteractType())) {
            SendPlatformResult result = doSendPlatformRequest(REPLY_URL, APP_KEY, SECRET, REPLY_TOKEN, data_body);
            return buildInteractSendResult(interactContent, result);
        } else if ("like".equals(interactContent.getInteractType())) {
            SendPlatformResult result = doSendPlatformRequest(LIKE_URL, APP_KEY, SECRET, LIKE_TOKEN, data_body);
            return buildInteractSendResult(interactContent, result);
        } else {
            return null;
        }
    }

    private InteractSendResult buildInteractSendResult(InteractContent interactContent, SendPlatformResult result) {
        InteractSendResult interactSendResult = new InteractSendResult();
        InteractCandidateInfo interactCandidateInfo = interactContent.getInteractCandidateInfo();
        interactSendResult.setTitle(interactContent.getAlertTitle());
        interactSendResult.setBody(interactContent.getAlertBody());
        interactSendResult.setUserId(interactCandidateInfo.getMid());
        interactSendResult.setUuid(interactContent.getUuid());
        interactSendResult.setCode(result.getCode());
        interactSendResult.setMessage(result.getMessage());
        interactSendResult.setTaskData(result.getTaskData());
        interactSendResult.setTtl(result.getTtl());
        interactSendResult.setRetryNum(result.getTryNum());
        interactSendResult.setCtime(System.currentTimeMillis() / 1000 + "");
        interactSendResult.setRedPoint(interactCandidateInfo.getRedPoint());
        interactSendResult.setUserType(interactCandidateInfo.getUserType());
        interactSendResult.setLinkType(interactContent.getLinkType());
        interactSendResult.setLinkValue(interactContent.getLinkValue());
        InteractSendResult.Ext ext = new InteractSendResult.Ext(interactContent.getInteractType(), String.valueOf(interactCandidateInfo.getPushId()));
        interactSendResult.setExt(JSON.toJSONString(ext));
        return interactSendResult;
    }


    private Map<String, Object> parseDataBody(String bodyStr) {
        if (StringUtils.isEmpty(bodyStr)) {
            return new HashMap<>();
        }

        return JSON.parseObject(bodyStr, Map.class);
    }

    private AVSendResult buildAVSendResult(AVContent avContent, SendPlatformResult result) {
        AVSendResult avSendResult = new AVSendResult();
        AvCandidateInfo avCandidateInfo = avContent.getAvCandidateInfo();
        avSendResult.setAvid(avCandidateInfo.getAvid());
        avSendResult.setTriggerType(avCandidateInfo.getTriggerType());
        avSendResult.setTitle(avContent.getAlertTitle());
        avSendResult.setBody(avContent.getAlertBody());
        avSendResult.setUserIds(avCandidateInfo.getUserIdGroup());
        avSendResult.setUuid(avContent.getUuid());
        avSendResult.setCode(result.getCode());
        avSendResult.setMessage(result.getMessage());
        avSendResult.setTaskData(result.getTaskData());
        avSendResult.setTtl(result.getTtl());
        avSendResult.setRetryNum(result.getTryNum());
        avSendResult.setCtime(System.currentTimeMillis() / 1000);
        avSendResult.setImageUrl(avSendResult.getImageUrl());
        avSendResult.setRedPoint(avCandidateInfo.getRedPoint());
        avSendResult.setUserType(avCandidateInfo.getUserType());

        return avSendResult;
    }

    @SuppressWarnings("unchecked")
    private AVSendResult doAVContentSend(AVContent avContent) {
        Map<String, Object> data_body = parseDataBody(avContent.getDataBody());
        SendPlatformResult response = doSendPlatformRequest(AV_URL, APP_KEY, SECRET, AV_TOKEN, data_body);
        return buildAVSendResult(avContent, response);
    }

    private SendPlatformResult doSendPlatformRequest(String baseUrl, String appKey, String secret, String token, Map<String, Object> data_body) {
        for (int i = 0; i < 5; ++i) {
            try {
                // do send
                String url = generateUrl(baseUrl, appKey, secret, new HashMap<>());
                FormBody.Builder builder = new FormBody.Builder();
                data_body.forEach((k, v) -> {
                    if (v != null) {
                        builder.add(k, v + "");
                    }
                });
                Request request = new Request.Builder().post(builder.build()).url(url)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Authorization", "token=" + token)
                        .build();
                // rateLimiter.tryAcquire();
                Response httpResponse = okHttpClient.newCall(request).execute();

                SendPlatformResult result = buildResponse(httpResponse, i);

                if (result == null) {
                    continue;
                }

                return result;
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
        return new SendPlatformResult(0, -2, "http_error", 0, -1);
    }

    private SendPlatformResult buildResponse(Response httpResponse, int tryNum) throws Exception {
        String responseBody = httpResponse.body().string();

        if (httpResponse.code() != 200 || StringUtils.isEmpty(responseBody)) {
            return null;
        }

        Map<String, Object> dataBody = JSON.parseObject(responseBody, Map.class);
        int code = (int) dataBody.get("code");
        if (code == -509 || code == 72008) {
            return null;
        }


        SendPlatformResult response = new SendPlatformResult();
        response.setTryNum(tryNum);
        response.setCode(code);
        response.setTtl((int) dataBody.get("ttl"));
        if (dataBody.containsKey("data")) {
            response.setTaskData((long) dataBody.get("data"));
        } else {
            response.setTaskData(0);
        }

        if (dataBody.containsKey("message")) {
            response.setMessage((String) dataBody.get("message"));
        } else {
            response.setMessage("");
        }

        return response;
    }

    private static class SendPlatformResult {
        private int tryNum;
        private int code;

        private String message;
        private long taskData;
        private int ttl;

        public SendPlatformResult() {
        }

        public SendPlatformResult(int tryNum, int code, String message, long taskData, int ttl) {
            this.tryNum = tryNum;
            this.code = code;
            this.message = message;
            this.taskData = taskData;
            this.ttl = ttl;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public long getTaskData() {
            return taskData;
        }

        public void setTaskData(long task_data) {
            this.taskData = task_data;
        }

        public int getTtl() {
            return ttl;
        }

        public void setTtl(int ttl) {
            this.ttl = ttl;
        }

        public int getTryNum() {
            return tryNum;
        }

        public void setTryNum(int tryNum) {
            this.tryNum = tryNum;
        }

    }

    private String generateUrl(String baseUrl, String appKey, String appSecret, Map<String, String> params) {
        StringBuilder paramBuilder = new StringBuilder();
        params.forEach((k, v) -> {
            if (v != null) {
                paramBuilder.append(String.format("%s=%s&", k, v));
            }
        });
        paramBuilder.append(signature(appKey, appSecret));
        return String.format("%s?%s", baseUrl, paramBuilder);
    }

    private String signature(String appKey, String appSecret) {
        long ts = System.currentTimeMillis() / 1000;
        String param = String.format("appkey=%s&ts=%s", appKey, ts);
        String sign = DigestUtils.md5Hex((param + appSecret).getBytes(StandardCharsets.UTF_8));
        return String.format("sign=%s&%s", sign, param);
    }
}
