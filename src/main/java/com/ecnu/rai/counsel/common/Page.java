package com.ecnu.rai.counsel.common;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Page<T> {
    // 当前页
    private Integer pageNum = 1;
    // 每页显示的总条数
    private Integer pageSize = 10;
    // 总条数
    private Long total;
    //总页数
    private int pages;
    // 分页结果
    private List<T> items;

    public Page(PageInfo<T> pageInfo) {
        this.pageNum = pageInfo.getPageNum();
        this.pageSize = pageInfo.getPageSize();
        this.total = pageInfo.getTotal();
        this.pages = pageInfo.getPages();
        this.items = pageInfo.getList();
    }

    public Page(List<T> rawList, Integer pageNum, Integer pageSize) {
        this.total = (long) rawList.size();
        pageSize = pageSize > this.total ? this.total.intValue() : pageSize;
        List<T> list  = rawList.subList((pageNum - 1) * pageSize, Math.min(pageNum * pageSize, rawList.size()));
        this.items = list;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = (int) Math.ceil((double) list.size() / pageSize);
    }

}
