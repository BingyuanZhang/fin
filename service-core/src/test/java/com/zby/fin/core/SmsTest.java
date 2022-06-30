package com.zby.fin.core;

import com.zby.fin.base.dto.SmsDTO;
import com.zby.fin.rabbitutil.constant.MQConst;
import com.zby.fin.rabbitutil.service.MQService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SmsTest {

    @Resource
    MQService mqService;

    @Test
    public void test1() {
        //发送短信
        SmsDTO smsDTO = new SmsDTO();
        smsDTO.setMobile("18030223964");
        smsDTO.setMessage("充值成功");
        mqService.sendMessage(MQConst.EXCHANGE_TOPIC_SMS, MQConst.ROUTING_SMS_ITEM, smsDTO);
    }
}
