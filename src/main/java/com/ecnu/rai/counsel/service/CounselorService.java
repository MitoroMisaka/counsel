package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.dao.*;
import com.ecnu.rai.counsel.entity.Counselor;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public interface CounselorService {

    Page<Counselor> getCounselorList(Integer page, Integer size, String order);

    Counselor findCounselorByID(Long id);

    List<CounselorBasicInfo> getFreeCounselorList();

    List<CounselorBasicInfo> getWorkingCounselorList();

    List<CounselorMonthlyWork> getCounselorRankingByWork(Integer len);

    List<CounselorMonthlyStar> getCounselorRankingByStar(Integer len);

    void addCounselor(Counselor counselor);

    void updateCounselor(Counselor counselor);

    Page<AvailableCounselor> getAvailableCounselor(Integer page, Integer size, String order, String token);

    Page<CounselorSMInfo> getAllCounselor(Integer page, Integer size, String order);

    Page<HashMap<String,String>> getAvailableCounselorByBusy(Integer page, Integer size, String order);

    HashMap<String, Integer> getBasicStatInfoByCounselor(Long counselorId);

    List<TreeMap<String, Integer>> getNumByWeek();

    List<TreeMap<String, Integer>> getNumByHours();
}
