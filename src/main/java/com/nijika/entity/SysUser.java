package com.nijika.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统用户实体
 * 核心设计：用逗号分隔的角色字符串替代传统RBAC多表设计（如 "STUDENT,INSPECTOR"）
 */
@Data
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username; // 学号/工号，唯一约束
    private String password; // BCrypt加密存储
    private String realName;
    private String phone;
    private String email;
    private String role; // 多角色字段：逗号分隔，如 "STUDENT,INSPECTOR"，通过 LIKE 查询
    // TODO: 角色超过10种时建议重构为独立角色表
    private Long classId; // 外键 sys_class.id
    private Long roomId; // 外键 sys_room.id，用于查询本宿舍卫生记录
    private Integer status; // 1=正常 0=禁用，用于封禁违规账号
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // 软删除标记，保留历史数据关联，所有查询需加 deleted_at IS NULL
    private LocalDateTime deletedAt;

    private transient String className;
    private transient String roomNumber;
    private transient String buildingName;
}
