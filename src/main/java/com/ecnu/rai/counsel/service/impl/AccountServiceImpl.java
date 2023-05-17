package com.ecnu.rai.counsel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.mapper.MyMapper;
import com.ecnu.rai.counsel.mapper.UserMapper;
import com.ecnu.rai.counsel.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        existingUser.setAvatar(user.getAvatar());
        existingUser.setPhone(user.getPhone());
        existingUser.setGender(user.getGender());
        existingUser.setDepartment(user.getDepartment());
        existingUser.setEmergentContact(user.getEmergentContact());
        existingUser.setEmergentPhone(user.getEmergentPhone());

        // Perform the update in the database
        userMapper.update(existingUser);

        return existingUser;
    }
}

