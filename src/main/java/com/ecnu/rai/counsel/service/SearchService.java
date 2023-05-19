package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.entity.Visitor;

public interface SearchService {
    Page<Visitor> searchUserByName(String content, int page, int size, String order);

}

