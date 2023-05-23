package com.ecnu.rai.counsel.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.config.WXConfig;
import com.ecnu.rai.counsel.dao.SigninRequest;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.entity.Visitor;
import com.ecnu.rai.counsel.mapper.UserMapper;
import com.ecnu.rai.counsel.mapper.VisitorMapper;
import com.ecnu.rai.counsel.utils.CommonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ecnu.rai.counsel.util.TokenGenUtil;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
public class WXController {
    @Autowired
    private WXConfig wxConfig;

    @Autowired
    private WXService wxService;
    
    @Autowired
    private VisitorMapper visitorMapper;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/wx/signin")
    public Result signin(@RequestBody SigninRequest request) {
        // Check if the user has already registered
        if(visitorMapper.selectByOpenid(request.getOpenid()) != null) {
            return Result.fail("该微信账号已注册");
        }
        // Check if the real name is valid
        if(!request.getRealName().matches("[\\u4e00-\\u9fa5a-zA-Z]{2,32}")) {
            return Result.fail("真实姓名格式不正确");
        }
        // Check if the phone number is valid
        if(!request.getPhoneNumber().matches("^1(3|4|5|6|7|8|9)\\d{9}$")) {
            return Result.fail("联系电话格式不正确");
        }
        // Check if the emergency contact name is valid
        if(!request.getEmergencyContactName().matches("[\\u4e00-\\u9fa5a-zA-Z]{2,32}")) {
            return Result.fail("紧急联系人姓名格式不正确");
        }
        // Check if the emergency contact phone number is valid
        if(!request.getEmergencyContactPhoneNumber().matches("^1(3|4|5|6|7|8|9)\\d{9}$")) {
            return Result.fail("紧急联系人电话格式不正确");
        }
        // Check if the emergency contact phone number is different from the user's phone number
        if(request.getEmergencyContactPhoneNumber().equals(request.getPhoneNumber())) {
            return Result.fail("紧急联系人电话不能和访客自己的电话号码重复");
        }
        User user = new User();
        user.setUsername(request.getOpenid());
        user.setRole("visitor");
        if(userMapper.findByUsername(request.getOpenid()) == null) {
            userMapper.insert(user);
        }
        else {
            return Result.fail("该微信账号已注册");
        }
        Long id = userMapper.findByUsername(request.getOpenid()).getId();
        // Register the user
        Visitor visitor = new Visitor();
        visitor.setId(id);
        visitor.setOpenid(request.getOpenid());
        visitor.setName(request.getRealName());
        visitor.setPhone(request.getPhoneNumber());
        visitor.setEmergentContact(request.getEmergencyContactName());
        visitor.setEmergentPhone(request.getEmergencyContactPhoneNumber());
        visitor.setRole("visitor");
        visitor.setGender(request.getGender());
        visitor.setAvatar("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKKEsVP4BbdXrF0d2Jve5mpwHkTrQ3BPtZxg&usqp=CAU");
        visitorMapper.insertVisitor(visitor);
        return Result.success("注册成功");
    }

    public String refreshToken(String refreshToken) throws IOException {
      String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token" +
               "appid="+wxConfig.getAppId()+"grant_type=refresh_token&refresh_token="
      +refreshToken;
      CloseableHttpClient httpClient = HttpClientBuilder.create().build();
      HttpGet httpGet = new HttpGet(url);
      CloseableHttpResponse response = httpClient.execute(httpGet);
      HttpEntity entity = response.getEntity();
      String body = CommonUtil.getBody(entity.getContent());
      JSONObject bodyJson = JSON.parseObject(body);
        return bodyJson.getString("access_token");
    };


    @ResponseBody
    @CrossOrigin
    @RequestMapping("/wx/login")
    public String login(HttpServletRequest request) throws IOException {

        String code = request.getParameter("code");
        if (code == null){
            log.error("用户取消登录");
        }
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="
                 +wxConfig.getAppId() +"&secret="+wxConfig.getAppSecret()
                 +"&js_code="+code+"&grant_type=authorization_code";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String body = CommonUtil.getBody(entity.getContent());
        log.info(body);
        response.close();

        JSONObject bodyJson = JSON.parseObject(body);
        String openid = bodyJson.getString("openid");
        String session_key = bodyJson.getString("session_key");
        String token = "";
        if(!WXService.visitorExist(openid)) {
            Visitor u = new Visitor();
            u.setOpenid(openid);
            WXService.insertNewVisitor(openid);
            token = TokenGenUtil.TokenGen(u);
//          u.setSession_key(session_key);
        }
        else {
            Visitor u = WXService.findByopenid(openid);
            u.setOpenid(openid);
            token = TokenGenUtil.TokenGen(u);
        }


return token;
     //   return "登录成功";
    }



}
