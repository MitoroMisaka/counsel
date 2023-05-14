package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AccountService {
    Page<User> findAllUsers(int page, int size);
}


