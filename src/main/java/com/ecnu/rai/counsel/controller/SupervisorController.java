package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.entity.Supervisor;
import com.ecnu.rai.counsel.mapper.SuperviseMapper;
import com.ecnu.rai.counsel.service.SupervisorService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/supervisor")
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;

    @Autowired
    private SuperviseMapper superviseMapper;

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


    @PostMapping("/getAcounselors")
    @ApiOperation("查看绑定咨询师")
    public Result askForBinding(@RequestBody Supervisor supervisor) {
        List<HashMap<String,Object>> Acounselors = superviseMapper.selectBindedCounselor(supervisor);
        return Result.success("获取成功",Acounselors);
    }

}
