package com.ecnu.rai.counsel.dao.single;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class Message {
    private String fromAccount;
    private String toAccount;
    private long msgSeq;
    private long msgRandom;
    private Timestamp msgTimeStamp;
    private int msgFlagBits;
    private int isPeerRead;
    private String msgKey;
    private List<MsgBody> msgBody;
    private String cloudCustomData;
}
