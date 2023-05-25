package com.ecnu.rai.counsel.service.impl;

import com.ecnu.rai.counsel.entity.Arrange;
import com.ecnu.rai.counsel.mapper.ArrangeMapper;
import com.ecnu.rai.counsel.service.ArrangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArrangeServiceImpl implements ArrangeService {

    @Autowired
    private ArrangeMapper arrangeMapper;

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
}
