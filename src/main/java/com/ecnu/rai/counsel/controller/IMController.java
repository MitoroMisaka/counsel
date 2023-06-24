package com.ecnu.rai.counsel.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.rai.counsel.config.UserSigConfig;
import com.ecnu.rai.counsel.dao.group.GroupMsg;
import com.ecnu.rai.counsel.dao.group.GroupMsgBody;
import com.ecnu.rai.counsel.dao.group.RspMsg;
import com.ecnu.rai.counsel.dao.single.Message;
import com.ecnu.rai.counsel.dao.single.MsgBody;
import com.ecnu.rai.counsel.mapper.UserSigMapper;
import com.ecnu.rai.counsel.service.IMService;
import com.ecnu.rai.counsel.util.LowerCaseUtil;
import com.ecnu.rai.counsel.util.PinyinUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tencentcloudapi.as.v20180419.models.Instance;
import io.github.doocs.im.ImClient;
import io.github.doocs.im.constant.ApplyJoinOption;
import io.github.doocs.im.constant.GroupType;
import io.github.doocs.im.constant.OnlineOnlyFlag;
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
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/im")
public class IMController {

    @Autowired
    IMService imService;

    @Autowired
    UserSigConfig userSigConfig;

    @Autowired
    UserSigMapper userSigMapper;

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
                .name(counselor_name)
                .ownerAccount(owner)
                .groupId(PinyinUtil.convertToPinyin(counselor_name))
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

    @GetMapping("/history")
    @ApiOperation("测试")
    public GroupMsg getGroupHistory(@RequestParam("group_id") String group_id) throws Exception {
//        group_id = "@TGS#2Y2M4MYM6";

        RestTemplate restTemplate = new RestTemplate();

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 设置请求体
        String requestBody = "{\"GroupId\": \"" + group_id +   "\", \"ReqMsgNumber\"" + ": 20}";
        System.out.println(requestBody);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // 发送请求
        String url = "https://console.tim.qq.com/v4/group_open_http_svc/group_msg_get_simple"+
                "?sdkappid=1400810789&identifier=administrator&"+
                "usersig=eJw1jkEOgjAURO-SrYb8ChZo4gZ1ocGF0URkR2zFr5Y2pUGM8e4i6HLm5WXmRfbpzpOtQSsJ9*nUZwAw7ttGWsLJxAMy5FrcCmNQEE4DgIhCGMUDQSErh2fshUIorLB2tnDa-lUsO1K2pyzBxXqV6fnxmVDVGCkfh7DMWb5M9fa*0XC9CH-E9OwnOlTdL8qiMIi-i*8PSFA1Uw__&"+
                "random=47712345&contenttype=json";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println(response);


        ObjectMapper objectMapper = new ObjectMapper();
        GroupMsg groupMsg = objectMapper.readValue(response.getBody(), GroupMsg.class);

        // 对From_Account进行修改
        Iterator<RspMsg> iterator = groupMsg.getRspMsgList().iterator();
        while (iterator.hasNext()) {
            RspMsg rspMsg = iterator.next();
            List<GroupMsgBody> msgBody = rspMsg.getMsgBody();
            for (GroupMsgBody body : msgBody) {
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
        return groupMsg;
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

    @GetMapping("/group/info")
    @ApiOperation("获取群组详细资料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "group_id", value = "群组ID", required = true, dataType = "String")
    })
    public Object getGroupInfo(@RequestParam("group_id") String group_id) throws IOException {
        Long sdkAppId = userSigConfig.getSdkAppId();
        String secretKey = userSigConfig.getSecretKey();
        String userId = "administrator";
        ImClient client = new ImClient(sdkAppId, userId, secretKey);

        List<String> groupIdList = Collections.singletonList(group_id);
        GetGroupInfoRequest request = new GetGroupInfoRequest(groupIdList);

        GetGroupInfoResult result = client.group.getGroupInfo(request);
        return result;
    }

    @GetMapping("/group/text/single")
    @ApiOperation("发送群聊文本消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "group_id", value = "群组ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "text", value = "文本内容", required = true, dataType = "String")
    })
    public Object sendGroupTextMsg( @RequestParam("group_id") String group_id, @RequestParam("text") String text) throws IOException {
        Long sdkAppId = userSigConfig.getSdkAppId();
        String secretKey = userSigConfig.getSecretKey();
        String userId = "administrator";
        ImClient client = new ImClient(sdkAppId, userId, secretKey);

        TIMTextMsgElement msg = new TIMTextMsgElement(text);
        List<TIMMsgElement> msgBody = Collections.singletonList(msg);
        SendGroupMsgRequest request = SendGroupMsgRequest.builder()
                .groupId(group_id)
                .random(1314L)
                .msgBody(msgBody)
                .onlineOnlyFlag(OnlineOnlyFlag.YES)
                .build();

        SendGroupMsgResult result = client.group.sendGroupMsg(request);

        return result;
    }

    @GetMapping("/group/text")
    @ApiOperation("发送群聊文本消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "group_id", value = "群组ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "imid", value = "imid", required = true, dataType = "String")
    })
    public Object sendGroupTextMsgByAPI( @RequestParam("group_id") String group_id, @RequestParam("imid") String imid) throws IOException {

        RestTemplate restTemplate = new RestTemplate();

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 设置请求体
        String requestBody = "{\n" +
                "  \"GroupId\": \"" + group_id +"\",\n" +
                "  \"From_Account\": \""+ imid +"\", \n" +
                "  \"Random\": 8912345, \n" +
                "  \"MsgBody\": [ \n" +
                "      {\n" +
                "          \"MsgType\": \"TIMTextElem\",\n" +
                "          \"MsgContent\": {\n" +
                "              \"Text\": \"开始会话\"\n" +
                "          }\n" +
                "      }\n" +
                "  ]\n" +
                "}";
        System.out.println(requestBody);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // 发送请求
        String url = "https://console.tim.qq.com/v4/group_open_http_svc/send_group_msg?sdkappid=1400810789&"+
                "identifier=administrator&"+
                "usersig=eJw1jkEOgjAURO-SrYb8ChZo4gZ1ocGF0URkR2zFr5Y2pUGM8e4i6HLm5WXmRfbpzpOtQSsJ9*nUZwAw7ttGWsLJxAMy5FrcCmNQEE4DgIhCGMUDQSErh2fshUIorLB2tnDa-lUsO1K2pyzBxXqV6fnxmVDVGCkfh7DMWb5M9fa*0XC9CH-E9OwnOlTdL8qiMIi-i*8PSFA1Uw__"+
                "random=234235566&contenttype=json";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response;
    }

    @GetMapping("/group/send/notify")
    @ApiOperation("发送群聊通知消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "group_id", value = "群组ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "text", value = "文本内容", required = true, dataType = "String")
    })
    public Object sendGroupNotifyMsg( @RequestParam("group_id") String group_id, @RequestParam("text") String text) throws IOException {
        Long sdkAppId = userSigConfig.getSdkAppId();
        String secretKey = userSigConfig.getSecretKey();
        String userId = "administrator";
        ImClient client = new ImClient(sdkAppId, userId, secretKey);



        return null;
    }

    @GetMapping("/group/text/system")
    @ApiOperation("发送群聊系统消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "group_id", value = "群组ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "text", value = "文本内容", required = true, dataType = "String")
    })
    public Object sendGroupSystemMsg( @RequestParam("group_id") String group_id, @RequestParam("text") String text) throws IOException {
        Long sdkAppId = userSigConfig.getSdkAppId();
        String secretKey = userSigConfig.getSecretKey();
        String userId = "administrator";
        ImClient client = new ImClient(sdkAppId, userId, secretKey);

        List<String> toMembersAccount = Collections.singletonList("wek");
        SendGroupSystemNotificationRequest request = SendGroupSystemNotificationRequest.builder()
                .groupId(group_id)
                .content(text)
                .toMembersAccount(toMembersAccount)
                .build();

        SendGroupSystemNotificationResult result = client.group.sendGroupSystemNotification(request);

        return result;
    }

    @GetMapping("/group/delete")
    @ApiOperation("解散群组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "group_id", value = "群组ID", required = true, dataType = "String")
    })
    public Object deleteGroup( @RequestParam("group_id") String group_id) throws IOException {
        Long sdkAppId = userSigConfig.getSdkAppId();
        String secretKey = userSigConfig.getSecretKey();
        String userId = "administrator";
        ImClient client = new ImClient(sdkAppId, userId, secretKey);

        DestroyGroupRequest request = new DestroyGroupRequest(group_id);

        DestroyGroupResult result = client.group.destroyGroup(request);

        return result;
    }

    //查询用户加入的群组
    @GetMapping("/group/joined")
    @ApiOperation("查询用户加入的群组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imid", value = "imid", required = true, dataType = "String")
    })
    public Object getJoinedGroup( @RequestParam("imid") String imid) throws IOException {
        Long sdkAppId = userSigConfig.getSdkAppId();
        String secretKey = userSigConfig.getSecretKey();
        String userId = "administrator";
        ImClient client = new ImClient(sdkAppId, userId, secretKey);

        GetJoinedGroupListRequest request = new GetJoinedGroupListRequest(imid);

        GetJoinGroupListResult result = client.group.getJoinGroupList(request);

        return result;
    }

}
