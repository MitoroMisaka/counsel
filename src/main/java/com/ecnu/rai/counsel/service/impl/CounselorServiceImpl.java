package com.ecnu.rai.counsel.service.impl;

import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.mapper.CounselorMapper;
import com.ecnu.rai.counsel.mapper.UserMapper;
import com.ecnu.rai.counsel.service.CounselorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CounselorServiceImpl implements CounselorService {

    @Autowired
    private CounselorMapper counselorMapper;

    @Override
    public Counselor findCounselorByID(Long id) {
        return counselorMapper.selectById(id);
    }

}
