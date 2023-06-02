package com.ecnu.rai.counsel.dao.group;

import lombok.Data;

@Data
public class GroupMsgBody {
    private GroupMsgContent msgContent;
    private String msgType;

    // getters and setters
}
