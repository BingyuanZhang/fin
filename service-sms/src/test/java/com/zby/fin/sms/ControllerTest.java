package com.zby.fin.sms;


import com.zby.fin.sms.controller.api.ApiSmsController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)//spring上下文环境
public class ControllerTest {
    @Resource
    ApiSmsController apiSmsController;

    @Test
    public void test1()
    {
        apiSmsController.send("18030223964");
    }
}
