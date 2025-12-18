package com.nijika.common;

import lombok.Data;

/**
 * 分页请求基类 - 所有分页查询DTO的父类
 */
@Data
public class PageRequest {
    private Integer pageNum = 1; // 当前页码，默认第1页
    private Integer pageSize = 10; // 每页条数，默认10条
    private String orderBy; // 排序字段，如 "check_date DESC"（可选）

    public void setPageSize(Integer pageSize) { // 限制分页大小，防止恶意大量查询
        if (pageSize != null && pageSize > 100) {
            this.pageSize = 100; // 最大100条/页
        } else if (pageSize != null && pageSize > 0) {
            this.pageSize = pageSize;
        }
    }
}
