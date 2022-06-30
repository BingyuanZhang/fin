package com.zby.fin.sms.service.impl;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.zby.common.exception.Assert;
import com.zby.common.exception.BusinessException;
import com.zby.common.result.ResponseEnum;
import com.zby.fin.sms.service.SmsService;
import com.zby.fin.sms.util.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {
        @Override
        public void send(String mobile,String RandomCode) {

                //创建远程连接客户端对象
                CCPRestSmsSDK ccpRestSmsSDK = new CCPRestSmsSDK();

                //创建远程连接的请求参数
                ccpRestSmsSDK.setAccount(SmsProperties.ACCOUNT_SID,SmsProperties.ACCOUNT_TOKEN);
                ccpRestSmsSDK.setAppId(SmsProperties.ACCOUNT_APPID);
                ccpRestSmsSDK.setBodyType(BodyType.Type_JSON);
                ccpRestSmsSDK.init(SmsProperties.SERVER_IP,SmsProperties.SERVER_PORT);

                String templateId="1";//短信模板
                String time="2";//分钟参数
                String[] datas = {RandomCode, "2"};

                try {
                        //使用客户端对象携带请求对象发送请求并得到响应结果
                        //第一个参数是手机号，第二个参数是你是用的第几个模板，第三个参数是你的验证码，第四个是在几分钟之内输入
                        HashMap<String, Object> result = ccpRestSmsSDK.sendTemplateSMS(mobile,templateId,datas);
                        boolean ifNull = result.equals(null);
                        //ALIYUN_RESPONSE_FAIL(-501, "云响应失败"),
                        Assert.isTrue(!ifNull, ResponseEnum.ALIYUN_RESPONSE_FAIL);

                        //获取返回的状态码
                        Object statusCode = result.get("statusCode");

                        //ALIYUN_SMS_LIMIT_CONTROL_ERROR(-502, "短信发送过于频繁"),//业务限流
                        Assert.notEquals("160038", statusCode, ResponseEnum.ALIYUN_SMS_LIMIT_CONTROL_ERROR);


                        //正常返回输出data包体信息（map）
                        HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
                        Set<String> keySet = data.keySet();


                        log.info("云短信发送响应结果：");
                        for(String key:keySet){
                                Object object = data.get(key);
                                log.info(key +" = "+object);
                        }

                        //ALIYUN_SMS_ERROR(-503, "短信发送失败"),//其他失败
                        Assert.equals("000000", statusCode, ResponseEnum.ALIYUN_SMS_ERROR);

                }
                catch (Exception e)
                {
                        log.error("云短信发送SDK调用失败");
                        throw new BusinessException(ResponseEnum.ALIYUN_SMS_ERROR , e);
                }
        }
}
