package com.ecnu.rai.counsel.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.dao.UserLoginInfo;
import com.ecnu.rai.counsel.dao.*;
import com.ecnu.rai.counsel.entity.*;
import com.ecnu.rai.counsel.mapper.*;
import com.ecnu.rai.counsel.service.AccountService;
import com.ecnu.rai.counsel.service.CounselorService;
import com.ecnu.rai.counsel.service.SupervisorService;
import com.ecnu.rai.counsel.util.PasswordUtil;
import com.tencentcloudapi.cam.v20190116.models.GetUserResponse;
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
import java.util.*;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private CounselorService counselorService;

    @Autowired
    private SupervisorService supervisorService;

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

    @Autowired
    private UserSigMapper userSigMapper;


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

        if(principal.getRole().equals("COUNSELOR"))
        {
            counselorMapper.setStatusOnline(username);
        }
        else if(principal.getRole().equals("SUPERVISOR"))
        {
            supervisorMapper.setStatusOnline(username);
        }

        if(principal.getState()==null)
            return Result.fail("用户处于未知状态");
        else if(principal.getState()==0)
            return Result.fail("用户被禁用");
        System.out.println(principal);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username",username);
        User user = userMapper.selectOne(wrapper);

        String imid = userSigMapper.getImidByName(user.getName());

        return Result.success("登录成功",new UserLoginInfo(user, imid));
    }

    @PostMapping("/logout")
    @ApiOperation("登出")
    public Result logout() throws IOException {
        //get principle
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        //if user is null return fail
        if(user==null)
            return Result.fail("用户未登录");
        if(user.getRole().equals("COUNSELOR")){
            counselorMapper.setStatusOffline(user.getUsername());
        }
        else if(user.getRole().equals("SUPERVISOR"))
        {
            supervisorMapper.setStatusOffline(user.getUsername());
        }
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "order", value = "排序", required = true, dataType = "String")
    })
    public Result getUserList(@RequestParam("page") Integer page,
                                           @RequestParam("size") Integer size,
                                           @RequestParam("order") String order) {
        return Result.success("获取用户列表成功", accountService.findUserList(page, size, order));
    }

    @RequiresRoles("admin")
    @GetMapping("/counselors")
    @ApiOperation("获取咨询师列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "order", value = "排序", required = true, dataType = "String")
    })
    public Result getCounselorList(@RequestParam("page") Integer page,
                                                  @RequestParam("size") Integer size,
                                                  @RequestParam("order") String order) {
        return Result.success("获取咨询师列表成功", counselorService.getAllCounselor(page, size, order));
    }
    @RequiresRoles("admin")
    @GetMapping("/supervisors")
    @ApiOperation("获取督导列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "order", value = "排序", required = true, dataType = "String")
    })
    public Result getSupervisorList(@RequestParam("page") Integer page,
                                                    @RequestParam("size") Integer size,
                                                    @RequestParam("order") String order) {
        return Result.success("获取督导列表成功", supervisorService.getAllSupervisor(page, size, order));
    }
    @RequiresRoles("admin")
    @GetMapping("/visitors")
    @ApiOperation("获取访客列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "order", value = "排序", required = true, dataType = "String")
    })
    public Result getVisitorList(@RequestParam("page") Integer page,
                                              @RequestParam("size") Integer size,
                                              @RequestParam("order") String order) {
        return Result.success("获取访客列表",accountService.findVisitorList(page, size, order));
    }



    @GetMapping("/{id}")
    @ApiOperation("获取用户信息")
    public Result getUserInfo(@PathVariable Long id) {

        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (!currentUser.getRole().equals("ADMIN")) {
            if (currentUser.getId() != id) {
                return Result.fail("无权访问");
            }
        }

        User user = accountService.findUserByID(id);
        String role = user.getRole();
        if(role.equals("VISITOR")) {
            Visitor visitor = visitorMapper.selectById(id);
            return Result.success("获取成功", visitor);
        } else if(role.equals("SUPERVISOR")) {
            Supervisor supervisor = supervisorMapper.selectById(id);
            return Result.success("获取成功", supervisor);
        } else if(role.equals("COUNSELOR")) {
            Counselor counselor = counselorMapper.selectById(id);
            return Result.success("获取成功", counselor);
        } else if(role.equals("ADMIN")) {
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
        if (!currentUser.getRole().equals("ADMIN")) {
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
                .role("ADMIN")
                .build();
        User updatedUser = accountService.updateUser(id, user);
        return Result.success("更新访客数据", updatedVisitor);
    }


    @RequiresRoles("admin")
    @ApiOperation("更新管理员")
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
                .role("ADMIN")
                .build();
        User updatedUser = accountService.updateUser(id, user);
        return Result.success("更新机构管理员信息成功", updatedAdmin);
    }

    // insert Counselor
    @RequiresRoles("admin")
    @ApiOperation("新增咨询师")
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
//        if(counselor.getSupervisors().isEmpty()) {
//            return Result.fail("Should bind at least one supervisor.");
//        }
        // Build the user object
        User user = User.builder()
                .name(counselor.getName())
                .username(counselor.getUsername())
                .password(counselor.getPassword())
                .role("COUNSELOR")
                .state(1)
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
//        for(Supervisor supervisor: counselor.getSupervisors())
//        {
//            superviseMapper.makeSupervise(counselor.getId(), supervisor.getId());
//        }
        return Result.success("Insert counselor successfully", counselor);
    }


    // update Counselor
    @PutMapping("/counselor/{id}")
    @ApiOperation("修改咨询师")
    public Result updateCounselor(
        @PathVariable Long id,
        @Valid @RequestBody Counselor counselor
    ) {

        // 权限控制，只有机构管理员 和 该咨询师本人可以修改
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (!currentUser.getRole().equals("ADMIN")) {
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
                .role("COUNSELOR")
                .build();
        User updatedUser = accountService.updateUser(id, user);
        return Result.success("更新咨询师信息成功", updatedCounselor);
    }


    // insert Supervisor
    @RequiresRoles("admin")
    @ApiOperation("新增督导")
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
        //role must be supervisor
        if(!supervisor.getRole().equals("SUPERVISOR")) {
            return Result.fail("Invalid role");
        }

        supervisor.setPassword(PasswordUtil.convert(supervisor.getPassword()));
        // Check if the phone number is valid
        if(!supervisor.getPhone().matches("^1(3|4|5|6|7|8|9)\\d{9}$")) {
            return Result.fail("Invalid phone number");
        }
        //gender must be "男" or "女
        if(!supervisor.getGender().equals("男") && !supervisor.getGender().equals("女") )
        {
            return Result.fail("invalid gender (must be 男 or 女)");
        }

        //qualification must be '一级' '二级' or '三级'
        if(!supervisor.getQualification().equals("一级") && !supervisor.getQualification().equals("二级") && !supervisor.getQualification().equals("三级")) {
            return Result.fail("Invalid qualification");
        }
        //if qualification number is null return fail
        if(supervisor.getQualificationCode() == null || supervisor.getQualificationCode().equals("")) {
            return Result.fail("Invalid qualification code");
        }

        //department cannot be null or ""
        if(supervisor.getDepartment() == null || supervisor.getDepartment().equals("")) {
            return Result.fail("Invalid department");
        }

        // Build the user object
        User user = User.builder()
                .name(supervisor.getName())
                .username(supervisor.getUsername())
                .password(supervisor.getPassword())
                .role("SUPERVISOR")
                .state(1)
                .build();
        System.out.println(user);

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
          supervisor.setStatus("OFFLINE");
          supervisor.setMaxConsult(10);

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
    @PutMapping("/supervisor/{id}")
    @ApiOperation("更新督导")
    public Result updateSupervisor(
            @PathVariable Long id,
            @Valid @RequestBody Supervisor supervisor
    ) {

        // 权限控制，只有机构管理员 和 该咨询师本人可以修改
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (!currentUser.getRole().equals("ADMIN")) {
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
        //role must be supervisor
        if(!supervisor.getRole().equals("SUPERVISOR")) {
            return Result.fail("Invalid role.");
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
                .role("SUPERVISOR")
                .build();
        User updatedUser = accountService.updateUser(id, user);
        return Result.success("更新督导信息成功", updatedSupervisor);
    }


    @RequiresRoles("admin")
    @ApiOperation("禁用用户")
    @PostMapping("/banUser")
    public Result banUser(@Valid @RequestBody List<Long> ids)
    {
        if (ids == null)
            return Result.fail("No valid user.");

        for (Long id:ids) {
            User user = userMapper.selectById(id);
            if (user == null)
                continue;
            if(Objects.equals(user.getRole(), "ADMIN"))
                continue;
            user.setState(0);
            userMapper.updateState(user);
        }
        return Result.success("Ban users successfully.");
    }


    @RequiresRoles("admin")
    @ApiOperation("启用用户")
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
            userMapper.updateState(user);
        }
        return Result.success("Enable users successfully.");
    }


    @RequiresRoles("admin")
    @ApiOperation("咨询师视角绑定")
    @PostMapping("/counselor/binding/{id}")
    public Result makeSuperviseByCounselor(@PathVariable Long id,
                                          @Valid @RequestBody List<Long> supervisorsId)
    {
        if(!Objects.equals(userMapper.findRoleById(id), "COUNSELOR"))
            return Result.fail("Unauthorized.");
        if(supervisorsId.isEmpty())
            return Result.fail("No supervisors to bind.");
        superviseMapper.deleteCounselorSupervise(id);
        for(Long supervisorid : supervisorsId)
        {
            if(superviseMapper.findSupervise(id,supervisorid)==1||!Objects.equals(userMapper.findRoleById(supervisorid), "SUPERVISOR"))
                continue;
            superviseMapper.makeSupervise(id,supervisorid);
        }

        return Result.success("Bind successfully!");
    }


    @RequiresRoles("admin")
    @ApiOperation("督导视角绑定")
    @PostMapping("/supervisor/binding/{id}")
    public Result makeSuperviseBySupervisor(@PathVariable Long id,
                                           @Valid @RequestBody List<Long> counselorsId)
    {
        if(!Objects.equals(userMapper.findRoleById(id), "SUPERVISOR"))
            return Result.fail("Unauthorized.");
        if(counselorsId.isEmpty())
            return Result.fail("No counselors to bind.");
        superviseMapper.deleteSupervisorSupervise(id);
        for(Long counselorid:counselorsId){
            if(superviseMapper.findSupervise(counselorid,id)==1|| !Objects.equals(userMapper.findRoleById(counselorid), "COUNSELOR"))
                continue;
            superviseMapper.makeSupervise(counselorid,id);
        }

        return Result.success("Bind successfully!");
    }

    @RequiresRoles("admin")
    @ApiOperation("删号")
    @GetMapping("/delete")
    public Result delUser(@Valid @RequestBody List<Long> userId)
    {
        if(userId.isEmpty())
            return Result.fail("No user to delete.");
        for(Long userid:userId){
            if(!Objects.equals(userMapper.findRoleById(userid), "ADMIN"))
                continue;
            userMapper.deleteById(userid);
        }

        return Result.success("Delete successfully!");
    }

    @GetMapping("getBasicStatInfo")
    @ApiOperation("获取基本咨询统计数据")
    public Result getBasicStatInfo() {
        return Result.success("获取成功", accountService.getBasicStatInfo());
    }

}

