package com.ecnu.rai.counsel.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.entity.*;
import com.ecnu.rai.counsel.mapper.*;
import com.ecnu.rai.counsel.response.GetUserResponse;
import com.ecnu.rai.counsel.service.AccountService;
import com.ecnu.rai.counsel.util.PasswordUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private VisitorMapper visitorMapper;

    @Autowired
    private CounselorMapper counselorMapper;

    @Autowired
    private SupervisorMapper supervisorMapper;

    @Autowired
    private SuperviseMapper superviseMapper;


    @PostMapping("/login")
    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码(长度6-20)", required = true, paramType = "query", dataType = "String")
    })
    public Result Login(@RequestParam("username")@NotNull String username,
                        @RequestParam("password")@NotNull @Size(min = 6,max = 20)String password){

        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password) ) {
            return Result.fail("用户名,密码和姓名不能为空！");
        }

        // AuthenticationToken token = new UsernamePasswordToken(username, PasswordUtil.convert(password));
        AuthenticationToken token = new UsernamePasswordToken(username, password);

        try {

            //尝试登陆，将会调用realm的认证方法
            SecurityUtils.getSubject().login(token);

        }catch (AuthenticationException e) {
            if (e instanceof UnknownAccountException) {
                return Result.fail("用户不存在");
            } else if (e instanceof LockedAccountException) {
                return Result.fail("用户被禁用");
            } else if (e instanceof IncorrectCredentialsException) {
                return Result.fail("密码错误");
            } else {
                return Result.fail("用户认证失败");
            }
        }

        User principal = (User) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole(principal.getRole());
        if(principal.getState()==null)
            return Result.fail("用户处于未知状态");
        else if(principal.getState()==0)
            return Result.fail("用户被禁用");
        System.out.println(principal);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username",username);

        return Result.success("登录成功",new GetUserResponse(userMapper.selectOne(wrapper), principal.getRole()));
    }

    @PostMapping("/logout")
    @ApiOperation("登出")
    public Result logout() throws IOException {
        SecurityUtils.getSubject().logout();
        return Result.success(null);
    }

//    @PostMapping("/signup")
//    @ApiOperation("注册")
//    public Result signup(@Valid @RequestBody User user) {
//        return Result.success("注册成功");
//    }


    @RequiresRoles("admin")
    @GetMapping("/users")
    @ApiOperation("获取用户列表")
    public Result getUsers() {
        return Result.success("获取成功", userMapper.getUserList());
    }




    //获取用户信息
    @GetMapping("/{id}")
    public Result getUser(@PathVariable Long id) {

        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (!currentUser.getRole().equals("admin")) {
            if (currentUser.getId() != id) {
                return Result.fail("无权访问");
            }
        }

        User user = accountService.findUserByID(id);
        String role = user.getRole();
        if(role.equals("visitor")) {
            Visitor visitor = visitorMapper.selectById(id);
            return Result.success("获取成功", visitor);
        } else if(role.equals("supervisor")) {
            Supervisor supervisor = supervisorMapper.selectById(id);
            return Result.success("获取成功", supervisor);
        } else if(role.equals("counselor")) {
            Counselor counselor = counselorMapper.selectById(id);
            return Result.success("获取成功", counselor);
        } else if(role.equals("admin")) {
            Admin admin = adminMapper.selectById(id);
            return Result.success("获取成功", admin);
        }
        return Result.fail("获取失败");
    }


    // update Visitor
    @PutMapping("/visitor/{id}")
    @ApiOperation("更新访客信息")
    public Result updateVisitor(
            @PathVariable Long id,
            @Valid @RequestBody Visitor visitor
    ) {

        // 权限控制，只有机构管理员 和 该访客本人可以修改
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (!currentUser.getRole().equals("admin")) {
            if (currentUser.getId() != id) {
                return Result.fail("You have no permission to update this counselor");
            }
        }

        // Check if all required fields are filled
        if(visitor.getName() == null || visitor.getUsername() == null || visitor.getRole() == null ||
                visitor.getAvatar() == null || visitor.getGender() == null || visitor.getPhone() == null ||
                visitor.getDepartment() == null || visitor.getTitle() == null || visitor.getEmergentContact() == null ||visitor.getEmergentPhone() == null ||
                visitor.getOpenid() == null) {
            return Result.fail("All required fields must be filled.");
        }
        // Check if the name is valid
        if(!visitor.getName().matches("[\\u4e00-\\u9fa5a-zA-Z]{2,32}")) {
            return Result.fail("Invalid name.");
        }
        // Check if the username is valid
        if(!visitor.getUsername().matches("^[A-Za-z0-9_]+$")) {
            return Result.fail("Invalid username.");
        }
        // Check if the password is valid
        // Check if the phone number is valid
        if(!visitor.getPhone().matches("^1(3|4|5|6|7|8|9)\\d{9}$")) {
            return Result.fail("Invalid phone number.");
        }
        // Check if the phone number is unique
        if(accountService.isPhoneUsedByOtherCounselor(id, visitor.getPhone())) {
            return Result.fail("Duplicate Phone Number.");
        }
        // Check if the emergentContact is valid
        if(!visitor.getName().matches("[\\u4e00-\\u9fa5a-zA-Z]{2,32}")) {
            return Result.fail("Invalid emergentContact.");
        }
        // Check if the emergentPhone number is valid
        if(!visitor.getPhone().matches("^1(3|4|5|6|7|8|9)\\d{9}$")) {
            return Result.fail("Invalid phone number.");
        }
        // Check if the emergentPhone number equals to the phone number
        if(!visitor.getPhone().equals(visitor.getPhone())) {
            return Result.fail("emergentPhone number can't be the same with the phone number.");
        }

        Visitor updatedVisitor = accountService.updateVisitor(id , visitor);
        //build user by visitor
        User user = User.builder()
                .id(updatedVisitor.getId())
                .name(updatedVisitor.getName())
                .username(updatedVisitor.getUsername())
                .role("visitor")
                .state(1)
                .build();
        User updatedUser = accountService.updateUser(id, user);
        return Result.success("更新访客数据", updatedVisitor);
    }

    // update Admin
    @RequiresRoles("admin")
    @PutMapping("/admin/{id}")
    public Result updateAdmin(
            @PathVariable Long id,
            @Valid @RequestBody Admin admin
    ) {

        // 权限控制，只有机构管理员 和 该访客本人可以修改
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (currentUser.getId() != id) {
            return Result.fail("You have no permission to update this counselor");
        }

        // Check if all required fields are filled
        if(admin.getName() == null || admin.getUsername() == null || admin.getPassword() == null || admin.getRole() == null ||
                admin.getAvatar() == null || admin.getGender() == null || admin.getPhone() == null ||
                admin.getDepartment() == null || admin.getTitle() == null) {
            return Result.fail("All required fields must be filled.");
        }
        // Check if the name is valid
        if(!admin.getName().matches("[\\u4e00-\\u9fa5a-zA-Z]{2,32}")) {
            return Result.fail("Invalid name.");
        }
        // Check if the username is valid
        if(!admin.getUsername().matches("^[A-Za-z0-9_]+$")) {
            return Result.fail("Invalid username.");
        }
        // Check if the password is valid
        if(admin.getPassword().length() < 6) {
            return Result.fail("Password must be at least 6 characters long.");
        }
        // Check if the phone number is valid
        if(!admin.getPhone().matches("^1(3|4|5|6|7|8|9)\\d{9}$")) {
            return Result.fail("Invalid phone number.");
        }
        // Check if the phone number is unique
        if(accountService.isPhoneUsedByOtherCounselor(id, admin.getPhone())) {
            return Result.fail("Duplicate Phone Number.");
        }

        Admin updatedAdmin = accountService.updateAdmin(id , admin);
        //build user by admin
        User user = User.builder()
                .id(updatedAdmin.getId())
                .name(updatedAdmin.getName())
                .username(updatedAdmin.getUsername())
                .password(updatedAdmin.getPassword())
                .role("admin")
                .build();
        User updatedUser = accountService.updateUser(id, user);
        return Result.success("更新机构管理员信息成功", updatedAdmin);
    }

    // insert Counselor
    @RequiresRoles("admin")
    @PostMapping("/counselor")
    public Result insertCounselor(@Valid @RequestBody Counselor counselor) {
        // Check if all required fields are filled
        if(counselor.getName() == null || counselor.getGender() == null || counselor.getAge() == null ||
                counselor.getIdNumber() == null || counselor.getPhone() == null || counselor.getEmail() == null ||
                counselor.getUsername() == null || counselor.getPassword() == null) {
            return Result.fail("Required fields are not filled");
        }
        // Check if the name is valid
        if(!counselor.getName().matches("[\\u4e00-\\u9fa5a-zA-Z]{2,32}")) {
            return Result.fail("Invalid name");
        }
        // Check if the ID number is valid
        if(!counselor.getIdNumber().matches("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)")) {
            return Result.fail("Invalid ID number");
        }
        // Extract the gender and age from the ID number
        String idNumber = counselor.getIdNumber();
        Integer age = LocalDate.now().getYear() - Integer.parseInt(idNumber.substring(6, 10));
        counselor.setAge(age);
        counselor.setPassword(PasswordUtil.convert(counselor.getPassword()));
        // Check if the phone number is valid
        if(!counselor.getPhone().matches("^1(3|4|5|6|7|8|9)\\d{9}$")) {
            return Result.fail("Invalid phone number");
        }
        // Check if the email is valid
        if(!counselor.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return Result.fail("Invalid email");
        }
        // Check if the username is valid
        if(!counselor.getUsername().matches("^[A-Za-z0-9_]+$")) {
            return Result.fail("Invalid username");
        }
        // Check if the password is valid
        if(counselor.getPassword().length() < 6) {
            return Result.fail("Invalid password");
        }
        // Check if the phone number is used by another counselor
        if(accountService.isPhoneUsedByOtherCounselor(null, counselor.getPhone())) {
            return Result.fail("Phone number is used by another counselor");
        }
        // Check if the email is used by another counselor
        if(accountService.isEmailUsedByOtherCounselor(null, counselor.getEmail())) {
            return Result.fail("Email is used by another counselor");
        }
        // Check if bind to supervisor
        if(counselor.getSupervisors().isEmpty()) {
            return Result.fail("Should bind at least one supervisor.");
        }
        // Build the user object
        User user = User.builder()
                .name(counselor.getName())
                .username(counselor.getUsername())
                .password(counselor.getPassword())
                .role("counselor")
                .build();
        // Insert the user
        //if the username is used by another user
        if(accountService.isUsernameUsedByOtherUser(user.getUsername())) {
            return Result.fail("Username is used by another user ");
        }else {
            userMapper.insertUser(user);
        }
        User insertedUser = userMapper.findByUsername(user.getUsername());
        // Set the ID of the counselor to the ID of the user
        counselor.setId(insertedUser.getId());
        // Set default values for role, create time, update time, enabled, deleted, and rating
        counselor.setRole("COUNSELOR");
        counselor.setCreateTime(LocalDateTime.now());
        counselor.setUpdateTime(LocalDateTime.now());
        counselor.setEnabled(true);
        counselor.setDeleted(false);
        counselor.setStatus("OFFLINE");
        counselor.setRating(0);
        counselor.setMaxConsult(10);
        // Insert the counselor
        if(accountService.isUsernameUsedByOtherCounselor(user.getUsername())) {
            return Result.fail("Username is used by another counselor");
        }else {
            counselorMapper.insertCounselor(counselor);
        }
        // Return the inserted counselor
        //Bind counselor and supervisors
        for(Supervisor supervisor: counselor.getSupervisors())
        {
            superviseMapper.makeSupervise(counselor.getId(), supervisor.getId());
        }
        return Result.success("Insert counselor successfully", counselor);
    }


    // update Counselor
    @PutMapping("/counselor/{id}")
    public Result updateCounselor(
        @PathVariable Long id,
        @Valid @RequestBody Counselor counselor
    ) {

        // 权限控制，只有机构管理员 和 该咨询师本人可以修改
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (!currentUser.getRole().equals("admin")) {
            if (currentUser.getId() != id) {
                return Result.fail("You have no permission to update this counselor");
            }
        }

        // Check if all required fields are filled
        if(counselor.getName() == null ||
                counselor.getIdNumber() == null || counselor.getPhone() == null || counselor.getEmail() == null ||
                counselor.getUsername() == null || counselor.getPassword() == null) {
            return Result.fail("All required fields must be filled.");
        }
        // Check if the name is valid
        if(!counselor.getName().matches("[\\u4e00-\\u9fa5a-zA-Z]{2,32}")) {
            return Result.fail("Invalid name.");
        }
        // Check if the ID number is valid
        if(!counselor.getIdNumber().matches("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)")) {
            return Result.fail("Invalid ID number.");
        }
        // Extract the gender and age from the ID number
        String idNumber = counselor.getIdNumber();
        String gender = idNumber.substring(16, 17);
        Integer age = LocalDate.now().getYear() - Integer.parseInt(idNumber.substring(6, 9));
        counselor.setGender(gender);
        counselor.setAge(age);
        counselor.setUpdateTime(LocalDateTime.now());
        // Check if the phone number is valid
        if(!counselor.getPhone().matches("^1(3|4|5|6|7|8|9)\\d{9}$")) {
            return Result.fail("Invalid phone number.");
        }
        // Check if the email is valid
        if(!counselor.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return Result.fail("Invalid email.");
        }
        // Check if the username is valid
        if(!counselor.getUsername().matches("^[A-Za-z0-9_]+$")) {
            return Result.fail("Invalid username.");
        }
        // Check if the password is valid
        if(counselor.getPassword().length() < 6) {
            return Result.fail("Password must be at least 6 characters long.");
        }
        // Check if the phone number is unique
        if(accountService.isPhoneUsedByOtherCounselor(id, counselor.getPhone())) {
            return Result.fail("Duplicate Phone Number.");
        }
        // Check if the email is unique
        if(accountService.isEmailUsedByOtherCounselor(id, counselor.getEmail())) {
            return Result.fail("Duplicate Email.");
        }
        // Update the counselor
        Counselor updatedCounselor = accountService.updateCounselor(id, counselor);
        // Build user by counselor
        User user = User.builder()
                .id(updatedCounselor.getId())
                .name(updatedCounselor.getName())
                .username(updatedCounselor.getUsername())
                .password(updatedCounselor.getPassword())
                .role("counselor")
                .build();
        User updatedUser = accountService.updateUser(id, user);
        return Result.success("更新咨询师信息成功", updatedCounselor);
    }


    // insert Supervisor
    @RequiresRoles("admin")
    @PostMapping("/supervisor")
    public Result insertSupervisor(@Valid @RequestBody Supervisor supervisor) {
        // Check if all required fields are filled
        if(supervisor.getName() == null || supervisor.getUsername() == null || supervisor.getPassword() == null ||
                supervisor.getRole() == null || supervisor.getAvatar() == null || supervisor.getGender() == null ||
                supervisor.getPhone() == null || supervisor.getDepartment() == null || supervisor.getTitle() == null ||
                supervisor.getQualification() == null || supervisor.getQualificationCode() == null) {
            return Result.fail("Required fields are not filled");
        }
        // Check if the name is valid
        if(!supervisor.getName().matches("[\\u4e00-\\u9fa5a-zA-Z]{2,32}")) {
            return Result.fail("Invalid name");
        }
        // Check if the username is valid
        if(!supervisor.getUsername().matches("^[A-Za-z0-9_]+$")) {
            return Result.fail("Invalid username");
        }
        // Check if the password is valid
        if(supervisor.getPassword().length() < 6) {
            return Result.fail("Invalid password");
        }
        supervisor.setPassword(PasswordUtil.convert(supervisor.getPassword()));
        // Check if the phone number is valid
        if(!supervisor.getPhone().matches("^1(3|4|5|6|7|8|9)\\d{9}$")) {
            return Result.fail("Invalid phone number");
        }

        // Build the user object
        User user = User.builder()
                .name(supervisor.getName())
                .username(supervisor.getUsername())
                .password(supervisor.getPassword())
                .role("supervisor")
                .state(1)
                .build();

        // Insert the user
        // Check if the phone number is used by another counselor
        if(accountService.isPhoneUsedByOtherSupervisor(null, supervisor.getPhone())) {
            return Result.fail("Phone number is used by another counselor");
        }
        //if the username is used by another user
        if(accountService.isUsernameUsedByOtherUser(user.getUsername())) {
            return Result.fail("Username is used by another user ");
        }else {
            userMapper.insertUser(user);
        }

          User insertedUser = userMapper.findByUsername(user.getUsername());
          supervisor.setId(insertedUser.getId());

        // Insert the counselor
        if(accountService.isUsernameUsedByOtherCounselor(user.getUsername())) {
            return Result.fail("Username is used by another supervisor");
        }else {
            supervisorMapper.insertSupervisor(supervisor);
        }
        // Return the inserted counselor
        return Result.success("Insert supervisor successfully", supervisor);
    }


    // update Supervisor


    //update Supervisor
    @PutMapping("/supervisor/{id}")
    public Result updateSupervisor(
            @PathVariable Long id,
            @Valid @RequestBody Supervisor supervisor
    ) {

        // 权限控制，只有机构管理员 和 该咨询师本人可以修改
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (!currentUser.getRole().equals("admin")) {
            if (currentUser.getId() != id) {
                return Result.fail("You have no permission to update this supervisor");
            }
        }

        // Check if all required fields are filled
        if(supervisor.getName() == null || supervisor.getUsername() == null || supervisor.getPassword() == null ||
                supervisor.getRole() == null || supervisor.getAvatar() == null || supervisor.getGender() == null ||
                supervisor.getPhone() == null || supervisor.getDepartment() == null || supervisor.getTitle() == null ||
                supervisor.getQualification() == null || supervisor.getQualificationCode() == null) {
            return Result.fail("Required fields are not filled");
        }
        // Check if the name is valid
        if(!supervisor.getName().matches("[\\u4e00-\\u9fa5a-zA-Z]{2,32}")) {
            return Result.fail("Invalid name.");
        }
        // Check if the username is valid
        if(!supervisor.getUsername().matches("^[A-Za-z0-9_]+$")) {
            return Result.fail("Invalid username.");
        }
        // Check if the password is valid
        if(supervisor.getPassword().length() < 6) {
            return Result.fail("Password must be at least 6 characters long.");
        }
        // Check if the phone number is valid
        if(!supervisor.getPhone().matches("^1(3|4|5|6|7|8|9)\\d{9}$")) {
            return Result.fail("Invalid phone number.");
        }
        // Check if the phone number is unique
        if(accountService.isPhoneUsedByOtherSupervisor(id, supervisor.getPhone())) {
            return Result.fail("Duplicate Phone Number.");
        }

        // Update the Supervisor
        Supervisor updatedSupervisor = accountService.updateSupervisor(id, supervisor);
        // Build user by Supervisor
        User user = User.builder()
                .id(updatedSupervisor.getId())
                .name(updatedSupervisor.getName())
                .username(updatedSupervisor.getUsername())
                .password(updatedSupervisor.getPassword())
                .role("supervisor")
                .build();
        User updatedUser = accountService.updateUser(id, user);
        return Result.success("更新督导信息成功", updatedSupervisor);
    }
//禁用用户
    @RequiresRoles("admin")
    @PostMapping("/banUser")
    public Result banUser(@Valid @RequestBody List<Long> ids)
    {
        if (ids == null)
            return Result.fail("No valid user.");

        for (Long id:ids) {
            User user = userMapper.selectById(id);
            if (user == null)
                continue;
            if(Objects.equals(user.getRole(), "admin"))
                continue;
            user.setState(0);
            userMapper.updateUser(user);
        }
        return Result.success("Ban users successfully.");
    }

    //启用用户
    @RequiresRoles("admin")
    @PostMapping("/enableUser")
    public Result enableUser(@Valid @RequestBody List<Long> ids)
    {
        if (ids == null)
            return Result.fail("No valid user.");
        for (Long id:ids) {
            User user = userMapper.selectById(id);
            if (user == null)
                continue;
            user.setState(1);
            userMapper.updateUser(user);
        }
        return Result.success("Enable users successfully.");
    }

    //咨询师页面绑定
    @RequiresRoles("admin")
    @PostMapping("/counselor/{id}/binding")
    public Result makeSuperviseByCounselor(@PathVariable Long id,
                                          @Valid @RequestBody List<Supervisor> supervisors)
    {
        if(supervisors.isEmpty())
            return Result.fail("No supervisors to bind.");
        for(Supervisor supervisor:supervisors)
            superviseMapper.makeSupervise(id,supervisor.getId());
        return Result.success("Bind successfully!");
    }

    //督导页面绑定
    @RequiresRoles("admin")
    @PostMapping("/supervisor/{id}/binding")
    public Result makeSuperviseBySupervisor(@PathVariable Long id,
                                           @Valid @RequestBody List<Counselor> counselors)
    {
        if(counselors.isEmpty())
            return Result.fail("No counselors to bind.");
        for(Counselor counselor:counselors)
            superviseMapper.makeSupervise(counselor.getId(),id);
        return Result.success("Bind successfully!");
    }

    //咨询师页面解绑
    @RequiresRoles("admin")
    @PostMapping("/counselor/{id}/unbinding")
    public Result delSuperviseByCounselor(@PathVariable Long id,
                               @Valid @RequestBody List<Supervisor> supervisors)
    {
        if(supervisors.isEmpty())
            return Result.fail("No supervisors to unbind.");
        for(Supervisor supervisor:supervisors)
            superviseMapper.deleteSupervise(id,supervisor.getId());
        return Result.success("Unbinding successfully!");
    }

    //督导页面解绑
    @RequiresRoles("admin")
    @PostMapping("/supervisor/{id}/unbinding")
    public Result delSuperviseBySupervisor(@PathVariable Long id,
                               @Valid @RequestBody List<Counselor> counselors)
    {
        if(counselors.isEmpty())
            return Result.fail("No counselors to unbind.");
        for(Counselor counselor:counselors)
            superviseMapper.deleteSupervise(counselor.getId(),id);
        return Result.success("Bind successfully!");
    }


}

