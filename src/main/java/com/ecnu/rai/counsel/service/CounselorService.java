package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervisor;

public interface CounselorService {

    Counselor findCounselorByID(Long id);

    void updateCounselor(Counselor counselor);

    Page<Supervisor> getAvailableSupervisor(Long id, Integer page, Integer size, String order);

}
