package com.ecnu.rai.counsel.controller;

import com.alibaba.fastjson.JSON;
import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.dao.DialogueRequest;
import com.ecnu.rai.counsel.dao.group.GetGroupMsgResponse;
import com.ecnu.rai.counsel.dao.group.GroupMsg;
import com.ecnu.rai.counsel.dao.group.GroupMsgBody;
import com.ecnu.rai.counsel.dao.group.RspMsg;
import com.ecnu.rai.counsel.dao.single.IMRequest;
import com.ecnu.rai.counsel.dao.single.Message;
import com.ecnu.rai.counsel.dao.single.MsgBody;
import com.ecnu.rai.counsel.entity.Dialogue;
import com.ecnu.rai.counsel.mapper.DialogueMapper;
import com.ecnu.rai.counsel.mapper.CounselorMapper;
import com.ecnu.rai.counsel.mapper.UserMapper;
import com.ecnu.rai.counsel.mapper.UserSigMapper;
import com.ecnu.rai.counsel.service.DialogueService;
import com.ecnu.rai.counsel.util.PinyinUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Iterator;
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
//        Integer maxConsult = dialogueMapper.getMaxConsult(dialogue.getCounselor());
//        if(maxConsult.equals(dialogueMapper.getConsultNum(dialogue.getCounselor()))){
//            Result.fail("咨询师咨询次数已达上限");
//        }
        dialogue.setStatus("STARTED");
        dialogue.setVisitorName(dialogue.getCounselor());
        dialogue.setCreateTime(LocalDateTime.now());
        dialogue.setLastUpdateTime(LocalDateTime.now());
        dialogue.setYear(LocalDateTime.now().getYear());
        dialogue.setMonth(LocalDateTime.now().getMonth());
        dialogue.setDay(LocalDateTime.now().getDayOfMonth());
        dialogue.setStartTime(Timestamp.valueOf(LocalDateTime.now()));
        Long supervisor_id = userMapper.findIdByName(dialogue.getSupervisor());
        Long counselor_id = userMapper.findIdByName(dialogue.getCounselor());
        dialogue.setSupervisor(supervisor_id.toString());
        dialogue.setCounselor(counselor_id.toString());
        dialogue.setCreator(counselor_id.toString());
        dialogue.setLastUpdater(counselor_id.toString());
        Dialogue new_dialogue = dialogueService.insertDialogueByID(dialogue);
//        String counselor_name = userMapper.findNameById(dialogue.getCounselor());
//        String supervisor_name = userMapper.findNameById(dialogue.getSupervisor());
        return Result.success("插入成功", new_dialogue);
    }

    @PostMapping("/update")
    @ApiOperation("(弃用接口)更新会话信息")
    public Result updatedialogue(@RequestParam("dialogue") Dialogue dialogue) {
        Dialogue new_arrange = dialogueService.updateDialogue(dialogue);
        return Result.success("获取成功", new_arrange);
    }

    @GetMapping("/supervisor")
    @ApiOperation("根据督导ID获取会话信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "supervisorId", value = "用户id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "order", value = "排序", required = true, dataType = "String")
    })
    public Page<Dialogue> getdialoguebyUser(@RequestParam("supervisorId") Long supervisorId,
                                                            @RequestParam("page") Integer page,
                                                            @RequestParam("size") Integer size,
                                                            @RequestParam("order") String order) {
        return dialogueService.findDialogueBySupervisor(supervisorId, page, size, order);
    }

    @GetMapping("/counselor")
    @ApiOperation("根据咨询师ID获取会话信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "counselorId", value = "咨询师id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "order", value = "排序", required = true, dataType = "String")
    })
    public Page<Dialogue> getdialoguebyCounselor(@RequestParam("counselorId") Long counselorId,
                                                                 @RequestParam("page") Integer page,
                                                                 @RequestParam("size") Integer size,
                                                                 @RequestParam("order") String order) {
        return dialogueService.findDialogueByCounselor(counselorId, page, size, order);
    }

    @GetMapping("/BothDate")
    @ApiOperation("根据咨询师ID, 督导ID, 日期获取会话信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "counselorId", value = "咨询师id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "supervisorId", value = "督导", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "date", value = "咨询日期", required = true, dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "order", value = "排序", required = true, dataType = "String")
    })
    public Page<Dialogue> getdialoguebyCounselorUserDate(@RequestParam("counselorId") Long counselorId,
                                                                 @RequestParam("supervisorId") Long supervisorId,
                                                                 @RequestParam("date") Date date,
                                                                 @RequestParam("page") Integer page,
                                                                 @RequestParam("size") Integer size,
                                                                 @RequestParam("order") String order) {
        return dialogueService.findDialogueByBothDate(counselorId, supervisorId, date, page, size, order);
    }


    @GetMapping("/supervisorConsultInfo")
    @ApiOperation("根据督导ID获取累计咨询时长, 今日咨询时长, 今日咨询数, 当前会话数")
    @ApiImplicitParam(name = "supervisor", value = "咨询师id", required = true, dataType = "Long")
    public Result getConsultInfoSupervisor(@RequestParam("supervisorId") Long supervisorId) {
        return Result.success("获取成功", dialogueService.findConsultInfobySupervisor(supervisorId));
    }

    @PostMapping("/save_roam_msg")
    @ApiOperation(value = "存储单聊信息", notes = "save the dialogue ")
    @ResponseBody
    public Object saveHistory(@Valid @RequestBody DialogueRequest request) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

//        String supervisor_id = userMapper.findIdByName(request.getSupervisor()).toString();
//        String counselor_id = userMapper.findIdByName(request.getCounselor()).toString();

        Dialogue dialogue = dialogueMapper.getDialogueById(request.getDialogue_id());

        String supervisor_id = dialogue.getSupervisor();
        String counselor_id = dialogue.getCounselor();

        String counselor_name = userMapper.findNameById(counselor_id);
        String supervisor_name = userMapper.findNameById(supervisor_id);

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String counelor_imid = userSigMapper.getImidByName(counselor_name);
        String supervisor_imid = userSigMapper.getImidByName(supervisor_name);

        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        ZoneId zone = ZoneId.of("Asia/Shanghai"); // 设置为您所需的时区
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zone);
        long currentTimestampInSeconds = zonedDateTime.toEpochSecond();
        // 设置请求体
        String requestBody = "{\n" +
                "    \"Operator_Account\":\""+counelor_imid+"\",\n" +
                "    \"Peer_Account\":\""+supervisor_imid+"\",\n" +
                "    \"MaxCnt\":100,\n" +
//                "    \"MinTime\":" + Timestamp.valueOf(dialogue.getCreateTime()) +",\n" +
                "    \"MinTime\":" + 0 +",\n" +
                "    \"MaxTime\":"+ currentTimestampInSeconds +"\n" +
                "}\n";
        System.out.println(requestBody);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // 发送请求
        String url = "https://console.tim.qq.com/v4/openim/admin_getroammsg?sdkappid=1400810789&identifier=administrator&"+
                "usersig=eJw1jkEOgjAURO-SrYb8ChZo4gZ1ocGF0URkR2zFr5Y2pUGM8e4i6HLm5WXmRfbpzpOtQSsJ9*nUZwAw7ttGWsLJxAMy5FrcCmNQEE4DgIhCGMUDQSErh2fshUIorLB2tnDa-lUsO1K2pyzBxXqV6fnxmVDVGCkfh7DMWb5M9fa*0XC9CH-E9OwnOlTdL8qiMIi-i*8PSFA1Uw__&"+
                "&random=45612345&contenttype=json";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println(response);


        ObjectMapper objectMapper = new ObjectMapper();
        IMRequest imRequest = objectMapper.readValue(response.getBody(), IMRequest.class);


        Iterator<Message> iterator = imRequest.getMsgList().iterator();
        while (iterator.hasNext()) {
            Message rspMsg = iterator.next();
            List<MsgBody> msgBody = rspMsg.getMsgBody();
            for (MsgBody body : msgBody) {
                if (body.getMsgType().equals("TIMTextElem")) {
                    String text = body.getMsgContent().getText();
                    System.out.println(text);
                } else if (body.getMsgType().equals("TIMRelayElem")) {
                    iterator.remove(); // Use iterator to remove the element
                }
            }
            String fromAccount = rspMsg.getFromAccount();
            String name = userSigMapper.getNameByImid(fromAccount);
            rspMsg.setFromAccount(name);
        }

        Dialogue dialogue1 = new Dialogue();
        dialogue1.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
        dialogue1.setMessage(objectMapper.writeValueAsString(imRequest));
        dialogue1.setId(dialogue.getId());
        dialogue1.setStatus("FINISHED");
        dialogueMapper.updateDialogueFinish(dialogue1);
        return imRequest;
    }
}
