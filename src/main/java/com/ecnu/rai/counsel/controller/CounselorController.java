package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.entity.Counselor;

import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.mapper.UserMapper;
import com.ecnu.rai.counsel.mapper.SuperviseMapper;
import com.ecnu.rai.counsel.service.CounselorService;
import com.ecnu.rai.counsel.util.PasswordUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/counselor")
public class CounselorController {

    @Autowired
    private CounselorService counselorService;

    @Autowired
    private UserMapper userMapper;
  
  @Autowired
  private SuperviseMapper superviseMapper;

    @PostMapping("/add")
    @ApiOperation("添加咨询师(弃用，参考AccountController里面接口)")
    public Result addCounselor(@RequestBody Counselor counselor){
        //检查所有counselor的属性是否有问题并返回错误信息
        //姓名必须是大小写字母或汉字 2-16位
        //用户名只能是大小写字母数字和下划线组成。 2-32 位
        //本系统的角色分为admin, visitor , counselor 和 supervisor.
        //其中admin是系统管理员，visitor是访客，counselor是咨询师，supervisor是督导。
        //角色必须是counselor
        //电话号码必须是11位
        //gender只能为男或女
        //密码必须是6-20位
        if(counselor.getName()!=null && !counselor.getName().matches("^[a-zA-Z\\u4e00-\\u9fa5]{2,16}$")){
            return Result.fail("姓名必须是大小写字母或汉字 2-16位");
        }
        if(counselor.getUsername()!=null && !counselor.getUsername().matches("^[a-zA-Z0-9_]{2,32}$")){
            return Result.fail("用户名只能是大小写字母数字和下划线组成。 2-32 位");
        }
        if(counselor.getRole()!=null && !counselor.getRole().equals("counselor")){
            return Result.fail("角色必须是counselor");
        }
        if(counselor.getPhone()!=null && !counselor.getPhone().matches("^[0-9]{11}$")){
            return Result.fail("电话号码必须是11位");
        }
        if(counselor.getGender()!=null && !counselor.getGender().equals("男") && !counselor.getGender().equals("女")){
            return Result.fail("gender只能为男或女");
        }
        if(counselor.getPassword()!=null && !counselor.getPassword().matches("^[a-zA-Z0-9_]{6,20}$")){
            return Result.fail("密码必须是6-20位");
        }
        //检查用户名是否已经存在
        User user = userMapper.findByUsername(counselor.getUsername());
        if(user!=null){
            return Result.fail("用户名已存在");
        }
        User user1 = new User();
        user1.setUsername(counselor.getUsername());
        user1.setPassword(counselor.getPassword());
        user1.setRole(counselor.getRole());
        user1.setName(counselor.getName());
        userMapper.insertUser(user1);
        Long id = userMapper.findByUsername(counselor.getUsername()).getId();
        counselor.setId(id);
        counselor.setPassword(PasswordUtil.convert(counselor.getPassword()));
        counselorService.addCounselor(counselor);
        return Result.success("添加成功");
    }
  
    @GetMapping("/info")
    @ApiOperation("获取咨询师基本信息")
    public Result getCounselorInfo(@RequestParam("id") Long id) {
        Counselor counselor = counselorService.findCounselorByID(id);
        return Result.success("获取成功", counselor);
    }

    @PostMapping("/update")
    @ApiOperation("更新咨询师基本信息")
    public Result updateCounselorInfo(@RequestBody Counselor counselor) {
        //检查所有counselor的属性是否有问题并返回错误信息
        //姓名必须是大小写字母或汉字 2-16位
        //用户名只能是大小写字母数字和下划线组成。 2-32 位
        //本系统的角色分为admin, visitor , counselor 和 supervisor.
        //其中admin是系统管理员，visitor是访客，counselor是咨询师，supervisor是督导。
        //角色必须是counselor
        //电话号码必须是11位
        //gender只能为男或女
        //密码必须是6-20位
        if(counselor.getName()!=null && !counselor.getName().matches("^[a-zA-Z\\u4e00-\\u9fa5]{2,16}$")){
            return Result.fail("姓名必须是大小写字母或汉字 2-16位");
        }
        if(counselor.getUsername()!=null && !counselor.getUsername().matches("^[a-zA-Z0-9_]{2,32}$")){
            return Result.fail("用户名只能是大小写字母数字和下划线组成。 2-32 位");
        }
        if(counselor.getRole()!=null && !counselor.getRole().equals("counselor")){
            return Result.fail("角色必须是counselor");
        }
        if(counselor.getPhone()!=null && !counselor.getPhone().matches("^1[0-9]{10}$")){
            return Result.fail("电话号码必须是11位");
        }
        if(counselor.getGender()!=null && !counselor.getGender().equals("男") && !counselor.getGender().equals("女")){
            return Result.fail("gender只能为男或女");
        }
        if(counselor.getPassword()!=null && !counselor.getPassword().matches("^.{6,20}$")){
            return Result.fail("密码必须是6-20位");
        }
        counselorService.updateCounselor(counselor);
        User user = new User();
        user.setId(counselor.getId());
        user.setName(counselor.getName());
        user.setUsername(counselor.getUsername());
        user.setRole(counselor.getRole());
        userMapper.updateUser(user);
        return Result.success("更新成功");
    }


    @PostMapping("/getAsupervisors")
    @ApiOperation("查看绑定督导")
    public Result askForBinding(@RequestBody Counselor counselor) {
        List<HashMap<String,Object>> Asupervisors = superviseMapper.selectBindedSupervisor(counselor);
        return Result.success("获取成功",Asupervisors);
    }

}
