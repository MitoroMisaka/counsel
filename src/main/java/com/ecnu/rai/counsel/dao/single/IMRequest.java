package com.ecnu.rai.counsel.dao.single;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class IMRequest {
    @JsonProperty("ActionStatus")
    private String actionStatus;
    @JsonProperty("ErrorInfo")
    private String errorInfo;
    @JsonProperty("ErrorCode")
    private int errorCode;
    @JsonProperty("Complete")
    private int complete;
    @JsonProperty("MsgCnt")
    private int msgCnt;
    @JsonProperty("LastMsgTime")
    private long lastMsgTime;
    @JsonProperty("LastMsgKey")
    private String lastMsgKey;
    @JsonProperty("MsgList")
    private List<Message> msgList;
}