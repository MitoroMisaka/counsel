package com.ecnu.rai.counsel.service.impl;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.dao.*;
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
        List<Long> availableSupervisorIdList = supervisorMapper.findSupervisorByCurrentTime(order);
        List<AvailableSupervisor> supervisorList = new ArrayList<>();
        List<Long> bindedSupervisorIdList = superviseMapper.findSupervisorByCounselorId(counselor_id);
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

        return new Page(supervisorList, page, size);
    }

    @Override
    public
    List<SupervisorBasicInfo> getFreeSupervisorList() {
        List<Long> freeSupervisorIdList = arrangeMapper.findFreeSupervisor();
        List<SupervisorBasicInfo> supervisorList = new ArrayList<>();
        for (Long freeSupervisorId : freeSupervisorIdList) {
            SupervisorBasicInfo supervisor = new SupervisorBasicInfo(supervisorMapper.findById(freeSupervisorId));
            supervisorList.add(supervisor);
        }
        return supervisorList;
    }

    @Override
    public
    List<SupervisorBasicInfo> getWorkingSupervisorList() {
        List<Long> workingSupervisorIdList = arrangeMapper.findWorkingSupervisor();
        List<SupervisorBasicInfo> supervisorList = new ArrayList<>();
        for (Long freeSupervisorId : workingSupervisorIdList) {
            SupervisorBasicInfo supervisor = new SupervisorBasicInfo(supervisorMapper.findById(freeSupervisorId));
            supervisorList.add(supervisor);
        }
        return supervisorList;
    }

    @Override
    public Page<Supervisor> getSupervisorList(Integer page, Integer size, String order) {
        PageHelper.startPage(page, size, order);
        List<Supervisor> supervisorList = supervisorMapper.getAll(order);
        PageInfo<Supervisor> pageInfo = new PageInfo<>(supervisorList);
        return new Page<>(new PageInfo<>(supervisorList));
    }

    @Override
    public Page<SupervisorSMInfo> getAllSupervisor(Integer page, Integer size, String order) {
        List<Supervisor> supervisors = supervisorMapper.getAll(order);
        List<SupervisorSMInfo> supervisorSMInfos = new ArrayList<>();
        SupervisorSMInfo supervisorSMInfo = null;
        for (Supervisor supervisor : supervisors) {
            supervisorSMInfo = new SupervisorSMInfo(supervisor);
            supervisorSMInfo.SetTotalNum(dialogueMapper.findTotalNumSupervisor(supervisor.getId()));
            supervisorSMInfo.SetTotalTime(dialogueMapper.findTotalBySupervisor(supervisor.getId()));
            supervisorSMInfo.setState(userMapper.findStateById(supervisor.getId()));
            List<Integer> Days = arrangeMapper.findDayArrange(supervisor.getId());
            for (Integer day : Days) {
                supervisorSMInfo.setTotalDay(day);
            }
            List<String> counselors = new ArrayList<>();
            List<Supervise> supervises = supervisorMapper.findCounselors(supervisor.getId());
            for (Supervise supervise : supervises) {
                counselors.add(counselorMapper.findById(supervise.getCounselorId()).getName());
            }
            supervisorSMInfo.SetCounselors(counselors);
            supervisorSMInfos.add(supervisorSMInfo);
        }

        return new Page(supervisorSMInfos, page, size);
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

    public Page<Counselor> getAvailableCounselor(Long supervisorId, Integer page, Integer size, String order) {

        List<Supervise> superviselist = counselorMapper.findCounselors(supervisorId, order);
        List<Counselor> availableCounselorList = new ArrayList<>();
        for (Supervise supervise : superviselist) {
            availableCounselorList.add(counselorMapper.findById(supervise.getCounselorId()));
        }
        return new Page(availableCounselorList, page, size);

    }
    
    

    @Override
    public Page<Supervisor> getAvailableSupervisor(Integer page, Integer size, String order) {
        List<Long> availableSupervisorIdList = arrangeMapper.findSupervisorByCurrentTime(order);
        List<Supervisor> supervisorList = new ArrayList<>();

        PageHelper.startPage(page, size, order);
        for (Long availableSupervisorId : availableSupervisorIdList) {
            supervisorList.add(supervisorMapper.findById(availableSupervisorId));
        }
        return new Page(supervisorList, page, size);
    }


    @Override
    public Page<HashMap<String, String>> getAvailableSupervisorByBusy(Integer page, Integer size, String order) {
        List<HashMap<String, String>> supervisorBusy= new ArrayList<>();
        List<Supervisor> supervisors = supervisorMapper.findAllSupervisorsOnline(order);
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
        return new Page(supervisorBusy, page, size);
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
