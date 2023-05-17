package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.entity.Visitor;

public interface AccountService {

    User findUserByID(Long id);

    User findUserByUsername(String username);

    User updateUser(Long id, User user);

}


