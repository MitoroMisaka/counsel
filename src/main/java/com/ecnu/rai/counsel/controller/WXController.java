package com.ecnu.rai.counsel.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.rai.counsel.config.WXConfig;
import com.ecnu.rai.counsel.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Controller
public class WXController {
    @Autowired
    private WXConfig wxConfig;

    @ResponseBody
    @RequestMapping("/wx/login")
    public String login(HttpServletRequest request) throws IOException {
        String code = request.getParameter("code");
//        String state = request.getParameter("state");
//        if (code == null){
//            log.error("用户取消登录");
//        }
//        log.info("code = {}", code);
//        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//        String url = "https://api.weixin.qq.com/sns/oauth2/access_token" +
//                "?appid=" + wxConfig.getAppId() +
//                "&secret=" + wxConfig.getAppSecret() +
//                "&code=" + code +
//                "&grant_type=authorization_code";
//        HttpGet httpGet = new HttpGet(url);
//        CloseableHttpResponse response = httpClient.execute(httpGet);
//        HttpEntity entity = response.getEntity();
//        String body = CommonUtil.getBody(entity.getContent());
//        log.info(body);
//        response.close();
//
//        JSONObject bodyJson = JSON.parseObject(body);
//        // 这里已经获取了用户的部分信息，可以在数据库中查询，如果已经记录过了，就没有必要进入后面的步骤了
//        String accessToken = bodyJson.getString("access_token");
//        String openId = bodyJson.getString("openid");
//        String refreshToken = bodyJson.getString("refresh_token");
//        url = "https://api.weixin.qq.com/sns/oauth2/refresh_token" +
//                "appid="+wxConfig.getAppId()+"grant_type=refresh_token&refresh_token="
//        +refreshToken;
//        httpGet = new HttpGet(url);
//        response = httpClient.execute(httpGet);
//        body = CommonUtil.getBody(response.getEntity().getContent());
//        bodyJson = JSON.parseObject(body);
//        accessToken = bodyJson.getString("access_token");
//        url = "https://api.weixin.qq.com/sns/userinfo" +
//                "?access_token=" + accessToken +
//                "&openid=" + openId + "&lang=zh_CN";
//        httpGet = new HttpGet(url);
//        response = httpClient.execute(httpGet);
//        body = CommonUtil.getBody(response.getEntity().getContent());
//        bodyJson = JSON.parseObject(body);
//        // 获取了用户信息后进行存储
//        log.info("the info of user is {}", bodyJson);
//        response.close();
//        // 登录成功，设置session
//        request.getSession().setAttribute("unionId", bodyJson.getString("union_id"));
//        return "登录成功";
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
        // 这里已经获取了用户的部分信息，可以在数据库中查询，如果已经记录过了，就没有必要进入后面的步骤了
        String openId = bodyJson.getString("openid");
        url ="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                +wxConfig.getAppId() +"&secret="+wxConfig.getAppSecret();
//        String accessToken = bodyJson.getString("access_token");
//        String refreshToken = bodyJson.getString("refresh_token");
//        url = "https://api.weixin.qq.com/sns/oauth2/refresh_token" +
//                "appid="+wxConfig.getAppId()+"grant_type=refresh_token&refresh_token="
//                +refreshToken;
        httpGet = new HttpGet(url);
        response = httpClient.execute(httpGet);
        body = CommonUtil.getBody(response.getEntity().getContent());
        bodyJson = JSON.parseObject(body);
        String accessToken = bodyJson.getString("access_token");
        log.info("access_token:{}",accessToken);
        url = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=" + accessToken +
                "&openid=" + openId + "&lang=zh_CN";
        httpGet = new HttpGet(url);
        response = httpClient.execute(httpGet);
        body = CommonUtil.getBody(response.getEntity().getContent());
        bodyJson = JSON.parseObject(body);
        // 获取了用户信息后进行存储
        log.info("the info of user is {}", bodyJson);
        response.close();
        // 登录成功，设置session
        request.getSession().setAttribute("unionId", bodyJson.getString("union_id"));
        return "登录成功";
    }



}
