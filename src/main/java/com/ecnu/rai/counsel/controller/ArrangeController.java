package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.dao.CounselorBasicInfo;
import com.ecnu.rai.counsel.dao.SupervisorBasicInfo;
import com.ecnu.rai.counsel.entity.Arrange;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.response.DayNum;
import com.ecnu.rai.counsel.service.ArrangeService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/arrange")
public class ArrangeController {

    @Autowired
    private ArrangeService arrangeService;

    @RequiresRoles("admin")
    @GetMapping("/id")
    @ApiOperation("根据ID获取排班信息")
    public Result getArrangebyID(@RequestParam("id") Long id) {
        Arrange arrange = arrangeService.findArrangeByID(id);
        return Result.success("获取成功", arrange);
    }

    @PostMapping("/insert")
    //添加接口的信息,id, creator,updater,updateTime,createTime,year,month,day可以不用给出
    @ApiOperation("添加排班信息,给出startTime endTime user role weekday 即可")
    public Result addArrange(@RequestBody Arrange arrange){
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        arrange.setCreator(currentUser.getId());
        arrange.setLastUpdater(currentUser.getId());
        arrange.setLastUpdateTime(LocalDateTime.now());
        arrange.setCreateTime(LocalDateTime.now());
        arrange.setYear(LocalDateTime.now().getYear());
        arrange.setMonth(LocalDateTime.now().getMonthValue());
        arrange.setDay(LocalDateTime.now().getDayOfMonth());
        arrange.setLocalDate(LocalDateTime.now());
        arrangeService.addArrange(arrange);
        return Result.success("添加成功");
    }

    @RequiresRoles("admin")
    @PostMapping("/update")
    @ApiOperation("更新排班信息")
    public Result updateArrange(@RequestParam("arrange") Arrange arrange) {
        Arrange new_arrange = arrangeService.updateArrange(arrange);
        return Result.success("获取成功", new_arrange);
    }

    @GetMapping("/user")
    @ApiOperation("根据用户ID获取排班信息")
    public Result getArrangebyUser(@RequestParam("user") Long user) {

        // 权限控制，机构管理员可以获取所有人的排班信息，咨询师或督导本人只可以获取自己的排班信息
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (!currentUser.getRole().equals("admin")) {
            if (currentUser.getId() != user) {
                return Result.fail("You have no permission to query this user");
            }
        }

        List<Arrange> arranges = arrangeService.findArrangeByUser(user);
        return Result.success("获取成功", arranges);
    }

    @GetMapping("/userYearMonth")
    @ApiOperation("根据用户ID, 年份, 月份获取排班信息")
    public Result getArrangebyUserYearMonth(@RequestParam("user") Long user,
                                            @RequestParam("year") Integer year,
                                            @RequestParam("month") Integer month) {

        // 权限控制，机构管理员可以获取所有人的排班信息，咨询师或督导本人只可以获取自己的排班信息
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (!currentUser.getRole().equals("admin")) {
            if (currentUser.getId() != user) {
                return Result.fail("You have no permission to query this user");
            }
        }

        List<Arrange> arranges = arrangeService.findArrangeByUserYearMonth(user, year, month);
        return Result.success("获取成功", arranges);
    }

    @RequiresRoles("admin")
    @GetMapping("/CounselorInfobyMonth")
    @ApiOperation("获取某年某月中每一天的排班咨询师总数")
    public Result getCounselorInfobyMonth(@RequestParam("year") Integer year,
                                            @RequestParam("month") Integer month) {
        List<DayNum> arranges = arrangeService.findCounselorInfoByMonth(year, month);
        return Result.success("获取成功", arranges);
    }

    @RequiresRoles("admin")
    @GetMapping("/SupervisorInfobyMonth")
    @ApiOperation("获取某年某月中每一天的排班督导总数")
    public Result getSupervisorInfobyMonth(@RequestParam("year") Integer year,
                                            @RequestParam("month") Integer month) {
        List<DayNum> arranges = arrangeService.findSupervisorInfoByMonth(year, month);
        return Result.success("获取成功", arranges);
    }

    @RequiresRoles("admin")
    @GetMapping("/counselorListByDay")
    @ApiOperation("获取某天有排班的咨询师的基本信息列表")
    public Result getCounselorListByDay(@RequestParam("year") Integer year,
                                        @RequestParam("month") Integer month,
                                        @RequestParam("day") Integer day) {
        List<CounselorBasicInfo> counselorListByDay = arrangeService.findCounselorListByDay(year, month, day);
        return Result.success("获取成功", counselorListByDay);
    }

    @RequiresRoles("admin")
    @GetMapping("/supervisorListByDay")
    @ApiOperation("获取某天有排班的督导的基本信息列表")
    public Result getSupervisorListByDay(@RequestParam("year") Integer year,
                                        @RequestParam("month") Integer month,
                                        @RequestParam("day") Integer day) {
        List<SupervisorBasicInfo> supervisorListByDay = arrangeService.findSupervisorListByDay(year, month, day);
        return Result.success("获取成功", supervisorListByDay);
    }

}
