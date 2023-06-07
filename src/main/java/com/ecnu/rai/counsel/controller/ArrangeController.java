package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.entity.Arrange;
import com.ecnu.rai.counsel.response.DayNum;
import com.ecnu.rai.counsel.service.ArrangeService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/update")
    @ApiOperation("更新排班信息")
    public Result updateArrange(@RequestParam("arrange") Arrange arrange) {
        Arrange new_arrange = arrangeService.updateArrange(arrange);
        return Result.success("获取成功", new_arrange);
    }

    @GetMapping("/user")
    @ApiOperation("根据用户ID获取排班信息")
    public Result getArrangebyUser(@RequestParam("user") Long user) {
        List<Arrange> arranges = arrangeService.findArrangeByUser(user);
        return Result.success("获取成功", arranges);
    }

    @GetMapping("/userYearMonth")
    @ApiOperation("根据用户ID, 年份, 月份获取排班信息")
    public Result getArrangebyUserYearMonth(@RequestParam("user") Long user,
                                            @RequestParam("year") Integer year,
                                            @RequestParam("month") Integer month) {
        List<Arrange> arranges = arrangeService.findArrangeByUserYearMonth(user, year, month);
        return Result.success("获取成功", arranges);
    }

    @GetMapping("/counselorYearMonthInfo")
    @ApiOperation("获取某年某月中每一天的排班咨询师总数")
    public Result getArrangeCounselorInfobyYearMonth(@RequestParam("year") Integer year,
                                            @RequestParam("month") Integer month) {
        List<DayNum> arranges = arrangeService.findArrangeCounselorInfoByYearMonthDay(year, month);
        return Result.success("获取成功", arranges);
    }

    @GetMapping("/supervisorYearMonthInfo")
    @ApiOperation("获取某年某月有排班的咨询师总数")
    public Result getArrangeSupervisorInfobyYearMonth(@RequestParam("year") Integer year,
                                            @RequestParam("month") Integer month) {
        List<DayNum> arranges = arrangeService.findArrangeSupervisorInfoByYearMonthDay(year, month);
        return Result.success("获取成功", arranges);
    }
}
