package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.dao.CounselorMonthlyStar;
import com.ecnu.rai.counsel.dao.CounselorMonthlyWork;
import com.ecnu.rai.counsel.dao.AvailableCounselor;
import com.ecnu.rai.counsel.dao.CounselorSMInfo;
import com.ecnu.rai.counsel.entity.Counselor;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public interface CounselorService {

    Page<Counselor> getCounselorList(Integer page, Integer size, String order);

    Counselor findCounselorByID(Long id);

    List<CounselorMonthlyWork> getCounselorRankingByWork(Integer len);

    List<CounselorMonthlyStar> getCounselorRankingByStar(Integer len);

    void addCounselor(Counselor counselor);

    void updateCounselor(Counselor counselor);

    Page<AvailableCounselor> getAvailableCounselor(Integer page, Integer size, String order, String token);

    List<CounselorSMInfo> getAllCounselor();

    Page<HashMap<String,String>> getAvailableCounselorByBusy(Integer page, Integer size, String order);

    HashMap<String, Integer> getBasicStatInfoByCounselor(Long counselorId);

    List<TreeMap<String, Integer>> getNumByWeek();

    List<TreeMap<String, Integer>> getNumByHours();
}
