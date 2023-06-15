package com.ecnu.rai.counsel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usersig {
    @Getter
    private String imid;
    @Getter
    private String usersig;
    @Getter
    private String name;
    @Getter
    private String role;
}
