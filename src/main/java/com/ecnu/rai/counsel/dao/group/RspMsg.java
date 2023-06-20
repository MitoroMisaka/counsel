package com.ecnu.rai.counsel.dao.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RspMsg {

    @JsonProperty("From_Account")
    private String fromAccount;

    @JsonProperty("IsPlaceMsg")
    private int isPlaceMsg;

    @JsonProperty("MsgPriority")
    private int msgPriority;

    @JsonProperty("MsgSeq")
    private int msgSeq;

    @JsonProperty("MsgBody")
    private List<GroupMsgBody> msgBody;

    @JsonProperty("MsgRandom")
    private int msgRandom;

    @JsonProperty("MsgTimeStamp")
    private Timestamp msgTimeStamp;


    // getters and setters
}
