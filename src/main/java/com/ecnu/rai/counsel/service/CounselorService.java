package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.dao.CounselorMonthlyStar;
import com.ecnu.rai.counsel.dao.CounselorMonthlyWork;
import com.ecnu.rai.counsel.dao.AvailableCounselor;
import com.ecnu.rai.counsel.entity.Counselor;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CounselorService {

    Page<Counselor> getCounselorList(Integer page, Integer size, String order);

    Counselor findCounselorByID(Long id);

    List<CounselorMonthlyWork> getCounselorRankingByWork(Integer len);

    List<CounselorMonthlyStar> getCounselorRankingByStar(Integer len);

    void addCounselor(Counselor counselor);

    void updateCounselor(Counselor counselor);

    Page<AvailableCounselor> getAvailableCounselor(Integer page, Integer size, String order, String token);

}
