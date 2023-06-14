package com.bilibili.ai.push.send.task.test;

import com.bilibili.ai.push.send.task.func.SendFrequencyFilterFunction;
import com.bilibili.ai.push.send.task.model.AVContent;
import com.bilibili.ai.push.send.task.model.AvCandidateInfo;
import com.bilibili.ai.push.send.task.model.SendPlatformSingleDataCell;
import com.bilibili.ai.push.send.task.model.TempDataCell;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author xumengyuan
 * @date 2023/1/30
 */
public class FreqControlTest {
    @Test
    public void testFreqControl() throws Exception {
        SendFrequencyFilterFunction function = new SendFrequencyFilterFunction();
        function.open(null);
        TempDataCell dataCell = new TempDataCell();
        dataCell.setMid("3461565749660001");
        dataCell.setUuid(UUID.randomUUID().toString());
        SendPlatformSingleDataCell cell = new SendPlatformSingleDataCell();
        AVContent avContent = new AVContent();
        AvCandidateInfo candidateInfo = new AvCandidateInfo();
        candidateInfo.setAvid(308203336L);
        avContent.setAvCandidateInfo(candidateInfo);
        avContent.setUserIds(new ArrayList<>(1));
        avContent.getUserIds().add("3461565749660001");
        cell.setAvContent(avContent);
        cell.setJobDate("20230129");
        cell.setPushId(2);
        cell.setPushType("old_usr_st_test");
        dataCell.setSourceCell(cell);
        boolean filter = function.filter(dataCell);
        System.out.printf(String.valueOf(filter));
    }
}
