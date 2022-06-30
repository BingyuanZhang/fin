package com.zby.fin.sms;


import com.zby.fin.sms.service.SmsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)//spring上下文环境
public class ServiceTest {
    @Resource
    SmsService smsService;

    @Test
    public void test1()
    {
        smsService.send("18030223964","0349");
    }
}
