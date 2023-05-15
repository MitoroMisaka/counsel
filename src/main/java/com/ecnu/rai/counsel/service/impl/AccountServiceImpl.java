package com.ecnu.rai.counsel.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.ecnu.rai.counsel.common.Page;

import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.mapper.MyMapper;
import com.ecnu.rai.counsel.mapper.UserMapper;
import com.ecnu.rai.counsel.service.AccountService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserByID(Long id) {
        return userMapper.selectById(id);
    }

    public User findUserByUsername(String username) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username",username);
        return userMapper.selectOne(wrapper);

    public Page<User> findAllUsers(int page, int size) {
        PageHelper.startPage(page, size);
        List<User> users = myMapper.findAllUser();
        return new Page<>(new PageInfo<>(users));
    }
}

