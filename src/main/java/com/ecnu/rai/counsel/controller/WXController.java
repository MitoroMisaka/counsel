package com.ecnu.rai.counsel.controller;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.LocalDateTime;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.config.WXConfig;
import com.ecnu.rai.counsel.dao.EditRequest;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.entity.Visitor;
import com.ecnu.rai.counsel.mapper.UserMapper;
import com.ecnu.rai.counsel.mapper.VisitorMapper;
import com.ecnu.rai.counsel.service.WXService;
import com.ecnu.rai.counsel.util.TokenUtil;
import com.ecnu.rai.counsel.util.VerifyCodeGenUtil;
import com.ecnu.rai.counsel.utils.CommonUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ecnu.rai.counsel.util.TokenGenUtil;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


@Slf4j
@RestController
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
    @ApiOperation("编辑微信用户")
    public Result edit(@RequestBody EditRequest request) {
        if(!TokenUtil.token_check(request.getToken())){
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
        DecodedJWT jwt = JWT.decode(request.getToken());
        String openid = jwt.getClaim("openid").asString();
        if(!wxService.visitorExist(openid)) {
            return Result.fail("该微信账号不存在");
        }
        else if(!wxService.visitorState(openid))
        {
            return Result.fail("该账号已封禁！");
        }
        else{
            u = wxService.findByopenid(openid);
        }
        if (!(Objects.equals(u.getPhone(), request.getPhoneNumber()))){
            if(visitorMapper.ifPhoneExist(request.getPhoneNumber())==0)
            {

                u.setPhone(request.getPhoneNumber());
            }
            else
            {
                return Result.fail("该号码已经注册过");
            }
        }

        u.setName(request.getRealName());
        u.setUsername(request.getUserName());
        u.setEmergentContact(request.getEmergencyContactName());
        u.setEmergentPhone(request.getEmergencyContactPhoneNumber());
        u.setRole("VISITOR");
        u.setTitle(request.getTitle());
        u.setDepartment(request.getDepartment());
        u.setGender(request.getGender());
        u.setAvatar("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKKEsVP4BbdXrF0d2Jve5mpwHkTrQ3BPtZxg&usqp=CAU");
        visitorMapper.updateVisitor(u);
        Long id = wxService.findIdByopenid(openid);
        User user = userMapper.findById(id);
        user.setName(request.getRealName());
        user.setUsername(request.getUserName());
        user.setRole("VISITOR");
        user.setState(1);
        userMapper.updateUser(user);
        return Result.success("信息修改成功");
    }
    @PostMapping("/wx/delete")
    @ApiOperation("删除用户")
    public Result deleteVisitor(@RequestParam("token") String token){
        if(!TokenUtil.token_check(token)){
            System.out.println("Token过期或非法");
            return Result.fail("Token过期或非法");

        }
        else{
            DecodedJWT jwt = JWT.decode(token);
            String openid = jwt.getClaim("openid").asString();
            Long id = wxService.findIdByopenid(openid);
            if(!wxService.visitorExist(openid)){
                return Result.fail("用户不存在");
            }
            else{
                visitorMapper.deleteVisitor(openid);
                userMapper.deleteById(id);
                return Result.success("账号已删除！");
            }
        }
    }


    @PostMapping("/wx/info")
    @ApiOperation("编辑用户")
    public Result findVisitor(@RequestParam("token") String token){
        if(!TokenUtil.token_check(token)){
            System.out.println("Token过期或非法");
            return Result.fail("Token过期或非法");

        }
        else{
            DecodedJWT jwt = JWT.decode(token);
            String openid = jwt.getClaim("openid").asString();
            Long id = wxService.findIdByopenid(openid);
            if(!wxService.visitorExist(openid)){
                return Result.fail("用户不存在");
            }
            else{
                Visitor v = visitorMapper.findByopenid(openid);
                return Result.success("success！",v);
            }
        }
    }

    @PostMapping("/wx/counselor")
    @ApiOperation("获取可用咨询师，暂时弃用")
    public Object getCounselor(@RequestParam("token") String token){
        if(!TokenUtil.token_check(token)){
            System.out.println("Token过期或非法");
            return null;
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        return visitorMapper.findAvailableCounselor(localDateTime);

    }



    @ResponseBody
    @CrossOrigin
    @RequestMapping("/wx/login")
    @ApiOperation("微信登录")
    public Result login(HttpServletRequest request) throws IOException {

        String code = request.getParameter("code");
        if (code == null){
            log.error("用户取消登录");
            return Result.fail("用户取消登录");
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
        if(openid==null)
            return Result.fail("code已过期");
        System.out.println(openid);
        String token = "";
        if(!wxService.visitorExist(openid)) {
            Visitor u = new Visitor();
            u.setOpenid(openid);
            wxService.insertNewVisitor(openid);
            token = TokenGenUtil.TokenGen(u);
        }
        else {
            Visitor u = wxService.findByopenid(openid);
            System.out.println(u.getId());
            User user = userMapper.findById(u.getId());
            if(user.getState()==0)
            {
                System.out.println("账号已禁用");
                return Result.fail("账号已禁用");
            }
            token = TokenGenUtil.TokenGen(u);
        }


return Result.success(token);
     //   return "登录成功";
    }





}
