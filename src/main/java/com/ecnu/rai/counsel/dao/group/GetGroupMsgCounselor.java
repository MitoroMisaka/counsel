package com.ecnu.rai.counsel.dao.group;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class GetGroupMsgCounselor {
    //    @ApiModelProperty(value = "腾讯IM用户id")
//    private String userid;
//    @ApiModelProperty(value = "腾讯IM咨询师id")
//    private String counselorid;
    @ApiModelProperty(value = "用户真实姓名")
    private String username;
    @ApiModelProperty(value = "咨询师真实姓名")
    private String counselorname;
    //    @ApiModelProperty(value = "开始时间")
//    private Timestamp startTime;
//    @ApiModelProperty(value = "结束时间")
//    private Timestamp endTime;
    @ApiModelProperty(value = "评价")
    private String comment;
    //    private String actionStatus;
//    private String errorInfo;
//    private int errorCode;
//    private String groupId;
//    private int isFinished;
//    private List<RspMsg> rspMsgList;
    @ApiModelProperty(value = "群组id")
    private String groupid;

    // getters and setters
}

