package com.ecnu.rai.counsel.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.ecnu.rai.counsel.common.Page;

import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.entity.Visitor;
import com.ecnu.rai.counsel.mapper.MyMapper;
import com.ecnu.rai.counsel.mapper.UserMapper;
import com.ecnu.rai.counsel.service.AccountService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MyMapper myMapper;

    @Override
    public User findUserByID(Long id) {
        return userMapper.selectById(id);
    }

    public User findUserByUsername(String username) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", username);
        return userMapper.selectOne(wrapper);
    }

        public Page<Visitor> findAllUsers (int page, int size){
            PageHelper.startPage(page, size);
            List<Visitor> users = myMapper.findAllUser();
            return new Page<>(new PageInfo<>(users));
        }

        @Override
        public User updateUser(Long id, User user) {
            User existingUser = userMapper.findById(id);
            if (existingUser == null) {
                throw new RuntimeException("User not found with ID: " + id);
            }

            // Update the user properties
            existingUser.setName(user.getName());
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            existingUser.setRole(user.getRole());

            // Perform the update in the database
            userMapper.update(existingUser);

            return existingUser;
        }
    }

