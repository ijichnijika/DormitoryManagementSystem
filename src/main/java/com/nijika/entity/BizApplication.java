package com.nijika.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BizApplication {
    private Long id;
    private Long applicantId;
    private String applicationReason;
    private Integer status;
    private Long reviewerId;
    private String reviewComment;
    private LocalDateTime reviewTime;
    private LocalDateTime applyTime;
    private LocalDateTime updatedAt;
}
