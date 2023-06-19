package com.ecnu.rai.counsel.dao.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupMsgContent {
    @JsonProperty("Data")
    private String data;
    @JsonProperty("Desc")
    private String desc;
    @JsonProperty("Ext")
    private String ext;
    @JsonProperty("Index")
    private int index;
    @JsonProperty("Text")
    private String text;

    // getters and setters
}
