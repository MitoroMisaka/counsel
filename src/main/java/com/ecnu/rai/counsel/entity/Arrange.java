package com.ecnu.rai.counsel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Arrange {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建者")
    private Long creator;

    @ApiModelProperty(value = "最后更新时间")
    private LocalDateTime lastUpdateTime;

    @ApiModelProperty(value = "最后更新者")
    private Long lastUpdater;

    @ApiModelProperty(value = "年")
    private Integer year;

    @ApiModelProperty(value = "月")
    private Integer month;

    @ApiModelProperty(value = "日")
    private Integer day;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "被排班用户")
    private Long user;

    @ApiModelProperty(value = "被排班用户角色")
    private String role;

    @ApiModelProperty(value = "星期")
    private String weekday;

    @ApiModelProperty(value = "当地时间")
    private LocalDateTime localDate;

}
