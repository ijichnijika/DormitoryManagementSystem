package com.nijika.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 宿舍房间实体
 */
@Data
public class SysRoom implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long buildingId; // 外键 sys_building.id
    // 房间号：联合唯一约束 uk_building_room (building_id, room_number)
    private String roomNumber; // 如"b501"
    private Integer capacity; // 床位容量，默认4人间
    private Integer gender; // 性别限制：1=男寝 2=女寝
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt; // 软删除时间戳
    private transient String buildingName; // 楼栋名称，如"A栋"
}
