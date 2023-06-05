package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.entity.Supervisor;

public interface SupervisorService {

    Page<Supervisor> findSupervisorList(int page, int size, String order);

    void addSupervisor(Supervisor supervisor);

    Supervisor findSupervisorByID(Long id);

    void updateSupervisor(Supervisor supervisor);

}
