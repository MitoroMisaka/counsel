package com.ecnu.rai.counsel.request;

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
@ApiModel("UpdateUserMessageRequest 修改用户信息")
public class UpdateUserMessageRequest {
    @ApiModelProperty("状态")
    private Short status;
}
