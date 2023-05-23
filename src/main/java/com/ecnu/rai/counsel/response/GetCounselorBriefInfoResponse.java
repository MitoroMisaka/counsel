package com.ecnu.rai.counsel.response;

import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.User;
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
@ApiModel("GetCounselorInfoResponse 咨询师基本信息返回")
public class GetCounselorBriefInfoResponse {
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("评分")
    private String star;

    public GetUserResponse(Counselor counselor) {
        this.id = counselor.getId();
        this.username = counselor.getUsername();
        this.status = counselor.getStatus();
        this.star = counselor.getStar();
    }

}
