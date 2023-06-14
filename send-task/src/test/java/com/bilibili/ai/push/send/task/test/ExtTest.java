package com.bilibili.ai.push.send.task.test;

import com.alibaba.fastjson.JSON;
import com.bilibili.ai.push.send.task.model.InteractSendResult;
import org.apache.flink.util.StringUtils;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Bangkura
 */
public class ExtTest {
    @Test
    public void print() {
        InteractSendResult.Ext ext = new InteractSendResult.Ext("like", String.valueOf(2));
        System.out.println(JSON.toJSONString(ext));
    }

    @Test
    public void test() {
        Pattern tablePattern = Pattern.compile("\\[(.*)]");
        Matcher outputMatcher = tablePattern.matcher("[]");
        outputMatcher.find();
        String result = outputMatcher.group(1);
        String[] outputTablesGroup = outputMatcher.group(1).split(",", -1);
        System.out.println(outputTablesGroup[0]);
        if (outputTablesGroup != null) {
            for (String i : outputTablesGroup) {
                if (StringUtils.isNullOrWhitespaceOnly(i.trim())) {
                    continue;
                }
                System.out.println("urn:datacenter:tab:Hive1." + i.trim());
            }
        }
    }
}
