package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.mapper.SuperviseMapper;
import com.ecnu.rai.counsel.service.CounselorService;
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
    private SuperviseMapper superviseMapper;

    @GetMapping("/info")
    @ApiOperation("获取咨询师基本信息")
    public Result getCounselorInfo(@RequestParam("id") Long id) {
        Counselor counselor = counselorService.findCounselorByID(id);
        return Result.success("获取成功", counselor);
    }

    @PostMapping("/update")
    @ApiOperation("更新咨询师基本信息")
    public Result updateCounselorInfo(@RequestBody Counselor counselor) {
        counselorService.updateCounselor(counselor);
        return Result.success("更新成功");
    }


    @PostMapping("/getAsupervisors")
    @ApiOperation("查看绑定督导")
    public Result askForBinding(@RequestBody Counselor counselor) {
        List<HashMap<String,Object>> Asupervisors = superviseMapper.selectBindedSupervisor(counselor);
        return Result.success("获取成功",Asupervisors);
    }

}
