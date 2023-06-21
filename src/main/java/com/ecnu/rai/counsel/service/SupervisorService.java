package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.dao.AvailableSupervisor;
import com.ecnu.rai.counsel.dao.CounselorSMInfo;
import com.ecnu.rai.counsel.dao.SupervisorSMInfo;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervisor;

import java.util.HashMap;
import java.util.List;

public interface SupervisorService {

    void addCounselors(Long id, Long counselorIds);

    Page<AvailableSupervisor> getAvailableSupervisorList(Long counselor_id, Integer page, Integer size, String order);

    Page<Supervisor> getSupervisorList(Integer page, Integer size, String order);

    void addSupervisor(Supervisor supervisor);

    Supervisor findSupervisorByID(Long id);

    void updateSupervisor(Supervisor supervisor);

    Page<Counselor> getAvailableCounselor(Long id, Integer page, Integer size, String order);

    Page<Supervisor> getAvailableSupervisor(Integer page, Integer size, String order);

    List<SupervisorSMInfo> getAllSupervisor();

    Page<HashMap<String, String>> getAvailableSupervisorByBusy(Integer page, Integer size, String order);

    HashMap<String, Integer> getBasicStatInfoBySupervisor(Long supervisorId);

}
