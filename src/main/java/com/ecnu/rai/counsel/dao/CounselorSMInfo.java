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
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(description = "咨询师信息管理")
public class CounselorSMInfo implements Serializable {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("身份")
    private String role;

    @ApiModelProperty("绑定督导")
    private List<String> supervisors;

    @ApiModelProperty("平均评价")
    private Integer rating;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("总咨询时间：秒")
    private Integer totalTime;

    @ApiModelProperty("总咨询次数")
    private Integer totalNum;

    @ApiModelProperty("排版星期")
    private List<Integer> totalDay = new ArrayList<>();

    @ApiModelProperty("禁用状态")
    private Integer state = 1;

    public CounselorSMInfo(Counselor counselor){
        this.id = counselor.getId();
        this.name = counselor.getName();
        this.name = counselor.getName();
        this.role = counselor.getRole();
        this.avatar = counselor.getAvatar();
        this.rating = counselor.getRating();
    }
    public void SetTotalTime(Integer totalTime)
    {
        this.totalTime = totalTime;
    }
    public void SetTotalNum(Integer totalNum)
    {
        this.totalNum = totalNum;
    }
    public void setTotalDay(Integer day)
    {
        if(!this.totalDay.contains(day))
        this.totalDay.add(day);
    }
    public void setState(Integer state)
    {
        this.state = state;
    }
    public void setSupervisors(List<String> supervisors)
    {
        this.supervisors = supervisors;
    }
}
