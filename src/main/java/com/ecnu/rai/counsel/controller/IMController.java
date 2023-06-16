package com.ecnu.rai.counsel.controller;

import com.alibaba.fastjson.JSONObject;
import com.ecnu.rai.counsel.config.UserSigConfig;
import com.ecnu.rai.counsel.dao.group.GroupMsg;
import com.ecnu.rai.counsel.service.IMService;
import com.ecnu.rai.counsel.util.PinyinUtil;
import com.google.gson.Gson;
import com.tencentcloudapi.as.v20180419.models.Instance;
import io.github.doocs.im.ImClient;
import io.github.doocs.im.constant.ApplyJoinOption;
import io.github.doocs.im.constant.GroupType;
import io.github.doocs.im.constant.SyncOtherMachine;
import io.github.doocs.im.model.message.TIMMsgElement;
import io.github.doocs.im.model.message.TIMTextMsgElement;
import io.github.doocs.im.model.request.*;
import io.github.doocs.im.model.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/im")
public class IMController {

    @Autowired
    IMService imService;

    @Autowired
    UserSigConfig userSigConfig;

    @GetMapping("/import")
    @ApiOperation("导入单个用户")
    @ApiImplicitParam(name = "imid", value = "IMid", required = true, dataType = "String")
    public Object importAccount(@RequestParam("imid") String imid) throws IOException {
        Long sdkAppId = userSigConfig.getSdkAppId();
        String secretKey = userSigConfig.getSecretKey();
        String userId = "administrator";
        ImClient client = new ImClient(sdkAppId, userId, secretKey);

        AccountImportRequest request = AccountImportRequest.builder()
                .userId(imid)
                .faceUrl("https://avatars.githubusercontent.com/u/43716716?s=200&v=4")
                .nick("doocs")
                .build();

        AccountImportResult result = client.account.accountImport(request);
        return result;
    }

    @GetMapping("/group/create")
    @ApiOperation("创建群组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "群组名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "owner", value = "群主", required = true, dataType = "String")
    })
    public Object createGroup(@RequestParam("name") String counselor_name, String user_name,@RequestParam("owner") String owner) throws IOException {
        Long sdkAppId = userSigConfig.getSdkAppId();
        String secretKey = userSigConfig.getSecretKey();
        String userId = "administrator";
        ImClient client = new ImClient(sdkAppId, userId, secretKey);

        CreateGroupRequest request = CreateGroupRequest.builder()
                .type(GroupType.PUBLIC)
                .name(counselor_name+ "_" + user_name)
                .ownerAccount(owner)
                .groupId(PinyinUtil.convertToPinyin(counselor_name) +  PinyinUtil.convertToPinyin(user_name))
                .introduction("心理咨询组")
                .notification("欢迎开始心理咨询")
                .faceUrl("https://avatars.githubusercontent.com/u/43716716?s=200&v=4")
                .maxMemberCount(500)
                .applyJoinOption(ApplyJoinOption.FREE_ACCESS)
                .build();

        CreateGroupResult result = client.group.createGroup(request);
        return result;
    }

    @GetMapping("/group")
    @ApiOperation("获取群组信息")
    public Object getGroupInfo() throws IOException {
        Long sdkAppId = userSigConfig.getSdkAppId();
        String secretKey = userSigConfig.getSecretKey();
        String userId = "administrator";
        ImClient client = new ImClient(sdkAppId, userId, secretKey);

        Long next = 10L;
        GetAppIdGroupListRequest request = GetAppIdGroupListRequest.builder()
                .limit(10)
                .groupType(GroupType.PUBLIC)
                .next(next)
                .build();

        GetAppIdGroupListResult result = client.group.getAppIdGroupList(request);
        return result;
    }

    @GetMapping("/group/add_member")
    @ApiOperation("添加群组成员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "group_id", value = "群组ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "member", value = "成员IMid", required = true, dataType = "String")
    })
    public Object addGroupMember(@RequestParam("group_id") String group_id, @RequestParam("member") String member) throws IOException {
        Long sdkAppId = userSigConfig.getSdkAppId();
        String secretKey = userSigConfig.getSecretKey();
        String userId = "administrator";
        ImClient client = new ImClient(sdkAppId, userId, secretKey);

        MemberRequestItem item = new MemberRequestItem(member);
        List<MemberRequestItem> memberList = Collections.singletonList(item);
        AddGroupMemberRequest request = AddGroupMemberRequest.builder()
                .groupId(group_id)
                .memberList(memberList)
                .silence(1)
                .build();

        AddGroupMemberResult result = client.group.addGroupMember(request);
        return result;
    }

    @GetMapping("/post/test")
    @ApiOperation("测试")
    public Object test(@RequestParam("group_name") String group_name) throws IOException {
        group_name = "@TGS#2Y2M4MYM6";

        RestTemplate restTemplate = new RestTemplate();

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 设置请求体
        String requestBody = "{\"GroupId\": \"" + group_name +   "\", \"ReqMsgNumber\"" + ": 20}";
        System.out.println(requestBody);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // 发送请求
        String url = "https://console.tim.qq.com/v4/group_open_http_svc/group_msg_get_simple"+
                "?sdkappid=1400810789&identifier=administrator&"+
                "usersig=eJw1jkELgjAYhv-LroV8W3NOoUN5SfAQldTV2LIP0clcYxD990Tr*D4PD7xvcinPkQ4DWk0yAVwCrGfmtSUZYRGQZY*qrYcBFckoB5AUEpkuBpXuHT5wDmrVYY*js7Uz9p9iM5n27p95cfMxv6Y*3x*q4pS-duPqyFipkyYUaFjbBLGpzPYXOuymV1RIIZmEmH**2SU0PQ__&"+
                "random=47712345&contenttype=json";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println(response);
        //convert response.getBody() to JSON Object
        Gson gson = new Gson();
        JSONObject groupMsg = gson.fromJson(response.getBody(), JSONObject.class);
        Object msg = groupMsg.get("RspMsgList");

        // 获取响应结果
        return msg;
    }

    @GetMapping("/send/text/single")
    @ApiOperation("发送单聊文本消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "from", value = "发送者IMid", required = true, dataType = "String"),
            @ApiImplicitParam(name = "to", value = "接收者IMid", required = true, dataType = "String"),
            @ApiImplicitParam(name = "text", value = "文本内容", required = true, dataType = "String")
    })
    public Object sendSingleTextMsg(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam("text") String text) throws IOException {
        Long sdkAppId = userSigConfig.getSdkAppId();
        String secretKey = userSigConfig.getSecretKey();
        String userId = "administrator";
        ImClient client = new ImClient(sdkAppId, userId, secretKey);

        //random Long
        Random random = new Random();
        Long randomLong = random.nextLong();
        //get Timestamp now
        Integer timestamp = (int) (System.currentTimeMillis() / 1000);

        TIMTextMsgElement msg = new TIMTextMsgElement(text);
        List<TIMMsgElement> msgBody = Collections.singletonList(msg);
        SendMsgRequest request = SendMsgRequest.builder()
                .fromAccount(from)
                .toAccount(to)
                .msgRandom(7723216L)
                .msgBody(msgBody)
                .syncOtherMachine(SyncOtherMachine.YES)
                .msgTimeStamp(timestamp)
                .msgLifeTime(604800)
                .build();

        SendMsgResult result = client.message.sendMsg(request);

        return result;
    }






}