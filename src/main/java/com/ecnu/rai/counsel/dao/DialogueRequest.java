package com.ecnu.rai.counsel.dao;

import lombok.Data;

@Data
public class DialogueRequest {
    private Long dialogue_id;
    private String counselor;
    private String supervisor;
}
