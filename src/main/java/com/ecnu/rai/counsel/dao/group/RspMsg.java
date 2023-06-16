package com.ecnu.rai.counsel.dao.group;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class RspMsg {
//    private String fromAccount;
//    private int isPlaceMsg;
//    private List<GroupMsgBody> msgBody;
//    private int msgPriority;
//    private int msgRandom;
//    private int msgSeq;
//    private Timestamp msgTimeStamp;

    private String FromAccount;
    private int IsPlaceMsg;
    private List<GroupMsgBody> MsgBody;
    private int MsgPriority;
    private int MsgRandom;
    private int MsgSeq;
    private Timestamp MsgTimeStamp;

    // getters and setters
}
