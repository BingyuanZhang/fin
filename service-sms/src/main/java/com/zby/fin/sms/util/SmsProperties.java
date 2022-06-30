package com.zby.fin.sms.util;


import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
//注意prefix要写到最后一个 "." 符号之前
//调用setter为成员赋值
@ConfigurationProperties(prefix = "cloopen.sms")
public class SmsProperties implements InitializingBean {

    private String accountSid;
    private String accountToken;
    private String accountAppid;
    private String serverPort;
    private String serverIp;

    public static String ACCOUNT_SID;
    public static String ACCOUNT_TOKEN;
    public static String ACCOUNT_APPID;
    public static String SERVER_IP;
    public static String SERVER_PORT;

    //当私有成员被赋值后，此方法自动被调用，从而初始化常量,这个方法是InitializingBean里的
    @Override
    public void afterPropertiesSet() throws Exception {
       ACCOUNT_APPID=accountAppid;
       ACCOUNT_SID=accountSid;
       ACCOUNT_TOKEN=accountToken;
       SERVER_IP=serverIp;
       SERVER_PORT=serverPort;
    }
}