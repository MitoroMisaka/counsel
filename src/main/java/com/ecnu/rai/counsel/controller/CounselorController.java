package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.dao.CounselorMonthlyStar;
import com.ecnu.rai.counsel.dao.CounselorMonthlyWork;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.mapper.*;
import com.ecnu.rai.counsel.service.CounselorService;
import com.ecnu.rai.counsel.service.SupervisorService;
import com.ecnu.rai.counsel.util.PasswordUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/counselor")
public class CounselorController {

    @Autowired
    private CounselorService counselorService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CounselorMapper counselorMapper;

    @Autowired
    private ConversationMapper conversationMapper;

    @Autowired
    private SupervisorService supervisorService;

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

        // 权限控制，只有机构管理员 和 该咨询师本人可以获取
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (!currentUser.getRole().equals("admin")) {
            if (currentUser.getId() != id) {
                return Result.fail("You have no permission to query this counselor");
            }
        }

        Counselor counselor = counselorService.findCounselorByID(id);
        return Result.success("获取成功", counselor);
    }

    @RequiresRoles("admin")
    @GetMapping("/getCounselorRankingByWork")
    @ApiOperation("获取咨询师月咨询数量排名")
    @ApiImplicitParam(name = "listSize", value = "排名列表长度", required = true, dataType = "Integer")
    public Result getCounselorRankingByWork(@RequestParam Integer len) {
        List<CounselorMonthlyWork> counselorRanking = counselorService.getCounselorRankingByWork(len);
        return Result.success("获取成功", counselorRanking);
    }

    @RequiresRoles("admin")
    @GetMapping("/getCounselorRankingByStar")
    @ApiOperation("获取咨询师月好评数量排名")
    @ApiImplicitParam(name = "listSize", value = "排名列表长度", required = true, dataType = "Integer")
    public Result getCounselorRankingByStar(@RequestParam Integer len) {
        List<CounselorMonthlyStar> counselorRanking = counselorService.getCounselorRankingByStar(len);
        return Result.success("获取成功", counselorRanking);
    }

    @PostMapping("/update")
    @ApiOperation("更新咨询师基本信息，无参数校验版本(暂时弃用)")
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

    @GetMapping("/list")
    @ApiOperation("获取咨询师列表")
    public Result getCounselorList(@RequestParam("page") Integer page,
                                   @RequestParam("size") Integer size,
                                   @RequestParam("order") String order) {
        return Result.success("获取成功", counselorService.getCounselorList(page, size, order));
    }

    @GetMapping("getAvailableCounselor")
    @ApiOperation("获取可用咨询师,可根据排班,是否繁忙查询(需要先登录)")
    public Result getAvailableCounselor(@RequestParam("page") Integer page,
                                        @RequestParam("size") Integer size,
                                        @RequestParam("order") String order,
                                        @RequestParam("token") String token) {
        return Result.success("获取成功",counselorService.getAvailableCounselor(page, size, order, token));
    }

    @GetMapping("getCounselorByBusy")
    @ApiOperation("获取咨询师及其繁忙状态")
    public Result getCounselorByBusy(@RequestParam("page") Integer page,
                                        @RequestParam("size") Integer size,
                                        @RequestParam("order") String order) {


        return Result.success("获取成功",counselorService.getAvailableCounselorByBusy(page, size, order));
    }

    @GetMapping("getBasicStatInfo")
    @ApiOperation("获取基本资询统计数据")
    public Result getBasicStatInfo() {


        return Result.success("获取成功",counselorService.getBasicStatInfo());
    }

    @GetMapping("getNumByWeek")
    @ApiOperation("获取周咨询统计数据（最近一周）")
    public Result getNumByWeek() {
        return Result.success("获取成功",counselorService.getNumByWeek());
    }

    @GetMapping("getNumByHours")
    @ApiOperation("获取今日时辰统计数据")
    public Result getNumByHours() {
        return Result.success("获取成功",counselorService.getNumByHours());
    }

    @GetMapping("getSupervisorByBusy")
    @ApiOperation("获取督导及其繁忙状态")
    public Result getSupervisorByBusy(@RequestParam("page") Integer page,
                                      @RequestParam("size") Integer size,
                                      @RequestParam("order") String order) {


        return Result.success("获取成功",supervisorService.getAvailableSupervisorByBusy(page, size, order));
    }
}
