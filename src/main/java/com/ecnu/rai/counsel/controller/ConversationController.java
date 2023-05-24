package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.entity.Arrange;
import com.ecnu.rai.counsel.entity.Conversation;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.service.ConversationService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Page<Conversation> getArrangebyUser(@RequestParam("user") Long user,
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
    public Page<Conversation> getArrangebyConversation(@RequestParam("counselor") Long counselor,
                                                       @RequestParam("page") Integer page,
                                                       @RequestParam("size") Integer size,
                                                       @RequestParam("order") String order) {
        return conversationService.findConversationByCounselor(counselor, page, size, order);
    }

}
