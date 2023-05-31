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
@ApiModel("排班信息汇总")
public class DayNum {

    @ApiModelProperty("日期")
    private Integer day;

    @ApiModelProperty("日期")

    private Integer num;

}
