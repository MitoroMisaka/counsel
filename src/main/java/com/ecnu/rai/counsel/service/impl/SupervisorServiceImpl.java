package com.ecnu.rai.counsel.service.impl;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervise;
import com.ecnu.rai.counsel.entity.Supervisor;
import com.ecnu.rai.counsel.mapper.CounselorMapper;
import com.ecnu.rai.counsel.mapper.SupervisorMapper;
import com.ecnu.rai.counsel.service.SupervisorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupervisorServiceImpl implements SupervisorService {

    @Autowired
    private SupervisorMapper supervisorMapper;

    @Autowired
    private CounselorMapper counselorMapper;

    @Override
    public Supervisor findSupervisorByID(Long id) {
        return supervisorMapper.findById(id);
    }

    @Override
    public void updateSupervisor(Supervisor supervisor) {
        supervisorMapper.updateSupervisor(supervisor);
    }

    public Page<Counselor> getAvailableCounselor(Long id, Integer page, Integer size, String order) {

        PageHelper.startPage(page, size, order);
        List<Supervise> superviselist = counselorMapper.findCounselors(id);
        List<Counselor> availableCounselorList = new ArrayList<>();
        for (Supervise supervise : superviselist) {
            availableCounselorList.add(counselorMapper.findById(supervise.getCounselorId()));
        }
        return new Page<>(new PageInfo<>(availableCounselorList));

    }

}
