package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.entity.Supervisor;

public interface SupervisorService {

    Supervisor findSupervisorByID(Long id);

    void updateSupervisor(Supervisor supervisor);

}
