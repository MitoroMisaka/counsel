package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.entity.Counselor;

public interface CounselorService {

    void addCounselor(Counselor counselor);

    Counselor findCounselorByID(Long id);

    void updateCounselor(Counselor counselor);

}
