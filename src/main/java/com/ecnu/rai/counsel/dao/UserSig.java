package com.ecnu.rai.counsel.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserSig {
    public String userid;
    public String token;
    public String name;
    public String role;
}
