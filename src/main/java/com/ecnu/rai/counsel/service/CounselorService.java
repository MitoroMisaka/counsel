package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.dao.AvailableCounselor;
import com.ecnu.rai.counsel.entity.Counselor;
import org.springframework.web.bind.annotation.RequestParam;

public interface CounselorService {

    void addCounselor(Counselor counselor);

    Counselor findCounselorByID(Long id);

    void updateCounselor(Counselor counselor);

    Page<AvailableCounselor> getAvailableCounselor(Integer page, Integer size, String order);

}
