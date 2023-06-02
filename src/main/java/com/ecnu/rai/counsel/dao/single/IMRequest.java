package com.ecnu.rai.counsel.dao.single;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class IMRequest {

    private String operatorAccount;
    private String operatorAccountReal;
    private String peerAccount;
    private String peerAccountReal;
    private Timestamp minTime;
    private Timestamp maxTime;
    private String actionStatus;
    private String errorInfo;
    private int errorCode;
    private int complete;
    private int msgCnt;
    private long lastMsgTime;
    private String lastMsgKey;
    private List<Message> msgList;
}