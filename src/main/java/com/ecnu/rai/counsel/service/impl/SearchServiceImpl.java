package com.ecnu.rai.counsel.service.impl;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.entity.Visitor;
import com.ecnu.rai.counsel.mapper.MyMapper;
import com.ecnu.rai.counsel.service.SearchService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    MyMapper myMapper;

    @Override
    public Page<Visitor> searchUserByName(String content, int page, int size, String order) {
        // 构造分页参数
        PageHelper.startPage(page, size, order);

        // 调用 Mapper 层的查询所有用户方法
        List<Visitor> userList = myMapper.findAllUser();

        // 过滤出符合搜索条件的用户
        List<Visitor> searchResults = userList.stream()

                .filter(user -> user.getUsername().contains(content))
                .collect(Collectors.toList());

        // 返回分页后的结果
        return new Page<>(new PageInfo<>(searchResults));
    }



}

