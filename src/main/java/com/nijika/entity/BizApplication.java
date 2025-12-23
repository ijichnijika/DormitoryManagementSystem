package com.nijika.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 检查员权限申请实体
 * 状态机：0待审核 → 1已通过（同步修改sys_user.role）/ 2已驳回
 */
@Data
public class BizApplication implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long applicantId; // 申请人ID，外键 sys_user.id
    private String applicationReason;
    private Integer status; // 0=待审核 1=已通过 2=已驳回
    // 审核信息：通过时填充reviewerId和reviewTime，驳回时额外填写reviewComment
    private Long reviewerId; // 审核人（教师）
    private String reviewComment; // 驳回理由
    private LocalDateTime reviewTime;
    private LocalDateTime applyTime;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt; // 软删除时间戳
}
