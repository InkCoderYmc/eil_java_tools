package com.bilibili.ai.push.send.task.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bilibili.ai.push.send.task.model.InteractSendResult;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Test;

import java.util.*;

/**
 * @author Bangkura
 */
public class JsonTest {
    String testStr = "{\"app_id\": 1, \"business_id\": 112, \"link_type\": 26, \"link_value\": \"{\\\"type\\\": \\\"3\\\"}\", \"mids\": \"275241347\", \"uuid\": \"58aa8d2a66d42f249407696801796cdd\", \"alert_title\": \"\u54d4\u54e9\u54d4\u54e9\", \"alert_body\": \"\u4f60\u6709\u672a\u8bfb\u7684\u70b9\u8d5e\u6d88\u606f\uff0c\u70b9\u51fb\u67e5\u770b>>\", \"msg_cnt\": 0}";
    @Test
    public void test() {
//        Map<String, Object> result = JSON.parseObject(testStr, Map.class);
//        System.out.println(result.get("app_id"));

        String testStr = "\001";
        System.out.println(testStr);
    }

    @Test
    public void testEscape() {
        List<Object> testResult = new ArrayList<>();
        testResult.add(1);
        testResult.add("test");

        String testStr = JSON.toJSONString(testResult);
        String content = "123" + "\001" + "sendType" + "\001" + testStr;
        Map<String, String> testMap = new HashMap<>();
        testMap.put("key", content);

        String content2 = "0\u0001\u0001\u00010\u0001av\u0001[\"[306934522,\"0\",\"你们心中也有白月光和不能释怀的人吗？\",\"【266万播放·16万点赞】人气视频推荐\",\"514258613\",\"f38f9bca8ce5d036596c0a148336e159\",-2,\"fake_message\",0,-1,0,0,\"1672990343\",\"1\",null,\"1\"]";
        System.out.println(content2);
        System.out.println(StringEscapeUtils.unescapeJava(JSON.toJSONString(testMap)));
    }

    @Test
    public void testJsonStr() {
        List<List<Object>> result = new ArrayList<>();
        List<Object> resultList = new ArrayList<>();

        resultList.add("栗子\"再胖下去只能做搞笑艺人了\"");
        result.add(resultList);
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void testDate() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println(hour);
    }

    @Test
    public void interactTest() {
        String interactStr = "{\"body\":\"\",\"code\":-2,\"ctime\":\"1673944545\",\"ext\":\"{\\\"interact_type\\\": like, \\\"push_id\\\": 4}\",\"linkType\":26,\"linkValue\":\"{\\\"type\\\": \\\"3\\\"}\",\"message\":\"fake_message\",\"redPoint\":\"0\",\"retryNum\":0,\"taskData\":0,\"title\":\"你有未读的点赞消息，点击查看>>\",\"ttl\":-1,\"userId\":\"193066314\",\"userType\":\"1\",\"uuid\":\"2c36f085bea6b6ca2254e5c68376a572\"}";
        InteractSendResult interactSendResult = JSON.parseObject(interactStr, InteractSendResult.class);
        System.out.println(JSON.toJSONString(Collections.singletonList(interactSendResult)));
    }

    public static class T {
        private Object str;

        public T(Object str) {
            this.str = str;
        }

        public Object getStr() {
            return str;
        }

        public void setStr(Object str) {
            this.str = str;
        }
    }
}
