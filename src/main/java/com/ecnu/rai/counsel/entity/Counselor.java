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
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "用户信息")
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

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "身份证号")
    private String id_number;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "头衔")
    private String title;

    @ApiModelProperty(value = "部门")
    private String department;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "创建时期")
    private Timestamp create_time;

    @ApiModelProperty(value = "修改时期")
    private Timestamp update_time;

    @ApiModelProperty(value = "是否被启用")
    private Boolean enable;

    @ApiModelProperty(value = "是否被删除")
    private Boolean delete;

    @ApiModelProperty(value = "评分")
    private Double star;

    @ApiModelProperty(value = "状态")
    private String stauts;

    @ApiModelProperty(value = "最大咨询人数")
    private Integer maxConsults;

    // 省略其他方法...
}

