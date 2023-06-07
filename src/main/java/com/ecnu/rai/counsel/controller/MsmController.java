package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.service.MsmService;
import com.ecnu.rai.counsel.util.RandomUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/msm")
public class MsmController {
    @Autowired
    private MsmService msmService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ApiOperation(value = "发送手机验证码")
    @GetMapping("/send/{phone}")
    public Result sendCode(@PathVariable String phone) {

        // Check if the phone number is valid
        if(!phone.matches("^1(3|4|5|6|7|8|9)\\d{9}$")) {
            return Result.fail("Invalid phone number.");
        }

        String code = redisTemplate.opsForValue().get(phone);
        if (StringUtils.hasText(code)) {
            return Result.success("发送短信成功(Redis)", code);
        }
        //如果从redis获取不到
        //生成验证码
        String newCode = RandomUtils.getSixBitRandom();

        //整合腾讯云短信服务进行发送
        boolean flag = msmService.send(phone, newCode);
        if (flag) {
            //生成验证码放到redis里面，设置有效时间
            redisTemplate.opsForValue().set(phone, newCode, 60, TimeUnit.MINUTES);
            return Result.success("发送短信成功", newCode);
        }
        return Result.fail("发送短信失败");
    }
}

