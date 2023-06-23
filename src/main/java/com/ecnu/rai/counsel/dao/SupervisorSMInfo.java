package com.ecnu.rai.counsel.dao;

import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervisor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
@ApiModel(description = "督导信息管理")
public class SupervisorSMInfo {
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("身份")
    private String role;


    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("总咨询时间：秒")
    private Integer totalTime;

    @ApiModelProperty("总咨询次数")
    private Integer totalNum;

    @ApiModelProperty("绑定咨询师name")
    private List<String> counselors;

    @ApiModelProperty("排版星期")
    private List<Integer> totalDay = new ArrayList<>();

    @ApiModelProperty("禁用状态")
    private Integer state;


    public SupervisorSMInfo(Supervisor supervisor){
        this.id = supervisor.getId();
        this.name = supervisor.getName();
        this.name = supervisor.getName();
        this.role = supervisor.getRole();
        this.avatar = supervisor.getAvatar();
    }
    public void SetTotalTime(Integer totalTime)
    {
        this.totalTime = totalTime;
    }
    public void SetTotalNum(Integer totalNum)
    {
        this.totalNum = totalNum;
    }
    public void SetCounselors(List<String> counselors)
    {
        this.counselors = counselors;
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
}
