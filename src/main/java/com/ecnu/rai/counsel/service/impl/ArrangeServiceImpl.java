package com.ecnu.rai.counsel.service.impl;

import com.ecnu.rai.counsel.dao.CounselorBasicInfo;
import com.ecnu.rai.counsel.dao.SupervisorBasicInfo;
import com.ecnu.rai.counsel.dao.UserBasicInfo;
import com.ecnu.rai.counsel.entity.Arrange;
import com.ecnu.rai.counsel.mapper.ArrangeMapper;
import com.ecnu.rai.counsel.mapper.CounselorMapper;
import com.ecnu.rai.counsel.mapper.SupervisorMapper;
import com.ecnu.rai.counsel.response.DayNum;
import com.ecnu.rai.counsel.service.ArrangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArrangeServiceImpl implements ArrangeService {

    @Autowired
    private ArrangeMapper arrangeMapper;

    @Autowired
    private CounselorMapper counselorMapper;

    @Autowired
    private SupervisorMapper supervisorMapper;

    @Override
    public void addArrange(Arrange arrange) {
        arrangeMapper.insertArrange(arrange);
    }

    @Override
    public Arrange findArrangeByID(Long id) {
        return arrangeMapper.findById(id);
    }

    @Override
    public Arrange updateArrange(Arrange arrange) {
        Arrange existingArrange = arrangeMapper.findById(arrange.getId());
        if (existingArrange == null) {
            throw new RuntimeException("Arrange not found with ID: " + arrange.getId());
        }

        // Update the supervisor properties
        existingArrange.setRole(arrange.getRole());
        existingArrange.setCreateTime(arrange.getCreateTime());
        existingArrange.setCreator(arrange.getCreator());
        existingArrange.setLastUpdateTime(arrange.getLastUpdateTime());
        existingArrange.setLastUpdater(arrange.getLastUpdater());
        existingArrange.setYear(arrange.getYear());
        existingArrange.setMonth(arrange.getMonth());
        existingArrange.setDay(arrange.getDay());
        existingArrange.setStartTime(arrange.getStartTime());
        existingArrange.setEndTime(arrange.getEndTime());
        existingArrange.setUser(arrange.getUser());
        existingArrange.setRole(arrange.getRole());
        existingArrange.setWeekday(arrange.getWeekday());
        existingArrange.setLocalDate(arrange.getLocalDate());

        // Perform the update in the database
        arrangeMapper.updateArrange(existingArrange);

        return existingArrange;
    }

    @Override
    public List<Arrange> findArrangeByUser(Long user) {
        return arrangeMapper.findByUser(user);
    }

    @Override
    public List<Arrange> findArrangeByUserYearMonth(Long user, Integer year, Integer month) {
        return arrangeMapper.findByUserYearMonth(user, year, month);
    }

    public List<DayNum> findCounselorInfoByMonth(Integer year, Integer month) {
        return arrangeMapper.findCounselorInfoByMonth(year, month);
    }

    public List<DayNum> findSupervisorInfoByMonth(Integer year, Integer month) {
        return arrangeMapper.findSupervisorInfoByMonth(year, month);
    }

    public List<CounselorBasicInfo> findCounselorListByDay(Integer year, Integer month, Integer day) {
        List<Long> CounselorIdList = arrangeMapper.findCounselorIDListByDay(year, month, day);
        List<CounselorBasicInfo> CounselorBasicInfoList = new ArrayList<>();
        for(Long id : CounselorIdList) {
            CounselorBasicInfoList.add(new CounselorBasicInfo(counselorMapper.findById(id)));
        }
        return CounselorBasicInfoList;
    }

    public List<SupervisorBasicInfo> findSupervisorListByDay(Integer year, Integer month, Integer day) {
        List<Long> SupervisorIdList = arrangeMapper.findSupervisorIDListByDay(year, month, day);
        List<SupervisorBasicInfo> SupervisorBasicInfoList = new ArrayList<>();
        for(Long id : SupervisorIdList) {
            SupervisorBasicInfoList.add(new SupervisorBasicInfo(supervisorMapper.findById(id)));
        }
        return SupervisorBasicInfoList;
    }

    public List<SupervisorBasicInfo> findSupervisorFreeListByDay(Integer year, Integer month, Integer day) {
        List<Long> SupervisorIdList = arrangeMapper.findSupervisorIDListByDay(year, month, day);
        List<Long> SupervisorList = supervisorMapper.findId();
        SupervisorList.removeAll(SupervisorIdList);
        List<SupervisorBasicInfo> SupervisorBasicInfoList = new ArrayList<>();
        for(Long id : SupervisorIdList) {
            SupervisorBasicInfoList.add(new SupervisorBasicInfo(supervisorMapper.findById(id)));
        }
        return SupervisorBasicInfoList;
    }

    @Override
    public List<CounselorBasicInfo> findCounselorFreeListByDay(Integer year, Integer month, Integer day) {
        List<Long> CounselorIdList = arrangeMapper.findCounselorIDListByDay(year, month, day);
        List<Long> CounselorList = counselorMapper.findId();
        CounselorList.removeAll(CounselorIdList);
        List<CounselorBasicInfo> CounselorBasicInfoList = new ArrayList<>();
        for(Long id : CounselorIdList) {
            CounselorBasicInfoList.add(new CounselorBasicInfo(counselorMapper.findById(id)));
        }
        return CounselorBasicInfoList;
    }

}
