package com.ecnu.rai.counsel.service.impl;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.dao.AvailableCounselor;
import com.ecnu.rai.counsel.entity.*;
import com.ecnu.rai.counsel.mapper.ArrangeMapper;
import com.ecnu.rai.counsel.mapper.ConversationMapper;
import com.ecnu.rai.counsel.mapper.CounselorMapper;
import com.ecnu.rai.counsel.mapper.SupervisorMapper;
import com.ecnu.rai.counsel.service.CounselorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CounselorServiceImpl implements CounselorService {
    @Autowired
    private CounselorMapper counselorMapper;

    @Autowired
    private ConversationMapper conversationMapper;

    @Autowired
    private SupervisorMapper supervisorMapper;

    @Autowired
    private ArrangeMapper arrangeMapper;

    @Override
    public Counselor findCounselorByID(Long id) {
        Counselor counselor = counselorMapper.findById(id);

        List<Supervise> superviselist = counselorMapper.findSupervisors(id);
        List<Supervisor> availablesupervisorList = new ArrayList<>();
        for (Supervise supervise : superviselist) {
            availablesupervisorList.add(supervisorMapper.findById(supervise.getSupervisorId()));
        }
        counselor.setSupervisors(availablesupervisorList);
        return counselor;
    }

    @Override
    public void updateCounselor(Counselor counselor) {
        counselorMapper.updateCounselor(counselor);
    }

    @Override
    public void addCounselor(Counselor counselor) {
        counselorMapper.insertCounselor(counselor);
    }

    @Override
    public Page<AvailableCounselor> getAvailableCounselor(Integer page, Integer size, String order) {
        List<Long> availableCounselorIdList = arrangeMapper.findCounselorByCurrentTime();
        List<AvailableCounselor> counselorList = new ArrayList<>();

        PageHelper.startPage(page, size, order);
        for (Long availableCounselorId : availableCounselorIdList) {
            Counselor counselor = counselorMapper.findById(availableCounselorId);
            AvailableCounselor availableCounselor = new AvailableCounselor(counselor);
            Integer currentConsult = conversationMapper.getConsultNum(counselor.getName());
            if(currentConsult <= counselor.getMaxConsult()/2){
                availableCounselor.setBusy("空闲");
            }else if(currentConsult < counselor.getMaxConsult()) {
                availableCounselor.setBusy("繁忙");
            }
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            List<Conversation> conversations = conversationMapper.findGroupMsgByCounselorUser(counselor.getName(), user.getName());
            if(conversations.size() > 0){
                availableCounselor.setConsulted("已咨询");
            }else {
                availableCounselor.setConsulted("未咨询");
            }

            if(!currentConsult.equals(counselor.getMaxConsult())){
                counselorList.add(availableCounselor);
            }
        }
        return new Page<>(new PageInfo<>(counselorList));
    }



}
