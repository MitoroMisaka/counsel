package com.ecnu.rai.counsel.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("咨询师咨询时长等数据")
public class ConsultInfo {

    @ApiModelProperty("累计咨询时长")
    private Integer total;

    @ApiModelProperty("今日咨询数")
    private Integer todayNum;

    @ApiModelProperty("今日咨询时长")
    private Integer todayTotal;

    @ApiModelProperty("当前会话数")
    private Integer currentNum;

}
