package com.ecnu.rai.counsel.service.impl;

import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.mapper.MyMapper;
import com.ecnu.rai.counsel.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private MyMapper myMapper;

    @Override
    public List<User> findAllUsers() {
        return myMapper.findAllUser();
    }
}

