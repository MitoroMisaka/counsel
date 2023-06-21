package com.ecnu.rai.counsel.dao;

import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.mapper.UserSigMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("用户基本信息返回（仅包括id，username和role）")
public class UserLoginInfo implements Serializable {
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("类别")
    private String role;

    @ApiModelProperty("imid")
    private String imid;

    public UserLoginInfo(User user, String imid){


        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();

        this.imid = imid;
    }
}
