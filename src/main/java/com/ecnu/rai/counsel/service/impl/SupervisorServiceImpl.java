package com.ecnu.rai.counsel.service.impl;

import com.ecnu.rai.counsel.entity.Supervisor;
import com.ecnu.rai.counsel.mapper.SupervisorMapper;
import com.ecnu.rai.counsel.service.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupervisorServiceImpl implements SupervisorService {

    @Autowired
    private SupervisorMapper supervisorMapper;

    @Override
    public Supervisor findSupervisorByID(Long id) {
        return supervisorMapper.findById(id);
    }

    @Override
    public void updateSupervisor(Supervisor supervisor) {
        supervisorMapper.updateSupervisor(supervisor);
    }

}
