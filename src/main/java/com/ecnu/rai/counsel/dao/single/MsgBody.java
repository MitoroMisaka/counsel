package com.ecnu.rai.counsel.dao.single;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MsgBody {
    @JsonProperty("MsgType")
    private String msgType;
    @JsonProperty("MsgContent")
    private MsgContent msgContent;
}