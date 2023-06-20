package com.ecnu.rai.counsel.dao.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupMsg {
    @JsonProperty("ActionStatus")
    private String actionStatus;
    @JsonProperty("ErrorInfo")
    private String errorInfo;
    @JsonProperty("ErrorCode")
    private int errorCode;
    @JsonProperty("GroupId")
    private String groupId;
    @JsonProperty("IsFinished")
    private int isFinished;
    @JsonProperty("RspMsgList")
    private List<RspMsg> rspMsgList;

    // getters and setters
}
