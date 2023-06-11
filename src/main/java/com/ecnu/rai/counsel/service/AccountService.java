package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.dao.UserBasicInfoResponse;
import com.ecnu.rai.counsel.entity.Admin;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.entity.Visitor;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervisor;

import java.util.List;

public interface AccountService {

    User findUserByID(Long id);

    Page<UserBasicInfoResponse> findUserList(Integer page, Integer size, String order);

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


