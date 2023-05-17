package com.ecnu.rai.counsel.entity;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "用户信息")
public class User {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "姓名", example = "John Smith")
    @NotNull(message = "姓名不能为空")
    @Size(min = 1, max = 50, message = "姓名长度必须在1到50个字符之间")
    private String name;

    @ApiModelProperty(value = "用户名", example = "john")
    @NotNull(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3到20个字符之间")
    private String username;

    @ApiModelProperty(value = "密码", example = "123456")
    @NotNull(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6到20个字符之间")
    private String password;

    @ApiModelProperty(value = "角色", example = "admin")
    private String role;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "电话号码")
    private String phone;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "部门")
    private String department;

    @ApiModelProperty(value = "紧急联系人")
    private String emergentContact;

    @ApiModelProperty(value = "紧急联系电话")
    private String emergentPhone;

    // 省略其他方法...
}

