package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.dao.CounselorBasicInfo;
import com.ecnu.rai.counsel.dao.SupervisorBasicInfo;
import com.ecnu.rai.counsel.dao.UserBasicInfo;
import com.ecnu.rai.counsel.entity.Arrange;
import com.ecnu.rai.counsel.response.DayNum;

import java.util.List;

public interface ArrangeService {

    void addArrange(Arrange arrange);

    Arrange findArrangeByID(Long id);

    Arrange updateArrange(Arrange arrange);

    List<Arrange> findArrangeByUser(Long user);

    List<Arrange> findArrangeByUserYearMonth(Long user, Integer year, Integer month);

    List<DayNum> findCounselorInfoByMonth(Integer year, Integer month);

    List<DayNum> findSupervisorInfoByMonth(Integer year, Integer month);

    List<CounselorBasicInfo> findCounselorListByDay(Integer year, Integer month, Integer day);

    List<SupervisorBasicInfo> findSupervisorListByDay(Integer year, Integer month, Integer day);

}
