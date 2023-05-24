package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.entity.Arrange;
import com.ecnu.rai.counsel.entity.User;

import java.util.List;

public interface ArrangeService {

    Arrange findArrangeByID(Long id);

    Arrange updateArrange(Arrange arrange);

    List<Arrange> findArrangeByUser(Long user);

    List<Arrange> findArrangeByUserYearMonth(Long user, Integer year, Integer month);

}
