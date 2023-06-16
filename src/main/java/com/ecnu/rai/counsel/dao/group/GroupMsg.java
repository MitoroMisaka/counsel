package com.ecnu.rai.counsel.dao.group;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupMsg {
    private String ActionStatus;
    private String ErrorInfo;
    private int ErrorCode;
    private String GroupId;
    private int IsFinished;
    private List<RspMsg> RspMsgList;

    // getters and setters
}
