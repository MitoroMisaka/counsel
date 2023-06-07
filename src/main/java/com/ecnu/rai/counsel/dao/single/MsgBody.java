package com.ecnu.rai.counsel.dao.single;

import lombok.Data;

@Data
public class MsgBody {
    private String msgType;
    private MsgContent msgContent;
}
