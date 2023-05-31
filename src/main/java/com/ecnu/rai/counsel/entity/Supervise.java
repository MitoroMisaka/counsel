package com.ecnu.rai.counsel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "咨询师督导绑定信息")
public class Supervise {

    @ApiModelProperty(value = "counselor_Id")
    private Long counselorId;

    @ApiModelProperty(value = "supervisor_Id")
    private Long supervisorId;

}
