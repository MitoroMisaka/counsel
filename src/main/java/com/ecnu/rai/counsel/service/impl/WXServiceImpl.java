package com.ecnu.rai.counsel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.entity.Visitor;
import com.ecnu.rai.counsel.mapper.MyMapper;
import com.ecnu.rai.counsel.mapper.UserMapper;
import com.ecnu.rai.counsel.mapper.VisitorMapper;
import com.ecnu.rai.counsel.service.AccountService;
import com.ecnu.rai.counsel.service.WXService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Long id = userMapper.addVisitor(visitor);
        visitorMapper.insert(openid,id);
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
}

