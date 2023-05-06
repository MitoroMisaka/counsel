package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AccountService {
    List<User> findAllUsers();
}


