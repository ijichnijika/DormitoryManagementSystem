package com.nijika.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysRoom {
    private Long id;
    private Long buildingId;
    private String roomNumber;
    private Integer capacity;
    private Integer gender;
    private LocalDateTime createdAt;
}
