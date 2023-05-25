package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.entity.Arrange;
import com.ecnu.rai.counsel.entity.Conversation;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.response.ConsultInfo;
import com.ecnu.rai.counsel.service.ConversationService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/conversation")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @GetMapping("/id")
    @ApiOperation("根据ID获取会话信息")
    public Result getConversationbyID(@RequestParam("id") Long id) {
        Conversation conversation = conversationService.findConversationByID(id);
        return Result.success("获取成功", conversation);
    }

    @GetMapping("/insert")
    @ApiOperation("插入会话信息")
    public Result insertConversationbyID(@Valid @RequestBody Conversation conversation) {
        Conversation new_conversation = conversationService.insertConversationByID(conversation);
        return Result.success("获取成功", new_conversation);
    }

    @PostMapping("/update")
    @ApiOperation("更新会话信息")
    public Result updateConversation(@RequestParam("conversation") Conversation conversation) {
        Conversation new_arrange = conversationService.updateConversation(conversation);
        return Result.success("获取成功", new_arrange);
    }

    @GetMapping("/user")
    @ApiOperation("根据用户ID获取会话信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "order", value = "排序", required = true, dataType = "String")
    })
    public Page<Conversation> getConversationbyUser(@RequestParam("user") Long user,
                                               @RequestParam("page") Integer page,
                                               @RequestParam("size") Integer size,
                                               @RequestParam("order") String order) {
        return conversationService.findConversationByUser(user, page, size, order);
    }

    @GetMapping("/counselor")
    @ApiOperation("根据咨询师ID获取会话信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "counselor", value = "咨询师id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "order", value = "排序", required = true, dataType = "String")
    })
    public Page<Conversation> getConversationbyCounselor(@RequestParam("counselor") Long counselor,
                                                       @RequestParam("page") Integer page,
                                                       @RequestParam("size") Integer size,
                                                       @RequestParam("order") String order) {
        return conversationService.findConversationByCounselor(counselor, page, size, order);
    }

    @GetMapping("/counselorUserDate")
    @ApiOperation("根据咨询师ID, 咨询人ID, 日期获取会话信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "counselor", value = "咨询师id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "user", value = "咨询人", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "date", value = "咨询日期", required = true, dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "order", value = "排序", required = true, dataType = "String")
    })
    public Page<Conversation> getConversationbyCounselorUserDate(@RequestParam("counselor") Long counselor,
                                                                 @RequestParam("user") Long user,
                                                                 @RequestParam("date") Date date,
                                                                 @RequestParam("page") Integer page,
                                                                 @RequestParam("size") Integer size,
                                                                 @RequestParam("order") String order) {
        return conversationService.findConversationByCounselorUserDate(counselor, user, date, page, size, order);
    }

    @GetMapping("/counselorConsultInfo")
    @ApiOperation("根据咨询师ID获取累计咨询时长, 今日咨询时长, 今日咨询数, 当前会话数")
    @ApiImplicitParam(name = "counselor", value = "咨询师id", required = true, dataType = "Long")
    public Result getConsultInfo(@RequestParam("counselor") Long counselor) {
        return Result.success("获取成功", conversationService.findConsultInfobyCounselor(counselor));
    }
}
