package com.nijika.common;

import com.github.pagehelper.PageInfo;
import lombok.Data;
import java.util.List;

/**
 * 分页响应类 - 统一分页结果封装
 */
@Data
public class PageResult<T> {
    private Long total; // 总记录数
    private Integer pageNum; // 当前页码
    private Integer pageSize; // 每页条数
    private Integer totalPages; // 总页数
    private List<T> records; // 当前页数据

    // 工具方法：从PageHelper的PageInfo转换为自定义PageResult
    public static <T> PageResult<T> of(PageInfo<T> pageInfo) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(pageInfo.getTotal());
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotalPages(pageInfo.getPages());
        result.setRecords(pageInfo.getList());
        return result;
    }
}
