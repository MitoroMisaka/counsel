package com.ecnu.rai.counsel.dao.group;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class RspMsg {
    private String fromAccount;
    private int isPlaceMsg;
    private List<GroupMsgBody> msgBody;
    private int msgPriority;
    private int msgRandom;
    private int msgSeq;
    private Timestamp msgTimeStamp;

    // getters and setters
}
