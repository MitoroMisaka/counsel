package com.ecnu.rai.counsel.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ecnu.rai.counsel.entity.Supervisor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "咨询师信息")
public class CounselorMonthlyWork {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "用户ID")
    private Long counselorId;

    @ApiModelProperty(value = "姓名", example = "John Smith")
    @NotNull(message = "姓名不能为空")
    @Size(min = 1, max = 50, message = "姓名长度必须在2-16个字符之间")
    private String name;

    @ApiModelProperty(value = "用户名", example = "john")
    @NotNull(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3到20个字符之间")
    private String username;

    // finishedconsults
    @ApiModelProperty(value = "当前月已完成咨询数量", example = "10")
    private Integer finishedConsults;
}


