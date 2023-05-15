package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.entity.User;

public interface SearchService {
    Page<User> searchUserByName(String content, int page, int size,String order);
}