package com.ecnu.rai.counsel.controller;

import com.alibaba.fastjson.JSON;
import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.dao.ConversationIdList;
import com.ecnu.rai.counsel.dao.ConversationResponse;
import com.ecnu.rai.counsel.dao.group.GetGroupMsgCounselor;
import com.ecnu.rai.counsel.dao.group.GetGroupMsgResponse;
import com.ecnu.rai.counsel.dao.group.GroupMsg;
import com.ecnu.rai.counsel.dao.group.RspMsg;
import com.ecnu.rai.counsel.dao.single.IMRequest;
import com.ecnu.rai.counsel.dao.single.Message;
import com.ecnu.rai.counsel.entity.Conversation;
import com.ecnu.rai.counsel.mapper.ConversationMapper;
import com.ecnu.rai.counsel.mapper.CounselorMapper;
import com.ecnu.rai.counsel.mapper.UserMapper;
import com.ecnu.rai.counsel.mapper.UserSigMapper;
import com.ecnu.rai.counsel.service.ConversationService;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/conversation")
public class ConversationController {

    @Autowired
    private IMController imController;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private ConversationMapper conversationMapper;

    @Autowired
    private CounselorMapper counselorMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserSigMapper userSigMapper;

    @GetMapping("/id")
    @ApiOperation("根据ID获取会话信息")
    public Result getConversationbyID(@RequestParam("id") Long id) {
        Conversation conversation = conversationService.findConversationByID(id);
        return Result.success("获取成功", conversation);
    }

    @PostMapping("/start")
    @ApiOperation("开始会话(就是建个表,给出咨询师和用户的姓名就行)")
    public Result insertConversation(@Valid @RequestBody Conversation conversation) throws IOException {
        Integer maxConsult = conversationMapper.getMaxConsult(conversation.getCounselor());
        if(maxConsult.equals(conversationMapper.getConsultNum(conversation.getCounselor()))){
            Result.fail("咨询师咨询次数已达上限");
        }
        conversation.setStatus("STARTED");
        Conversation new_conversation = conversationService.insertConversationByID(conversation);
//        String counselor_name = userMapper.findNameById(conversation.getCounselor());
//        String counselor_imid = userSigMapper.getImidByName(counselor_name);
//        String user_name = userMapper.findNameById(conversation.getUser());
//
//        Object result = imController.createGroup(counselor_name , user_name, counselor_imid);
        return Result.success("获取成功", new_conversation);
    }

    @PostMapping("/update")
    @ApiOperation("(弃用接口)更新会话信息")
    public Result updateConversation(@RequestParam("conversation") Conversation conversation) {
        Conversation new_arrange = conversationService.updateConversation(conversation);
        return Result.success("获取成功", new_arrange);
    }

    @GetMapping("/user")
    @ApiOperation("根据用户ID获取会话信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "page", value = "每页数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "order", value = "排序", required = true, dataType = "String")
    })
    public Result getConversationbyUser(@RequestParam(value = "user") Long user,
                                                            @RequestParam(value = "page") Integer page,
                                                            @RequestParam(value = "page") Integer size,
                                                            @RequestParam(value = "order") String order) {
        return Result.success("获取会话信息成功", conversationService.findConversationByUser(user, page, size, order));
    }

    @GetMapping("/counselor")
    @ApiOperation("根据咨询师ID获取会话信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "counselor", value = "咨询师id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "order", value = "排序", required = true, dataType = "String")
    })
    public Result getConversationbyCounselor(@RequestParam("counselor") Long counselor,
                                                                 @RequestParam("page") Integer page,
                                                                 @RequestParam("size") Integer size,
                                                                 @RequestParam("order") String order) {
        return Result.success("获取会话信息成功", conversationService.findConversationByCounselor(counselor, page, size, order));
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
    public Result getConversationbyCounselorUserDate(@RequestParam("counselor") Long counselor,
                                                                 @RequestParam("user") Long user,
                                                                 @RequestParam("date") String date,
                                                                 @RequestParam("page") Integer page,
                                                                 @RequestParam("size") Integer size,
                                                                 @RequestParam("order") String order) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date util_date = simpleDateFormat.parse(date);
        java.sql.Date sql_date = new java.sql.Date(util_date.getTime());

        return Result.success("获取会话信息成功", conversationService.findConversationByCounselorUserDate(counselor, user, sql_date, page, size, order));
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
    public Result saveGroupMsg(@Valid @RequestBody GetGroupMsgResponse getGroupMsgResponse) throws Exception {
        System.out.println(getGroupMsgResponse);

        GroupMsg groupMsg = imController.getGroupHistory(getGroupMsgResponse.getGroupid());
        String history = JSON.toJSONString(groupMsg);
        Long counselor_id = userMapper.findIdByName(getGroupMsgResponse.getCounselorname());
        Long user_id = userMapper.findIdByName(getGroupMsgResponse.getUsername());

        String counselor_id_str = String.valueOf(counselor_id);
        String user_id_str = String.valueOf(user_id);

        Conversation conversation = Conversation.builder()
                .id(getGroupMsgResponse.getId())
                .createTime(LocalDateTime.now())
                .creator(counselor_id_str)
                .lastUpdateTime(LocalDateTime.now())
                .lastUpdater(counselor_id_str)
//                .year(getGroupMsgResponse.getStartTime().toLocalDateTime().getYear())
//                .month(getGroupMsgResponse.getStartTime().toLocalDateTime().getMonth())
//                .day(getGroupMsgResponse.getStartTime().toLocalDateTime().getDayOfMonth())
//                .startTime(getGroupMsgResponse.getStartTime())
//                .endTime(getGroupMsgResponse.getEndTime())
                .user(user_id_str)
                .counselor(counselor_id_str)
//                .status("FINISHED")
                .visitorName(getGroupMsgResponse.getUsername())
                .evaluate(getGroupMsgResponse.getRating())
                .conversationType("评价:"+getGroupMsgResponse.getComment())
                .message(history)
                .build();
        conversationMapper.updateConversationUser(conversation);
        String counselorid = String.valueOf(userMapper.findIdByName(getGroupMsgResponse.getCounselorname()));
        List<Conversation> conversations1 = conversationMapper.findGroupMsgByCounselor(counselorid);
        Double sum = 0.0;
        for(Conversation conversation1 : conversations1){
            sum += conversation1.getEvaluate();
        }
        //average 四舍五入
        Integer average = (int) Math.round(sum/conversations1.size());
        String name = getGroupMsgResponse.getCounselorname();
        counselorMapper.updateCounselorRating(name , average);

        return Result.success("save and insert conversation ");
    }

    @PostMapping("/save_group_msg/counselor")
    @ApiOperation(value = "存储群聊信息/结束会话", notes = "save the conversation ")
    @ResponseBody
    public Result saveGroupMsgByCounselor(@Valid @RequestBody GetGroupMsgCounselor getGroupMsgResponse) throws Exception {

        System.out.println(getGroupMsgResponse);

        Long counselor_id = userMapper.findIdByName(getGroupMsgResponse.getCounselorname());
        Long user_id = userMapper.findIdByName(getGroupMsgResponse.getUsername());

        String counselor_id_str = String.valueOf(counselor_id);
        String user_id_str = String.valueOf(user_id);

        Long conversation_id = conversationMapper.findConversationByCounselorAndUser(counselor_id_str , user_id_str);

        Conversation conversation = Conversation.builder()
                .id(conversation_id)
//                .createTime(LocalDateTime.now())
//                .creator(counselor_id_str)
                .lastUpdateTime(LocalDateTime.now())
                .lastUpdater(counselor_id_str)
//                .year(getGroupMsgResponse.getStartTime().toLocalDateTime().getYear())
//                .month(getGroupMsgResponse.getStartTime().toLocalDateTime().getMonth())
//                .day(getGroupMsgResponse.getStartTime().toLocalDateTime().getDayOfMonth())
//                .startTime(getGroupMsgResponse.getStartTime())
//                .endTime(getGroupMsgResponse.getEndTime())
//                .user(user_id_str)
//                .counselor(counselor_id_str)
                .status("FINISHED")
//                .visitorName(getGroupMsgResponse.getUsername())
//                .evaluate(getGroupMsgResponse.getRating())
                .comment("评价:"+getGroupMsgResponse.getComment())
//                .message(history)
                .build();
        conversationMapper.updateConversationCounselor(conversation);

        return Result.success("save and insert conversation by counselor success");
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


    @GetMapping("/export/history")
    @ApiOperation(value = "导出历史记录(JSON)", notes = "export the history ")
    @ApiImplicitParam(name = "conversation_id", value = "会话id", required = true, dataType = "Long")
    public ResponseEntity<ByteArrayResource> getJsonFile(@RequestParam("conversation_id") Long conversation_id) throws Exception {
        Conversation conversation = conversationMapper.getConversationById(conversation_id);

        String text = conversation.getMessage();

        Gson gson = new Gson();

        List<RspMsg> message = gson.fromJson(text, List.class);
        String response = "{ \"message\" :" + gson.toJson(message) + "}";
        String counselor_name  = userMapper.findNameById(conversation.getCounselor());
        //convert counselor_name to pinyin
        counselor_name = PinyinUtil.convertToPinyin(counselor_name);
        String visitor_name = PinyinUtil.convertToPinyin(conversation.getVisitorName());
        String filename = counselor_name + "_" + visitor_name + "_" + conversation.getStartTime() + ".json";

        ByteArrayResource resource = new ByteArrayResource(response.getBytes("GBK"));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                .contentType(MediaType.APPLICATION_JSON)
                .body(resource);
    }

    @GetMapping("/export/history/batch")
    public ResponseEntity<ByteArrayResource> getBatchJsonFiles(@RequestBody ConversationIdList conversationIdList) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

        for (Long conversationId : conversationIdList.getConversationIds()) {
            Conversation conversation = conversationMapper.getConversationById(conversationId);
            String text = conversation.getMessage();
            Gson gson = new Gson();
            List<RspMsg> message = gson.fromJson(text, List.class);
            String response = "{ \"message\" :" + gson.toJson(message) + "}";
            String counselorName = userMapper.findNameById(conversation.getCounselor());
            counselorName = PinyinUtil.convertToPinyin(counselorName);
            String visitorName = PinyinUtil.convertToPinyin(conversation.getVisitorName());
            String filename = counselorName + "_" + visitorName + "_" + conversation.getStartTime() + ".json";

            // 添加文件到压缩包
            zipOutputStream.putNextEntry(new ZipEntry(filename));
            zipOutputStream.write(response.getBytes("GBK"));
            zipOutputStream.closeEntry();
        }

        zipOutputStream.close();

        byte[] zipData = outputStream.toByteArray();
        ByteArrayResource resource = new ByteArrayResource(zipData);

        // 设置响应头，指定文件名为导出的压缩包
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=export_history.zip");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(zipData.length)
                .body(resource);
    }
}
