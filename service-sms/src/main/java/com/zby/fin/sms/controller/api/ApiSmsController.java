package com.zby.fin.sms.controller.api;

import com.zby.common.exception.Assert;
import com.zby.common.result.R;
import com.zby.common.result.ResponseEnum;
import com.zby.common.util.RandomUtils;
import com.zby.common.util.RegexValidateUtils;
import com.zby.fin.sms.client.CoreUserInfoClient;
import com.zby.fin.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/sms")
@Api(tags = "短信管理")
@Slf4j
public class ApiSmsController {

    @Resource
    private SmsService smsService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CoreUserInfoClient coreUserInfoClient;

    @ApiOperation("获取验证码")
    @GetMapping("/send/{mobile}")
    public R send(
            @ApiParam(value = "手机号", required = true)
            @PathVariable String mobile){

        //MOBILE_NULL_ERROR(-202, "手机号不能为空"),
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        //MOBILE_ERROR(-203, "手机号不正确"),
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile), ResponseEnum.MOBILE_ERROR);

        //判断手机号是否已经注册
        boolean result = coreUserInfoClient.checkMobile(mobile);
        log.info("result = " + result);
        Assert.isTrue(result == false, ResponseEnum.MOBILE_EXIST_ERROR);

        //生成验证码
        String randomCode = RandomUtils.getFourBitRandom();
        //发送短信
        smsService.send(mobile, randomCode);

        //将验证码存入redis
        redisTemplate.opsForValue().set("fin:sms:code:" + mobile, randomCode, 5, TimeUnit.MINUTES);

        return R.ok().message("短信发送成功");
    }
}