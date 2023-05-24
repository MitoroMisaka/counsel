package com.ecnu.rai.counsel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "咨询师信息")
public class Counselor {
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

    @ApiModelProperty(value = "角色", example = "男")
    @NotNull(message = "角色不能为空")
    private String role;

    @ApiModelProperty(value = "性别", example = "男")
    @NotNull(message = "性别不能为空")
    private String gender;

    @ApiModelProperty(value = "年龄", example = "30")
    @NotNull(message = "年龄不能为空")
    private Integer age;

    @ApiModelProperty(value = "身份证号码", example = "110101199001011234")
    @NotNull(message = "身份证号码不能为空")
    private String idNumber;

    @ApiModelProperty(value = "手机号码", example = "13812345678")
    @NotNull(message = "手机号码不能为空")
    private String phone;

    @ApiModelProperty(value = "电子邮箱", example = "john@example.com")
    @NotNull(message = "电子邮箱不能为空")
    private String email;

    //title
    @ApiModelProperty(value = "职称", example = "CHIEF_COUNSELOR")
    @NotNull(message = "职称不能为空")
    private String title;

    //department
    @ApiModelProperty(value = "部门", example = "COUNSELING_CENTER")
    @NotNull(message = "部门不能为空")
    private String department;

    @ApiModelProperty(value = "所属主管列表")
    private List<Supervisor> supervisors;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
    
    @ApiModelProperty(value = "是否启用", example = "true")
    private Boolean enabled;
    
    @ApiModelProperty(value = "是否删除", example = "false")
    private Boolean deleted;

    //status 
    @ApiModelProperty(value = "状态", example = "ONLINE")
    private String status;

    //star 
    @ApiModelProperty(value = "星级", example = "5")
    private Integer rating;
}

