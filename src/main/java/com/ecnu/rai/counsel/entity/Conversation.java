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

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建者")
    private String creator;

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

    @ApiModelProperty(value = "开始时间")
    private Timestamp startTime;

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

}
