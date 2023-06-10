package com.ecnu.rai.counsel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usersig {
    private String userid;
    private String usersig;
    private String name;
    private String role;
}
