package com.nijika.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BizInspection {
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
}
