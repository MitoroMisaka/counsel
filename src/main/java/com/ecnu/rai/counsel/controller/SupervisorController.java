package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervisor;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.mapper.UserMapper;
import com.ecnu.rai.counsel.service.SupervisorService;
import com.ecnu.rai.counsel.util.PasswordUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supervisor")
public class SupervisorController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SupervisorService supervisorService;

    @PostMapping("/add")
    @ApiOperation("添加督导")
    public Result addSupervisor(@RequestBody Supervisor supervisor) {
        //检查所有supervisor的属性是否有问题并返回错误信息
        //姓名必须是大小写字母或汉字 2-16位
        if (!supervisor.getName().matches("^[a-zA-Z\\u4e00-\\u9fa5]{2,16}$")) {
            return Result.fail("姓名必须是大小写字母或汉字 2-16位");
        }
        //用户名只能是大小写字母数字和下划线组成。 2-32 位
        if (!supervisor.getUsername().matches("^[a-zA-Z0-9_]{2,32}$")) {
            return Result.fail("用户名只能是大小写字母数字和下划线组成。 2-32 位");
        }
        //本系统的角色分为admin, visitor , counselor 和 supervisor.
        //其中admin是系统管理员，visitor是访客，counselor是咨询师，supervisor是督导。
        //角色必须是supervisor
        if (!supervisor.getRole().equals("supervisor")) {
            return Result.fail("角色必须是supervisor");
        }
        //电话号码必须是11位
        if (!supervisor.getPhone().matches("^\\d{11}$")) {
            return Result.fail("电话号码必须是11位");
        }
        //qualification必须是合法值为“一级”、“二级”、“三级”
        if (!supervisor.getQualification().equals("一级") && !supervisor.getQualification().equals("二级") && !supervisor.getQualification().equals("三级")) {
            return Result.fail("qualification必须是合法值为“一级”、“二级”、“三级”");
        }
        //gender只能为男或女
        if(!supervisor.getGender().equals("女") && !supervisor.getGender().equals("男")){
            return Result.fail("gender只能为男或女");
        }
        //密码必须是6-20位
        if(supervisor.getPassword().length()<6 || supervisor.getPassword().length()>20){
            return Result.fail("密码必须是6-20位");
        }
        supervisor.setPassword(PasswordUtil.convert(supervisor.getPassword()));
        User user = new User();
        user.setName(supervisor.getName());
        user.setUsername(supervisor.getUsername());
        user.setPassword(supervisor.getPassword());
        user.setRole("supervisor");
        userMapper.insertUser(user);
        Long id = userMapper.findByUsername(supervisor.getUsername()).getId();
        supervisor.setId(id);
        supervisorService.addSupervisor(supervisor);
        return Result.success("添加成功");
    }

    @GetMapping("/info")
    @ApiOperation("获取督导基本信息")
    public Result getCounselorInfo(@RequestParam("id") Long id) {
        Supervisor supervisor = supervisorService.findSupervisorByID(id);
        return Result.success("获取成功", supervisor);
    }

    @GetMapping("/list")
    @ApiOperation("获取督导列表")
    public Page<Supervisor> getSupervisorList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "order", defaultValue = "id_asc") String order
    ) {
        return supervisorService.getSupervisorList(page, size, order);
    }

    @GetMapping("/available")
    @ApiOperation("获取可用督导列表")
    public Page<Supervisor> getAvailableSupervisorList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "order", defaultValue = "id_asc") String order
    ) {
        return supervisorService.getAvailableSupervisorList(page, size, order);
    }

    @PostMapping("/update")
    @ApiOperation("更新督导基本信息")
    public Result updateCounselorInfo(@RequestBody Supervisor supervisor) {
        //检查所有supervisor的属性是否有问题并返回错误信息
        //姓名必须是大小写字母或汉字 2-16位
        if (!supervisor.getName().matches("^[a-zA-Z\\u4e00-\\u9fa5]{2,16}$")) {
            return Result.fail("姓名必须是大小写字母或汉字 2-16位");
        }
        //用户名只能是大小写字母数字和下划线组成。 2-32 位
        if (!supervisor.getUsername().matches("^[a-zA-Z0-9_]{2,32}$")) {
            return Result.fail("用户名只能是大小写字母数字和下划线组成。 2-32 位");
        }
        //本系统的角色分为admin, visitor , counselor 和 supervisor.
        //其中admin是系统管理员，visitor是访客，counselor是咨询师，supervisor是督导。
        //角色必须是supervisor
        if (!supervisor.getRole().equals("supervisor")) {
            return Result.fail("角色必须是supervisor");
        }
        //电话号码必须是11位
        if (!supervisor.getPhone().matches("^\\d{11}$")) {
            return Result.fail("电话号码必须是11位");
        }
        //qualification必须是合法值为“一级”、“二级”、“三级”
        if (!supervisor.getQualification().equals("一级") && !supervisor.getQualification().equals("二级") && !supervisor.getQualification().equals("三级")) {
            return Result.fail("qualification必须是合法值为“一级”、“二级”、“三级”");
        }
        //gender只能为男或女
        if(!supervisor.getGender().equals("男") && !supervisor.getGender().equals("女")){
            return Result.fail("gender只能为男(0)或女(1)");
        }
        //密码必须是6-20位
        if(supervisor.getPassword().length()<6 || supervisor.getPassword().length()>20){
            return Result.fail("密码必须是6-20位");
        }
        supervisor.setPassword(PasswordUtil.convert(supervisor.getPassword()));
        supervisorService.updateSupervisor(supervisor);
        User user = new User();
        user.setId(supervisor.getId());
        user.setName(supervisor.getName());
        user.setUsername(supervisor.getUsername());
        user.setPassword(supervisor.getPassword());
        user.setRole("supervisor");
        userMapper.updateUser(user);
        return Result.success("更新成功");
    }

    @GetMapping("getAvailableSupervisor")
    @ApiOperation("获取可用咨询师,目前只能根据排班判断，无法根据繁忙程度判断")
    public Page<Supervisor> getAvailableCounselor(@RequestParam("page") Integer page,
                                                 @RequestParam("size") Integer size,
                                                 @RequestParam("order") String order) {
        return supervisorService.getAvailableSupervisor(page, size, order);
    }

}
