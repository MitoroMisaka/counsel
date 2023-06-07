package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervisor;
import com.ecnu.rai.counsel.mapper.SuperviseMapper;
import com.ecnu.rai.counsel.service.CounselorService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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


    @GetMapping("/getSupervisors")
    @ApiOperation("获取咨询师绑定的督导")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "咨询师id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "order", value = "排序", required = true, dataType = "String")
    })
    public Page<Supervisor> getAvailableCounselor(@RequestParam("id") Long id,
                                                  @RequestParam("page") Integer page,
                                                  @RequestParam("size") Integer size,
                                                  @RequestParam("order") String order) {
        return counselorService.getAvailableSupervisor(id, page, size, order);
    }


}
