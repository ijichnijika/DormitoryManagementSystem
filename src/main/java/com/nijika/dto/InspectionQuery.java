package com.nijika.dto;

import com.nijika.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * 卫生检查多条件查询DTO - 支持楼栋/宿舍/检查员/分数/日期范围筛选
 */
@Data
@EqualsAndHashCode(callSuper = true) // 继承PageRequest的equals/hashCode
public class InspectionQuery extends PageRequest {
    private Long buildingId; // 宿舍楼ID（通过room关联查询）
    private Long roomId; // 宿舍ID
    private Long inspectorId; // 检查员ID
    private Integer minScore; // 最低分数，如60（查询不及格）
    private Integer maxScore; // 最高分数，如100
    private LocalDate startDate; // 开始日期
    private LocalDate endDate; // 结束日期
}
