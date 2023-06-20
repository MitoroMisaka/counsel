package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.dao.CounselorSMInfo;
import com.ecnu.rai.counsel.dao.SupervisorSMInfo;
import com.ecnu.rai.counsel.dao.UserBasicInfo;
import com.ecnu.rai.counsel.entity.Admin;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.entity.Visitor;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervisor;

public interface AccountService {

    User findUserByID(Long id);

    Page<UserBasicInfo> findUserList(Integer page, Integer size, String order);

    Page<CounselorSMInfo> findCounselorList(Integer page, Integer size, String order);

    Page<SupervisorSMInfo> findSupervisorList(Integer page, Integer size, String order);

    boolean isPhoneUsedByOtherCounselor(Long id,String phone);

    boolean isPhoneUsedByOtherSupervisor(Long id, String phone);

    boolean isEmailUsedByOtherCounselor(Long id, String email);

    User findUserByUsername(String username);

    User updateUser(Long id, User user);

    Visitor updateVisitor(Long id, Visitor visitor);

    //update Admin
    Admin updateAdmin(Long id, Admin admin);

    //update Counselor
    Counselor updateCounselor(Long id, Counselor counselor);

    //update Supervisor
    Supervisor updateSupervisor(Long id, Supervisor supervisor);

    boolean isUsernameUsedByOtherUser(String username);

    boolean isUsernameUsedByOtherCounselor(String username);

    boolean isUsernameUsedByOtherSupervisor(String username);
}


