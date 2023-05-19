package com.ecnu.rai.counsel.controller.search;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("搜索Controller")
@Validated
@RequestMapping("/search")
public class SearchController {
    @Autowired
    SearchService searchService;

    @GetMapping("/user/name")
    @ApiOperation("根据标题搜索(实际上发布会议，主要内容这些都在搜)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "姓名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "order", value = "排序", required = true, dataType = "String")
    })
    public Page<User> searchByTitle(@RequestParam("content") String content, @RequestParam("size") Integer size,
                                    @RequestParam("page") Integer page, @RequestParam("order") String order) {
        return searchService.searchUserByName(content, size, page, order);
    }
}

