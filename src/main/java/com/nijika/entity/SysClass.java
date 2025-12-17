package com.nijika.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysClass {
    private Long id;
    private String className;
    private String counselorName;
    private LocalDateTime createdAt;
}
