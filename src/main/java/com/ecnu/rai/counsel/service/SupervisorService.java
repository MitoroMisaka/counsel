package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervisor;

public interface SupervisorService {

    Supervisor findSupervisorByID(Long id);

    void updateSupervisor(Supervisor supervisor);

    Page<Counselor> getAvailableCounselor(Long id, Integer page, Integer size, String order);

}
