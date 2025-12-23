package com.nijika.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 卫生检查记录实体
 */
@Data
public class BizInspection implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long roomId;
    private Long inspectorId;
    private Long modifierId;
    private Integer totalScore;
    private String remarks;
    private String evidenceImgs;
    private LocalDate checkDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // 扩展字段：用于前端显示，不持久化到数据库
    private transient String roomNumber; // 房间号如"401"
    private transient String buildingName; // 楼栋名如"A栋"
}
