package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervisor;
import com.ecnu.rai.counsel.service.SupervisorService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supervisor")
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;

    @GetMapping("/info")
    @ApiOperation("获取督导基本信息")
    public Result getCounselorInfo(@RequestParam("id") Long id) {
        Supervisor supervisor = supervisorService.findSupervisorByID(id);
        return Result.success("获取成功", supervisor);
    }

    @PostMapping("/update")
    @ApiOperation("更新督导基本信息")
    public Result updateCounselorInfo(@RequestBody Supervisor supervisor) {
        supervisorService.updateSupervisor(supervisor);
        return Result.success("更新成功");
    }

    @GetMapping("/getCounselors")
    @ApiOperation("获取督导所管理的咨询师")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "督导id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "order", value = "排序", required = true, dataType = "String")
    })
    public Page<Counselor> getAvailableCounselor(@RequestParam("id") Long id,
                                                 @RequestParam("page") Integer page,
                                                 @RequestParam("size") Integer size,
                                                 @RequestParam("order") String order) {
        return supervisorService.getAvailableCounselor(id, page, size, order);
    }

}
