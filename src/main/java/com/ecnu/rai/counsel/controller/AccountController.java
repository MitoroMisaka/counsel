package com.ecnu.rai.counsel.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.dao.SigninRequest;
import com.ecnu.rai.counsel.entity.*;
import com.ecnu.rai.counsel.mapper.*;
import com.ecnu.rai.counsel.response.GetUserResponse;
import com.ecnu.rai.counsel.service.AccountService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.bytebuddy.asm.Advice;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
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


    @RequiresUser
    @PostMapping("/hello")
    @ApiImplicitParam(name = "str", value = "参数", required = true, paramType = "query", dataType = "String")
    public Result Hello(@RequestParam("str")@NotNull String str) {
        return Result.success(null);
    }

    @GetMapping("/users")
    @ApiOperation("获取用户列表")
    public Result getUsers() {
        return Result.success("获取成功", userMapper.getUserList());
    }

    //获取用户信息
    @GetMapping("/{id}")
    public Result getUser(@PathVariable Long id) {
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

    @PutMapping("/visitor/{id}")
    public ResponseEntity<Visitor> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody Visitor visitor
    ) {
        Visitor updatedVisitor = accountService.updateVisitor(id , visitor);
        //build user by visitor
        User user = User.builder()
                .id(updatedVisitor.getId())
                .name(updatedVisitor.getName())
                .username(updatedVisitor.getUsername())
                .password(updatedVisitor.getPassword())
                .role("visitor")
                .build();
        User updatedUser = accountService.updateUser(id, user);
        return ResponseEntity.ok(updatedVisitor);
    }

    //update Admin
    @PutMapping("/admin/{id}")
    public ResponseEntity<Admin> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody Admin admin
    ) {
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
        return ResponseEntity.ok(updatedAdmin);
    }

    @PostMapping("/counselor")
    public ResponseEntity<String> insertCounselor(@Valid @RequestBody Counselor counselor) {
        // Check if all required fields are filled
        if(counselor.getName() == null || counselor.getGender() == null || counselor.getAge() == null ||
                counselor.getIdNumber() == null || counselor.getPhone() == null || counselor.getEmail() == null ||
                counselor.getSupervisors() == null || counselor.getUsername() == null || counselor.getPassword() == null) {
            return ResponseEntity.badRequest().build();
        }
        // Check if the name is valid
        if(!counselor.getName().matches("[\\u4e00-\\u9fa5a-zA-Z]{2,32}")) {
            return ResponseEntity.badRequest().body(null);
        }
        // Check if the ID number is valid
        if(!counselor.getIdNumber().matches("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)")) {
            return ResponseEntity.badRequest().body(null);
        }
        // Extract the gender and age from the ID number
        String idNumber = counselor.getIdNumber();
        String gender = idNumber.substring(16, 17);
        Integer age = LocalDate.now().getYear() - Integer.parseInt("19" + idNumber.substring(6, 8));
        counselor.setGender(gender);
        counselor.setAge(age);
        // Check if the phone number is valid
        if(!counselor.getPhone().matches("^1(3|4|5|6|7|8|9)\\d{9}$")) {
            return ResponseEntity.badRequest().body(null);
        }
        // Check if the email is valid
        if(!counselor.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return ResponseEntity.badRequest().body(null);
        }
        // Check if the username is valid
        if(!counselor.getUsername().matches("^[A-Za-z0-9_]+$")) {
            return ResponseEntity.badRequest().body(null);
        }
        // Check if the password is valid
        if(counselor.getPassword().length() < 6) {
            return ResponseEntity.badRequest().body(null);
        }
        // Check if the phone number is used by another counselor
        if(accountService.isPhoneUsedByOtherCounselor(null, counselor.getPhone())) {
            return ResponseEntity.badRequest().body(null);
        }
        // Check if the email is used by another counselor
        if(accountService.isEmailUsedByOtherCounselor(null, counselor.getEmail())) {
            return ResponseEntity.badRequest().body(null);
        }
        // Build the user object
        User user = User.builder()
                .name(counselor.getName())
                .username(counselor.getUsername())
                .password(counselor.getPassword())
                .role("COUNSELOR")
                .build();
        // Insert the user
        userMapper.insertUser(user);
        User insertedUser = userMapper.findByUsername(user.getUsername());
        // Set the ID of the counselor to the ID of the user
        counselor.setId(insertedUser.getId());
        // Set default values for role, create time, update time, enabled, deleted, and rating
        counselor.setRole("COUNSELOR");
        counselor.setCreateTime(LocalDateTime.now());
        counselor.setUpdateTime(LocalDateTime.now());
        counselor.setEnabled(true);
        counselor.setDeleted(false);
        counselor.setRating(0);
        // Insert the counselor
        counselorMapper.insertCounselor(counselor);
        // Return the inserted counselor
        return ResponseEntity.ok("sign in successfully");
    }

    //update Counselor
    @PutMapping("/counselor/{id}")
    public ResponseEntity<Counselor> updateCounselor(
        @PathVariable Long id,
        @Valid @RequestBody Counselor counselor
    ) {
        // Check if all required fields are filled
        if(counselor.getName() == null || counselor.getGender() == null || counselor.getAge() == null ||
                counselor.getIdNumber() == null || counselor.getPhone() == null || counselor.getEmail() == null ||
                counselor.getSupervisors() == null || counselor.getUsername() == null || counselor.getPassword() == null) {
            return ResponseEntity.badRequest().build();
        }
        // Check if the name is valid
        if(!counselor.getName().matches("[\\u4e00-\\u9fa5a-zA-Z]{2,32}")) {
            return ResponseEntity.badRequest().body(null);
        }
        // Check if the ID number is valid
        if(!counselor.getIdNumber().matches("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)")) {
            return ResponseEntity.badRequest().body(null);
        }
        // Extract the gender and age from the ID number
        String idNumber = counselor.getIdNumber();
        String gender = idNumber.substring(16, 17);
        Integer age = LocalDate.now().getYear() - Integer.parseInt("19" + idNumber.substring(6, 8));
        counselor.setGender(gender);
        counselor.setAge(age);
        // Check if the phone number is valid
        if(!counselor.getPhone().matches("^1(3|4|5|6|7|8|9)\\d{9}$")) {
            return ResponseEntity.badRequest().body(null);
        }
        // Check if the email is valid
        if(!counselor.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return ResponseEntity.badRequest().body(null);
        }
        // Check if the username is valid
        if(!counselor.getUsername().matches("^[A-Za-z0-9_]+$")) {
            return ResponseEntity.badRequest().body(null);
        }
        // Check if the password is valid
        if(counselor.getPassword().length() < 6) {
            return ResponseEntity.badRequest().body(null);
        }
        // Check if the phone number is unique
        if(accountService.isPhoneUsedByOtherCounselor(id, counselor.getPhone())) {
            return ResponseEntity.badRequest().body(null);
        }
        // Check if the email is unique
        if(accountService.isEmailUsedByOtherCounselor(id, counselor.getEmail())) {
            return ResponseEntity.badRequest().body(null);
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
        return ResponseEntity.ok(updatedCounselor);
    }



    //update Supervisor
    @PutMapping("/supervisor/{id}")
    public ResponseEntity<Supervisor> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody Supervisor supervisor
    ) {
        Supervisor updatedSupervisor = accountService.updateSupervisor(id , supervisor);
        //build user by supervisor
        User user = User.builder()
                .id(updatedSupervisor.getId())
                .name(updatedSupervisor.getName())
                .username(updatedSupervisor.getUsername())
                .password(updatedSupervisor.getPassword())
                .role("supervisor")
                .build();
        User updatedUser = accountService.updateUser(id, user);
        return ResponseEntity.ok(updatedSupervisor);
    }
}

