package com.ecnu.rai.counsel.dao;

import com.ecnu.rai.counsel.entity.Counselor;
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
@ApiModel("咨询师基本信息返回（仅包括id，name，username和role）")
public class CounselorBasicInfo implements Serializable {
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("类别")
    private String role;

    @ApiModelProperty("头像")
    private String avatar;

    public CounselorBasicInfo(Counselor counselor){
        this.id = counselor.getId();
        this.name = counselor.getName();
        this.username = counselor.getUsername();
        this.role = counselor.getRole();
        this.avatar = counselor.getAvatar();
    }
}
