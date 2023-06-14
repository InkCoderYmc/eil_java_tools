package com.bilibili.ai.push.send.task.test;

import com.bilibili.ai.push.send.task.common.ConfigUtils;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Bangkura
 */
public class DoneFileTest {
    @Test
    public void test() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        String DONE_FILE_BASE_PATH = ConfigUtils.getProperty("send.platform.done.file.path", "");
        String donePathStr = DONE_FILE_BASE_PATH + "/" + dateStr + "/" + 2 + "/done";
        System.out.println(donePathStr);
    }
}
