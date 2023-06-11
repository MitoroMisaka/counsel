package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervisor;

public interface SupervisorService {


    Page<Supervisor> getAvailableSupervisorList(Integer page, Integer size, String order);

    Page<Supervisor> getSupervisorList(Integer page, Integer size, String order);

    void addSupervisor(Supervisor supervisor);

    Supervisor findSupervisorByID(Long id);

    void updateSupervisor(Supervisor supervisor);

    Page<Counselor> getAvailableCounselor(Long id, Integer page, Integer size, String order);

    Page<Supervisor> getAvailableSupervisor(Integer page, Integer size, String order);

}
