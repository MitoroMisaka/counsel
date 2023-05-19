package com.ecnu.rai.counsel.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.rai.counsel.config.WXConfig;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.entity.WXUser;
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
        String openId = bodyJson.getString("openid");
        String session_key = bodyJson.getString("session_key");
        WXUser u = new WXUser();
        u.setOpenid(openId);
        u.setSession_key(session_key);
        String token = TokenGenUtil.TokenGen(u);


return token;
        //return "登录成功";
    }



}
