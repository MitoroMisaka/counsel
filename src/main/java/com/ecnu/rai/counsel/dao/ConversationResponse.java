package com.ecnu.rai.counsel.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ecnu.rai.counsel.dao.group.GroupMsg;
import com.ecnu.rai.counsel.dao.group.RspMsg;
import com.ecnu.rai.counsel.entity.Conversation;
import com.google.gson.Gson;
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
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "会话信息")
public class ConversationResponse {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "会话ID")
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @ApiModelProperty(value = "创建者")
    private String creator;

    @ApiModelProperty(value = "最后更新时间")
    private Timestamp lastUpdateTime;

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
    private Object message;

    @ApiModelProperty(value = "会话评价")
    private String comment;

    //从Conversation来的构造函数, 用于将Conversation转换为ConversationResponse,
    //其他属性直接使用,Conversation中的message(String)转换为ConversationResponse里面的message(List<RspMsg>)
    public ConversationResponse(Conversation conversation){
        Gson gson = new Gson();

        this.id = conversation.getId();
        this.createTime = Timestamp.valueOf(conversation.getCreateTime());
        this.creator = conversation.getCreator();
        this.lastUpdateTime = Timestamp.valueOf(conversation.getLastUpdateTime());
        this.lastUpdater = conversation.getLastUpdater();
        this.year = conversation.getYear();
        this.month = conversation.getMonth();
        this.day = conversation.getDay();
        this.startTime = conversation.getStartTime();
        this.endTime = conversation.getEndTime();
        this.user = conversation.getUser();
        this.counselor = conversation.getCounselor();
        this.status = conversation.getStatus();
        this.visitorName = conversation.getVisitorName();
        this.evaluate = conversation.getEvaluate();
        this.conversationType = conversation.getConversationType();
        this.message = gson.fromJson(conversation.getMessage(),Object.class);
        this.comment = conversation.getComment();
    }
}
