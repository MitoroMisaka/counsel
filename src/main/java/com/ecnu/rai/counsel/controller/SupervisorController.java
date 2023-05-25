package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.entity.Supervisor;
import com.ecnu.rai.counsel.service.SupervisorService;
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

}
