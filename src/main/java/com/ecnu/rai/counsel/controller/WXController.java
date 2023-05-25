package com.ecnu.rai.counsel.controller;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.config.WXConfig;
import com.ecnu.rai.counsel.dao.EditRequest;
import com.ecnu.rai.counsel.dao.NormalRequest;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.entity.Visitor;
import com.ecnu.rai.counsel.mapper.UserMapper;
import com.ecnu.rai.counsel.mapper.VisitorMapper;
import com.ecnu.rai.counsel.service.WXService;
import com.ecnu.rai.counsel.utils.CommonUtil;
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
import java.util.List;


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


    @PostMapping("/wx/edit")
    public Result edit(@RequestBody EditRequest request) {
        if(!token_check(request.getToken())){
            return Result.fail("Token过期或非法");
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
        Visitor u;
        if(!wxService.visitorExist(request.getOpenid())) {
            return Result.fail("该微信账号不存在");
        }
        else{
            u = wxService.findByopenid(request.getOpenid());
        }
        u.setName(request.getRealName());
        u.setUsername(request.getUsername());
        u.setPhone(request.getPhoneNumber());
        u.setEmergentContact(request.getEmergencyContactName());
        u.setEmergentPhone(request.getEmergencyContactPhoneNumber());
        u.setGender(request.getGender());
        u.setAvatar("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKKEsVP4BbdXrF0d2Jve5mpwHkTrQ3BPtZxg&usqp=CAU");
        visitorMapper.updateVisitor(u);
        Long id = wxService.findIdByopenid(request.getOpenid());
        User user = userMapper.findById(id);
        user.setName(request.getRealName());
        user.setUsername(request.getUsername());
        user.setRole("visitor");
        userMapper.updateUser(user);
        return Result.success("信息修改成功");
    }

    public static final String secret = "sdjhakdhajdklsl;o653632";


    public boolean token_check(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Date expirationTime = jwt.getExpiresAt();
            Date currentTime = new Date();

            if (expirationTime.before(currentTime)) {
                System.out.println("Token overtime");
                return false;
            }
        } catch (Exception e) {
            // Error decoding or verifying the token
            System.out.println("Error decoding/verifying the token: " + e.getMessage());
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            System.out.println("Signature verification successful");
            return true;
        } catch (JWTVerificationException e) {
            // Invalid signature
            System.out.println("Signature verification failed: " + e.getMessage());
            return false;
        }

    }
    @PostMapping("/wx/counselor")
    public Result getCounselor(NormalRequest request){
        if(!token_check(request.getToken())){
            System.out.println("Token过期或非法");
            return null;
        }
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        return Result.success("Successfully get avaliable counselors.",visitorMapper.findAvaliableCounselor(timestamp));

    }

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
        if(!wxService.visitorExist(openid)) {
            Visitor u = new Visitor();
            u.setOpenid(openid);
            wxService.insertNewVisitor(openid);
            token = TokenGenUtil.TokenGen(u);
//          u.setSession_key(session_key);
        }
        else {
            Visitor u = wxService.findByopenid(openid);
            u.setOpenid(openid);
            token = TokenGenUtil.TokenGen(u);
        }


return token;
     //   return "登录成功";
    }




}
