package com.ecnu.rai.counsel.dao.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupMsgBody {

    @JsonProperty("MsgContent")
    private GroupMsgContent msgContent;
    @JsonProperty("MsgType")
    private String msgType;
    // getters and setters
}
