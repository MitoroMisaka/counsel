package com.ecnu.rai.counsel.dao.single;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    @JsonProperty("From_Account")
    private String fromAccount;
    @JsonProperty("To_Account")
    private String toAccount;
    @JsonProperty("MsgSeq")
    private long msgSeq;
    @JsonProperty("MsgRandom")
    private long msgRandom;
    @JsonProperty("MsgTimeStamp")
    private Timestamp msgTimeStamp;
    @JsonProperty("MsgFlagBits")
    private int msgFlagBits;
    @JsonProperty("IsPeerRead")
    private int isPeerRead;
    @JsonProperty("MsgKey")
    private String msgKey;
    @JsonProperty("MsgBody")
    private List<MsgBody> msgBody;
    @JsonProperty("CloudCustomData")
    private String cloudCustomData;
}