package com.nijika.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysBuilding {
    private Long id;
    private String buildingName;
    private String managerName;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
