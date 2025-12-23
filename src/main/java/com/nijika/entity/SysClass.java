package com.nijika.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 班级信息实体
 */
@Data
public class SysClass implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String className; // 班级名称，如"软件232"，唯一约束
    private String counselorName; // 辅导员姓名
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt; // 软删除时间戳
}
