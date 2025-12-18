package com.nijika.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 宿舍楼信息实体
 */
@Data
public class SysBuilding implements Serializable {
    private static final long serialVersionUID = 1L; // Redis序列化必需
    private Long id;
    private String buildingName; // 楼宇名称，如"东区4号楼"
    private String managerName; // 宿管姓名
    private LocalDateTime createdAt;
    // 软删除标记，查询需加 deleted_at IS NULL
    private LocalDateTime deletedAt;
}
