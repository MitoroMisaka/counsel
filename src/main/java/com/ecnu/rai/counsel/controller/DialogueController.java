package com.ecnu.rai.counsel.controller;

import com.alibaba.fastjson.JSON;
import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.dao.group.GetGroupMsgResponse;
import com.ecnu.rai.counsel.dao.group.GroupMsg;
import com.ecnu.rai.counsel.dao.group.RspMsg;
import com.ecnu.rai.counsel.dao.single.IMRequest;
import com.ecnu.rai.counsel.dao.single.Message;
import com.ecnu.rai.counsel.entity.Dialogue;
import com.ecnu.rai.counsel.mapper.DialogueMapper;
import com.ecnu.rai.counsel.mapper.CounselorMapper;
import com.ecnu.rai.counsel.mapper.UserMapper;
import com.ecnu.rai.counsel.mapper.UserSigMapper;
import com.ecnu.rai.counsel.service.DialogueService;
import com.ecnu.rai.counsel.util.PinyinUtil;
import com.google.gson.Gson;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/dialogue")
public class DialogueController {
    @Autowired
    private IMController imController;

    @Autowired
    private DialogueService dialogueService;

    @Autowired
    private DialogueMapper dialogueMapper;

    @Autowired
    private CounselorMapper counselorMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserSigMapper userSigMapper;

    @GetMapping("/id")
    @ApiOperation("根据ID获取会话信息")
    public Result getDialoguebyID(@RequestParam("id") Long id) {
        Dialogue dialogue = dialogueService.findDialogueByID(id);
        return Result.success("获取成功", dialogue);
    }

    @PostMapping("/start")
    @ApiOperation("开始会话(就是建个表,给出咨询师和用户的姓名就行)")
    public Result insertdialogue(@Valid @RequestBody Dialogue dialogue) throws IOException {
        Integer maxConsult = dialogueMapper.getMaxConsult(dialogue.getCounselor());
        if(maxConsult.equals(dialogueMapper.getConsultNum(dialogue.getCounselor()))){
            Result.fail("咨询师咨询次数已达上限");
        }
        dialogue.setStatus("STARTED");
        Dialogue new_dialogue = dialogueService.insertDialogueByID(dialogue);
        String counselor_name = userMapper.findNameById(dialogue.getCounselor());
        String supervisor_name = userMapper.findNameById(dialogue.getSupervisor());
        return Result.success("获取成功", new_dialogue);
    }

    @PostMapping("/update")
    @ApiOperation("(弃用接口)更新会话信息")
    public Result updatedialogue(@RequestParam("dialogue") Dialogue dialogue) {
        Dialogue new_arrange = dialogueService.updateDialogue(dialogue);
        return Result.success("获取成功", new_arrange);
    }

    @GetMapping("/user")
    @ApiOperation("根据督导ID获取会话信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "order", value = "排序", required = true, dataType = "String")
    })
    public Page<Dialogue> getdialoguebyUser(@RequestParam("user") Long user,
                                                            @RequestParam("page") Integer page,
                                                            @RequestParam("size") Integer size,
                                                            @RequestParam("order") String order) {
        return dialogueService.findDialogueBySupervisor(user, page, size, order);
    }

    @GetMapping("/counselor")
    @ApiOperation("根据咨询师ID获取会话信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "counselor", value = "咨询师id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "order", value = "排序", required = true, dataType = "String")
    })
    public Page<Dialogue> getdialoguebyCounselor(@RequestParam("counselor") Long counselor,
                                                                 @RequestParam("page") Integer page,
                                                                 @RequestParam("size") Integer size,
                                                                 @RequestParam("order") String order) {
        return dialogueService.findDialogueByCounselor(counselor, page, size, order);
    }

    @GetMapping("/BothDate")
    @ApiOperation("根据咨询师ID, 督导ID, 日期获取会话信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "counselor", value = "咨询师id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "user", value = "咨询人", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "date", value = "咨询日期", required = true, dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "order", value = "排序", required = true, dataType = "String")
    })
    public Page<Dialogue> getdialoguebyCounselorUserDate(@RequestParam("counselor") Long counselor,
                                                                 @RequestParam("user") Long user,
                                                                 @RequestParam("date") Date date,
                                                                 @RequestParam("page") Integer page,
                                                                 @RequestParam("size") Integer size,
                                                                 @RequestParam("order") String order) {
        return dialogueService.findDialogueByBothDate(counselor, user, date, page, size, order);
    }


    @GetMapping("/supervisorConsultInfo")
    @ApiOperation("根据督导ID获取累计咨询时长, 今日咨询时长, 今日咨询数, 当前会话数")
    @ApiImplicitParam(name = "supervisor", value = "咨询师id", required = true, dataType = "Long")
    public Result getConsultInfoSupervisor(@RequestParam("supervisor") Long supervisor) {
        return Result.success("获取成功", dialogueService.findConsultInfobySupervisor(supervisor));
    }

    @PostMapping("/save_roam_msg")
    @ApiOperation(value = "存储单聊信息", notes = "save the dialogue ")
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
        Dialogue dialogue = Dialogue.builder()
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
                .counselor(request.getPeerAccountReal())
                .supervisor(request.getOperatorAccountReal())
                .status("FINISHED")
                .visitorName(request.getPeerAccountReal())
                .message(history)
                .build();
        dialogueMapper.insertDialogue(dialogue);
        return Result.success("save and insert dialogue ");
    }
}
