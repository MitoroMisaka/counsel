package com.ecnu.rai.counsel.service.impl;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.dao.AvailableSupervisor;
import com.ecnu.rai.counsel.dao.CounselorSMInfo;
import com.ecnu.rai.counsel.dao.SupervisorSMInfo;
import com.ecnu.rai.counsel.dao.UserSig;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervise;
import com.ecnu.rai.counsel.entity.Supervisor;
import com.ecnu.rai.counsel.entity.Usersig;
import com.ecnu.rai.counsel.mapper.*;
import com.ecnu.rai.counsel.service.SupervisorService;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class SupervisorServiceImpl implements SupervisorService {

    @Autowired
    private SupervisorMapper supervisorMapper;

    @Autowired
    private CounselorMapper counselorMapper;

    @Autowired
    private ConversationMapper conversationMapper;

    @Autowired
    private DialogueMapper dialogueMapper;

    @Autowired
    private ArrangeMapper arrangeMapper;

    @Autowired
    private SuperviseMapper superviseMapper;

    @Autowired
    private UserSigMapper userSigMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void addCounselors(Long id, Long counselorIds) {
        superviseMapper.makeSupervise(id, counselorIds);
    }

    @Override
    public Page<AvailableSupervisor> getAvailableSupervisorList(Long counselor_id, Integer page, Integer size, String order){
        List<Long> availableSupervisorIdList = supervisorMapper.findSupervisorByCurrentTime();
        List<AvailableSupervisor> supervisorList = new ArrayList<>();
        List<Long> bindedSupervisorIdList = superviseMapper.findSupervisorByCounselorId(counselor_id);
        PageHelper.startPage(page, size, order);
        for (Long availableSupervisorId : availableSupervisorIdList) {
            if(bindedSupervisorIdList.contains(availableSupervisorId)){
                Supervisor supervisor= supervisorMapper.findById(availableSupervisorId);
                AvailableSupervisor availableSupervisor = new AvailableSupervisor(supervisor);
                if(userSigMapper.getUserSigByName(supervisor.getName())!=null){
                    Usersig userSig = userSigMapper.getUserSigByName(supervisor.getName());
                    availableSupervisor.setUserSig(userSig.getUsersig());
                    availableSupervisor.setImid(userSig.getImid());
                }
                else {
                    continue;
                }
                supervisorList.add(availableSupervisor);
            }
        }

        return new Page<>(new PageInfo<>(supervisorList));
    }

    @Override
    public Page<Supervisor> getSupervisorList(Integer page, Integer size, String order) {
        PageHelper.startPage(page, size, order);
        List<Supervisor> supervisorList = supervisorMapper.getAll();
        PageInfo<Supervisor> pageInfo = new PageInfo<>(supervisorList);
        System.out.println(pageInfo.getPageNum() +" "+ pageInfo.getPageSize() + " "+ pageInfo.getSize());
        return new Page<>(new PageInfo<>(supervisorList));
    }

    @Override
    public List<SupervisorSMInfo> getAllSupervisor() {
        List<Supervisor> supervisors = supervisorMapper.getAll();
        List<SupervisorSMInfo> supervisorSMInfos = new ArrayList<>();
        for(Supervisor supervisor:supervisors)
        {
            SupervisorSMInfo supervisorSMInfo = new SupervisorSMInfo(supervisor);
            supervisorSMInfo.SetTotalNum(dialogueMapper.findTotalNumSupervisor(supervisor.getId()));
            supervisorSMInfo.SetTotalTime(dialogueMapper.findTotalBySupervisor(supervisor.getId()));
            supervisorSMInfo.setState(userMapper.findStateById(supervisor.getId()));
            List<Integer> Days = arrangeMapper.findDayArrange(supervisor.getId());
            for(Integer day:Days)
            {
                supervisorSMInfo.setTotalDay(day);
            }
            List<String> counselors = new ArrayList<>();
            List<Supervise> supervises = supervisorMapper.findCounselors(supervisor.getId());
            for(Supervise supervise:supervises)
            {
                counselors.add(counselorMapper.findById(supervise.getCounselorId()).getName());
            }
            supervisorSMInfo.SetCounselors(counselors);
            supervisorSMInfos.add(supervisorSMInfo);
        }


        return supervisorSMInfos;
    }
    
    @Override
    public void addSupervisor(Supervisor supervisor) {
        supervisorMapper.insertSupervisor(supervisor);
    }

    @Override
    public Supervisor findSupervisorByID(Long id) {
        return supervisorMapper.findById(id);
    }

    @Override
    public void updateSupervisor(Supervisor supervisor) {
        supervisorMapper.updateSupervisor(supervisor);
    }

    public Page<Counselor> getAvailableCounselor(Long id, Integer page, Integer size, String order) {

        PageHelper.startPage(page, size, order);
        List<Supervise> superviselist = counselorMapper.findCounselors(id);
        List<Counselor> availableCounselorList = new ArrayList<>();
        for (Supervise supervise : superviselist) {
            availableCounselorList.add(counselorMapper.findById(supervise.getCounselorId()));
        }
        return new Page<>(new PageInfo<>(availableCounselorList));

    }
    
    

    @Override
    public Page<Supervisor> getAvailableSupervisor(Integer page, Integer size, String order) {
        List<Long> availableSupervisorIdList = arrangeMapper.findSupervisorByCurrentTime();
        List<Supervisor> supervisorList = new ArrayList<>();

        PageHelper.startPage(page, size, order);
        for (Long availableSupervisorId : availableSupervisorIdList) {
            supervisorList.add(supervisorMapper.findById(availableSupervisorId));
        }
        return new Page<>(new PageInfo<>(supervisorList));
    }

    @Override
    public Page<HashMap<String, String>> getAvailableSupervisorByBusy(Integer page, Integer size, String order) {
        List<Supervisor> supervisors = supervisorMapper.getAll();
        List<HashMap<String, String>> supervisorBusy= new ArrayList<>();
        for(Supervisor supervisor:supervisors)
        {
            Integer currentConsult = conversationMapper.getConsultNum(supervisor.getName());
            if(currentConsult <= 5){
                HashMap<String, String> h = new HashMap<>();
                h.put("supervisor",supervisor.getName());
                h.put("status","空闲");
                supervisorBusy.add(h);
            }else if(currentConsult < supervisor.getMaxConsult()) {
                HashMap<String, String> h = new HashMap<>();
                h.put("supervisor",supervisor.getName());
                h.put("status","繁忙");
                supervisorBusy.add(h);
            }
        }
        PageHelper.startPage(page, size, order);
        return new Page<>(new PageInfo<>(supervisorBusy));
    }

    @Override
    public HashMap<String, Integer> getBasicStatInfoBySupervisor(Long supervisorId) {
        Integer totalNumCounselor = conversationMapper.findTodayNumBySupervisor(supervisorId);
        Integer totalTime = conversationMapper.findTodayTotalBySupervisor(supervisorId);
        Integer curNum = conversationMapper.findCurrentNumBySupervisor(supervisorId);
        Integer totalNumSupervisor = dialogueMapper.findTodayNumBySupervisor(supervisorId);
        totalNumCounselor = totalNumCounselor + curNum;
        HashMap<String, Integer> h = new HashMap<>();
        h.put("totalNumCounselor",totalNumCounselor);
        h.put("totalTime",totalTime);
        h.put("curNum",curNum);
        h.put("totalNumSupervisor",totalNumSupervisor);
        return h;

    }

}
