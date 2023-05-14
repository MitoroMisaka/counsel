package com.ecnu.rai.counsel.controller.search;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Search Request")
@Builder
public class SearchRequest {
    private String content;
    // 3 args in PageParams
    private Integer pageSize;
    private Integer pageNum;
    private String orderBy;
}

