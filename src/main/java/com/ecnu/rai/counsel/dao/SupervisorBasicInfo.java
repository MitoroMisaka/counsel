package com.ecnu.rai.counsel.dao;

import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervisor;
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
@ApiModel("督导基本信息返回（仅包括id，name，username和role）")
public class SupervisorBasicInfo implements Serializable {
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

    public SupervisorBasicInfo(Supervisor supervisor){
        this.id = supervisor.getId();
        this.name = supervisor.getName();
        this.username = supervisor.getUsername();
        this.role = supervisor.getRole();
        this.avatar = supervisor.getAvatar();
    }
}
