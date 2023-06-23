package com.ecnu.rai.counsel.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.ecnu.rai.counsel.common.Page;

import com.ecnu.rai.counsel.dao.CounselorSMInfo;
import com.ecnu.rai.counsel.dao.SupervisorSMInfo;
import com.ecnu.rai.counsel.dao.UserBasicInfo;
import com.ecnu.rai.counsel.entity.*;
import com.ecnu.rai.counsel.mapper.*;
import com.ecnu.rai.counsel.service.AccountService;
import com.ecnu.rai.counsel.service.CounselorService;
import com.ecnu.rai.counsel.service.SupervisorService;
import com.ecnu.rai.counsel.util.PasswordUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VisitorMapper visitorMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private CounselorMapper counselorMapper;

    @Autowired
    private SupervisorMapper supervisorMapper;

    @Autowired
    private ConversationMapper conversationMapper;

    @Autowired
    private DialogueMapper dialogueMapper;

    @Override
    public User findUserByID(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public Page<UserBasicInfo> findUserList(Integer page, Integer size, String order) {
        PageHelper.startPage(page, size, order);
        List<UserBasicInfo> userList = userMapper.findAllUsers();
        return new Page<>(new PageInfo<>(userList));
    }

    public User findUserByUsername(String username) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User updateUser(Long id, User user) {
        User existingUser = userMapper.findById(id);
        if (existingUser == null) {
            throw new RuntimeException("User not found with ID: " + id);
        }

        // Update the user properties
        existingUser.setName(user.getName());
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(PasswordUtil.convert(user.getPassword()));
        existingUser.setRole(user.getRole());


        // Perform the update in the database
        userMapper.updateUser(existingUser);

        return existingUser;
    }

    @Override
    //update visitor using visitorMapper
    public Visitor updateVisitor(Long id, Visitor visitor) {
        Visitor existingVisitor = visitorMapper.findById(id);
        if (existingVisitor == null) {
            throw new RuntimeException("Visitor not found with ID: " + id);
        }

        // Update the user properties
        existingVisitor.setName(visitor.getName());
        existingVisitor.setUsername(visitor.getUsername());
        // existingVisitor.setPassword(PasswordUtil.convert(visitor.getPassword()));
        existingVisitor.setRole(visitor.getRole());
        existingVisitor.setAvatar(visitor.getAvatar());
        existingVisitor.setPhone(visitor.getPhone());
        existingVisitor.setGender(visitor.getGender());
        existingVisitor.setDepartment(visitor.getDepartment());
        existingVisitor.setTitle(visitor.getTitle());
        existingVisitor.setEmergentContact(visitor.getEmergentContact());
        existingVisitor.setEmergentPhone(visitor.getEmergentPhone());
        existingVisitor.setOpenid(visitor.getOpenid());

        // Perform the update in the database
        visitorMapper.updateVisitor(existingVisitor);

        return existingVisitor;
    }

    //update Admin using AdminMapper
    @Override
    public Admin updateAdmin(Long id, Admin admin) {
        Admin existingAdmin = adminMapper.findById(id);
        if (existingAdmin == null) {
            throw new RuntimeException("Admin not found with ID: " + id);
        }

        // Update the admin properties
        existingAdmin.setName(admin.getName());
        existingAdmin.setUsername(admin.getUsername());
        // existingAdmin.setPassword(PasswordUtil.convert(admin.getPassword()));
        existingAdmin.setPassword(admin.getPassword());
        existingAdmin.setRole(admin.getRole());
        existingAdmin.setAvatar(admin.getAvatar());
        existingAdmin.setPhone(admin.getPhone());
        existingAdmin.setGender(admin.getGender());
        existingAdmin.setDepartment(admin.getDepartment());
        existingAdmin.setTitle(admin.getTitle());

        // Perform the update in the database
        adminMapper.updateAdmin(existingAdmin);

        return existingAdmin;
    }

    //update Counselor using CounselorMapper
    @Override
    public Counselor updateCounselor(Long id, Counselor counselor) {
        Counselor existingCounselor = counselorMapper.findById(id);
        if (existingCounselor == null) {
            throw new RuntimeException("Counselor not found with ID: " + id);
        }

        // Update the counselor properties
        existingCounselor.setName(counselor.getName());
        existingCounselor.setUsername(counselor.getUsername());
        // existingCounselor.setPassword(PasswordUtil.convert(counselor.getPassword()));
        existingCounselor.setPassword(counselor.getPassword());
        existingCounselor.setRole(counselor.getRole());
        existingCounselor.setAvatar(counselor.getAvatar());
        existingCounselor.setPhone(counselor.getPhone());
        existingCounselor.setGender(counselor.getGender());
        existingCounselor.setDepartment(counselor.getDepartment());
        existingCounselor.setTitle(counselor.getTitle());
        existingCounselor.setMaxConsult(counselor.getMaxConsult());
        // Perform the update in the database
        counselorMapper.updateCounselor(counselor);

        return existingCounselor;
    }

    //update Supervisor using SupervisorMapper
    @Override
    public Supervisor updateSupervisor(Long id, Supervisor supervisor) {
        Supervisor existingSupervisor = supervisorMapper.findById(id);
        if (existingSupervisor == null) {
            throw new RuntimeException("Supervisor not found with ID: " + id);
        }

        // Update the supervisor properties
        existingSupervisor.setName(supervisor.getName());
        existingSupervisor.setUsername(supervisor.getUsername());
        // existingSupervisor.setPassword(PasswordUtil.convert(supervisor.getPassword()));
        existingSupervisor.setPassword(supervisor.getPassword());
        existingSupervisor.setRole(supervisor.getRole());
        existingSupervisor.setAvatar(supervisor.getAvatar());
        existingSupervisor.setPhone(supervisor.getPhone());
        existingSupervisor.setGender(supervisor.getGender());
        existingSupervisor.setDepartment(supervisor.getDepartment());
        existingSupervisor.setTitle(supervisor.getTitle());
        existingSupervisor.setQualification(supervisor.getQualification());
        existingSupervisor.setQualificationCode(supervisor.getQualificationCode());

        // Perform the update in the database
        supervisorMapper.updateSupervisor(existingSupervisor);

        return existingSupervisor;
    }

    @Override
    public boolean isPhoneUsedByOtherCounselor(Long id, String phone) {
        // Get the counselor with the given phone number
        Counselor counselor = counselorMapper.selectOne(new QueryWrapper<Counselor>().eq("phone", phone));
        // Check if the counselor exists and has a different ID than the given ID
        return counselor != null && !counselor.getId().equals(id);
    }

    @Override
    public boolean isPhoneUsedByOtherSupervisor(Long id, String phone) {
        // Get the counselor with the given phone number
        Supervisor supervisor = supervisorMapper.selectOne(new QueryWrapper<Supervisor>().eq("phone", phone));
        // Check if the counselor exists and has a different ID than the given ID
        return supervisor != null && !supervisor.getId().equals(id);
    }

    @Override
    public boolean isEmailUsedByOtherCounselor(Long id, String email) {
        // Get the counselor with the given email address
        Counselor counselor = counselorMapper.selectOne(new QueryWrapper<Counselor>().eq("email", email));
        // Check if the counselor exists and has a different ID than the given ID
        return counselor != null && !counselor.getId().equals(id);
    }

    @Override
    public boolean isUsernameUsedByOtherUser(String username) {
        // Get the user with the given username
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        // Check if the user exists
        return user != null;
    }

    @Override
    public boolean isUsernameUsedByOtherCounselor(String username) {
        // Get the counselor with the given username
        Counselor counselor = counselorMapper.selectOne(new QueryWrapper<Counselor>().eq("username", username));
        // Check if the counselor exists
        return counselor != null;
    }

    @Override
    public boolean isUsernameUsedByOtherSupervisor(String username) {
        // Get the supervisor with the given username
        Supervisor supervisor = supervisorMapper.selectOne(new QueryWrapper<Supervisor>().eq("username", username));
        // Check if the supervisor exists
        return supervisor != null;
    }

    @Override
    public HashMap<String, Integer> getBasicStatInfo() {
        Integer totalNumCounselor = conversationMapper.findTodayNum();
        Integer totalTime = conversationMapper.findTodayTotal();
        Integer curNum = conversationMapper.findCurrentNum();
        Integer totalNumSupervisor = dialogueMapper.findTodayNum();
        totalNumCounselor = totalNumCounselor + curNum;
        HashMap<String, Integer> h = new HashMap<>();
        h.put("totalNumCounselor",totalNumCounselor);
        h.put("totalTime",totalTime);
        h.put("curNum",curNum);
        h.put("totalNumSupervisor",totalNumSupervisor);
        return h;

    }

}

    


