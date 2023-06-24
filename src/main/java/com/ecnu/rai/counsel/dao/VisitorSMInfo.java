package com.ecnu.rai.counsel.dao;

import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervisor;
import com.ecnu.rai.counsel.entity.Visitor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bytebuddy.implementation.bytecode.constant.JavaConstantValue;

import java.util.ArrayList;
import java.util.List;
@Data
@ApiModel(description = "访客信息管理")
public class VisitorSMInfo {
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("身份")
    private String role;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("性别")
    private String gender;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("工作单位")
    private String department;

    @ApiModelProperty("职称")
    private String title;

    @ApiModelProperty("紧急联系人")
    private String emergent_contact;

    @ApiModelProperty("紧急联系人电话")
    private String emergent_phone;

    @ApiModelProperty("禁用状态")
    private Integer state;

    public VisitorSMInfo(Visitor visitor){
        this.id = visitor.getId();
        this.name = visitor.getName();
        this.username = visitor.getUsername();
        this.role = visitor.getRole();
        this.avatar = visitor.getAvatar();
        this.gender = visitor.getGender();
        this.phone = visitor.getPhone();
        this.department = visitor.getDepartment();
        this.title = visitor.getTitle();
        this.emergent_contact =visitor.getEmergentContact();
        this.emergent_phone = visitor.getEmergentPhone();

    }

    public void setState(Integer state)
    {
        this.state = state;
    }



}
