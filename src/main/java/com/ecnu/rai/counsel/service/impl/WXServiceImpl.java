package com.ecnu.rai.counsel.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.entity.Visitor;
import com.ecnu.rai.counsel.mapper.MyMapper;
import com.ecnu.rai.counsel.mapper.UserMapper;
import com.ecnu.rai.counsel.mapper.VisitorMapper;
import com.ecnu.rai.counsel.service.AccountService;
import com.ecnu.rai.counsel.service.WXService;
import com.ecnu.rai.counsel.util.TokenUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class WXServiceImpl implements WXService {
    @Autowired
    VisitorMapper visitorMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    public void insertNewVisitor(String openid) {
        User visitor = new User();
        userMapper.addVisitor(visitor);
        Long id = visitor.getId();
        visitorMapper.insertVisitor(openid,id);
    }

    @Override
    public boolean visitorExist(String openid){
        return visitorMapper.ifVisitorExist(openid) != 0;
    }

    @Override
    public Visitor findByopenid(String openid) {
        return visitorMapper.findByopenid(openid);
    }

    @Override
    public Long findIdByopenid(String openid) {
        return visitorMapper.findIdbyopenid(openid);
    }

    @Override
    public boolean visitorState(String openid) {
        Visitor visitor = visitorMapper.findByopenid(openid);
        User user = userMapper.findById(visitor.getId());
        if(user.getState() == null)return false;
        if(user.getState() == 1)return true;
        else return false;
    }

    @Override
    public User finduserByopenid(String token) {
        DecodedJWT jwt = JWT.decode(token);
        String openid = jwt.getClaim("openid").asString();
        Long id = visitorMapper.findIdbyopenid(openid);
        return userMapper.findById(id);
    }

//    @Override
//    public List<HashMap<String, Object>> findAvaliableCounselorInfo(LocalDateTime localDateTime) {
//        List l = visitorMapper.findAvailableCounselor(localDateTime);
//        for(Object i:l){
//
//        }
//    }
}

