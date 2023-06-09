package com.ecnu.rai.counsel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "会话信息")
public class Conversation {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "会话ID")
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建者")
    private String creator;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "最后更新时间")
    private LocalDateTime lastUpdateTime;

    @ApiModelProperty(value = "最后更新者")
    private String lastUpdater;

    @ApiModelProperty(value = "年")
    private Integer year;

    @ApiModelProperty(value = "月")
    private Month month;

    @ApiModelProperty(value = "日")
    private Integer day;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "开始时间")
    private Timestamp startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "结束时间")
    private Timestamp endTime;

    @ApiModelProperty(value = "咨询用户姓名")
    private String  user;

    @ApiModelProperty(value = "咨询师姓名")
    private String counselor;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "咨询用户姓名")
    private String visitorName;

    @ApiModelProperty(value = "会话评分")
    private Integer evaluate;

    @ApiModelProperty(value = "会话类型")
    private String conversationType;

    @ApiModelProperty(value = "会话内容")
    private String message;

    @ApiModelProperty(value = "咨询师评价")
    private String comment;

}
