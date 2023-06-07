package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.mapper.UserMapper;
import com.ecnu.rai.counsel.service.CounselorService;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/counselor")
public class CounselorController {

    @Autowired
    private CounselorService counselorService;

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

    @PostMapping("/update")
    @ApiOperation("更新咨询师基本信息，无参数校验版本(暂时弃用)")
    public Result updateCounselorInfo(@RequestBody Counselor counselor) {
        counselorService.updateCounselor(counselor);
        return Result.success("更新成功");
    }

    @GetMapping("getAvailableCounselor")
    @ApiOperation("获取可用咨询师,目前只能根据排班判断，无法根据繁忙程度判断")
    public Page<Counselor> getAvailableCounselor(@RequestParam("page") Integer page,
                                                 @RequestParam("size") Integer size,
                                                 @RequestParam("order") String order) {
        return counselorService.getAvailableCounselor(page, size, order);
    }

}
