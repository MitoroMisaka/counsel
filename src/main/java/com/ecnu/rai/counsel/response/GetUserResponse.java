package com.ecnu.rai.counsel.response;

import com.ecnu.rai.counsel.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("GetUserResponse 用户信息返回")
public class GetUserResponse implements Serializable {
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("类别")
    private String role;

    public GetUserResponse(User user, String role){
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        if(role == "visitor")
            this.role = "visitor";
        else if(role == "admin")
            this.role = "admin";
    }
}
