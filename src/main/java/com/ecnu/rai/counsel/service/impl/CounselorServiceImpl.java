package com.ecnu.rai.counsel.service.impl;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervise;
import com.ecnu.rai.counsel.entity.Supervisor;
import com.ecnu.rai.counsel.mapper.ArrangeMapper;
import com.ecnu.rai.counsel.mapper.CounselorMapper;
import com.ecnu.rai.counsel.mapper.SupervisorMapper;
import com.ecnu.rai.counsel.service.CounselorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CounselorServiceImpl implements CounselorService {
    @Autowired
    private CounselorMapper counselorMapper;

    @Autowired
    private SupervisorMapper supervisorMapper;

    @Autowired
    private ArrangeMapper arrangeMapper;

    @Override
    public Counselor findCounselorByID(Long id) {
        Counselor counselor = counselorMapper.findById(id);

        List<Supervise> superviselist = counselorMapper.findSupervisors(id);
        List<Supervisor> availablesupervisorList = new ArrayList<>();
        for (Supervise supervise : superviselist) {
            availablesupervisorList.add(supervisorMapper.findById(supervise.getSupervisorId()));
        }
        counselor.setSupervisors(availablesupervisorList);
        return counselor;
    }

    @Override
    public void updateCounselor(Counselor counselor) {
        counselorMapper.updateCounselor(counselor);
    }

    @Override
    public Page<Counselor> getAvailableCounselor(Integer page, Integer size, String order) {
        List<Long> availableCounselorIdList = arrangeMapper.findCounselorByCurrentTime();
        List<Counselor> counselorList = new ArrayList<>();

        PageHelper.startPage(page, size, order);
        for (Long availableCounselorId : availableCounselorIdList) {
            counselorList.add(counselorMapper.findById(availableCounselorId));
        }
        return new Page<>(new PageInfo<>(counselorList));
    }

}
