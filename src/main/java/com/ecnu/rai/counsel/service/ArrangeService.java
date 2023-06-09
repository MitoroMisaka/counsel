package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.entity.Arrange;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.response.DayNum;

import java.util.List;

public interface ArrangeService {

    void addArrange(Arrange arrange);

    Arrange findArrangeByID(Long id);

    Arrange updateArrange(Arrange arrange);

    List<Arrange> findArrangeByUser(Long user);

    List<Arrange> findArrangeByUserYearMonth(Long user, Integer year, Integer month);

    List<DayNum> findArrangeCounselorInfoByYearMonthDay(Integer year, Integer month);

    List<DayNum> findArrangeSupervisorInfoByYearMonthDay(Integer year, Integer month);


}
