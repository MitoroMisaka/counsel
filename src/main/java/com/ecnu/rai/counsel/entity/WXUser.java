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
public class WXUser {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "openid")
    private String openid;

    @ApiModelProperty(value = "session_key")
    private String session_key;
}