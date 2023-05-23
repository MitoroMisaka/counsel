package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.mapper.CounselorMapper;
import com.ecnu.rai.counsel.mapper.UserMapper;
import com.ecnu.rai.counsel.service.CounselorService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/counselor")
public class CounselorController {

    @Autowired
    private CounselorService counselorService;

    @Autowired
    private CounselorMapper counselorMapper;

    @Autowired
    UserMapper userMapper;

    @PostMapping("/info")
    @ApiOperation("获取用户列表")
    public Result getCounselorInfo() {

        return Result.success("获取成功", userMapper.getUserList());
    }

}
