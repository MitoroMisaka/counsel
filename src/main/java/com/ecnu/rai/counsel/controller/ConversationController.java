package com.ecnu.rai.counsel.controller;

import com.alibaba.fastjson.JSON;
import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.dao.group.GetGroupMsgResponse;
import com.ecnu.rai.counsel.dao.group.RspMsg;
import com.ecnu.rai.counsel.dao.single.IMRequest;
import com.ecnu.rai.counsel.dao.single.Message;
import com.ecnu.rai.counsel.entity.Conversation;
import com.ecnu.rai.counsel.mapper.ConversationMapper;
import com.ecnu.rai.counsel.service.ConversationService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/conversation")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private ConversationMapper conversationMapper;

    @GetMapping("/id")
    @ApiOperation("根据ID获取会话信息")
    public Result getConversationbyID(@RequestParam("id") Long id) {
        Conversation conversation = conversationService.findConversationByID(id);
        return Result.success("获取成功", conversation);
    }

    @PostMapping("/start")
    @ApiOperation("开始会话(就是建个表,给出咨询师和用户的姓名就行)")
    public Result insertConversation(@Valid @RequestBody Conversation conversation) {
        Integer maxConsult = conversationMapper.getMaxConsult(conversation.getCounselor());
        if(maxConsult.equals(conversationMapper.getConsultNum(conversation.getCounselor()))){
            Result.fail("咨询师咨询次数已达上限");
        }
        conversation.setStatus("STARTED");
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

    @PostMapping("/save_roam_msg")
    @ApiOperation(value = "存储单聊信息(已弃用)", notes = "save the conversation ")
    @ResponseBody
    public Result saveHistory(@Valid @RequestBody IMRequest request) {
        System.out.println(request);
        List<Message> messages = request.getMsgList();
        for (Message message : messages) {
            if (message.getFromAccount().equals(request.getOperatorAccount())) {
                message.setFromAccount(request.getOperatorAccountReal());
                message.setToAccount(request.getPeerAccountReal());
            } else if (message.getFromAccount().equals(request.getPeerAccount())) {
                message.setFromAccount(request.getPeerAccountReal());
                message.setToAccount(request.getOperatorAccountReal());
            }
        }
        //convert message into JSON string
        String history = JSON.toJSONString(messages);
        System.out.println(history);
        Conversation conversation = Conversation.builder()
                .id(null)
                .createTime(LocalDateTime.now())
                .creator(request.getOperatorAccountReal())
                .lastUpdateTime(LocalDateTime.now())
                .lastUpdater(request.getOperatorAccountReal())
                .year(request.getMinTime().toLocalDateTime().getYear())
                .month(request.getMinTime().toLocalDateTime().getMonth())
                .day(request.getMinTime().toLocalDateTime().getDayOfMonth())
                .startTime(request.getMinTime())
                .endTime(request.getMaxTime())
                .user(request.getPeerAccountReal())
                .counselor(request.getOperatorAccountReal())
                .status("FINISHED")
                .visitorName(request.getPeerAccountReal())
                .evaluate(0)
                .conversationType("C2C")
                .message(history)
                .build();
        conversationMapper.insertConversation(conversation);
        return Result.success("save and insert conversation ");
    }

    @PostMapping("/save_group_msg")
    @ApiOperation(value = "存储群聊信息/结束会话", notes = "save the conversation ")
    @ResponseBody
    public Result saveGroupMsg(@Valid @RequestBody GetGroupMsgResponse getGroupMsgResponse){
        System.out.println(getGroupMsgResponse);
        List<RspMsg> messages = getGroupMsgResponse.getRspMsgList();
        for(RspMsg message : messages){
            if(message.getFromAccount().equals(getGroupMsgResponse.getUserid())){
                message.setFromAccount("用户："+ getGroupMsgResponse.getUsername());
            }
            if(message.getFromAccount().equals(getGroupMsgResponse.getCounselorid())){
                message.setFromAccount("咨询师："+ getGroupMsgResponse.getCounselorname());
            }
        }
        String history = JSON.toJSONString(messages);
        Conversation conversation = Conversation.builder()
                .id(null)
                .createTime(LocalDateTime.now())
                .creator(getGroupMsgResponse.getCounselorname())
                .lastUpdateTime(LocalDateTime.now())
                .lastUpdater(getGroupMsgResponse.getCounselorname())
                .year(getGroupMsgResponse.getStartTime().toLocalDateTime().getYear())
                .month(getGroupMsgResponse.getStartTime().toLocalDateTime().getMonth())
                .day(getGroupMsgResponse.getStartTime().toLocalDateTime().getDayOfMonth())
                .startTime(getGroupMsgResponse.getStartTime())
                .endTime(getGroupMsgResponse.getEndTime())
                .user(getGroupMsgResponse.getUsername())
                .counselor(getGroupMsgResponse.getCounselorname())
                .status("FINISHED")
                .visitorName(getGroupMsgResponse.getUsername())
                .evaluate(0)
                .conversationType("C2C")
                .message(history)
                .build();
        conversationMapper.updateConversation(conversation);
        return Result.success("save and insert conversation ");
    }

    @GetMapping("/get_group_msg")
    @ApiOperation(value = "获取群聊信息", notes = "get the conversation ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "counselor_name", value = "咨询师姓名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "user_name", value = "咨询人姓名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "order", value = "排序", required = true, dataType = "String")
    })
    public Result getGroupMsg(@RequestParam("counselor_name") String counselor_name,
                              @RequestParam("user_name") String user_name,
                              @RequestParam("page") Integer page,
                              @RequestParam("size") Integer size,
                              @RequestParam("order") String order) {
        return Result.success("获取成功", conversationService.findGroupMsgByCounselorUser(counselor_name, user_name, page, size, order));
    }

}
