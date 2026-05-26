package com.czh.leave.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OperationLog {
    private Long id;
    private Long userId;
    private String action;
    private String method; // New: Java Method Name
    private String params; // New: Request Params
    private String ipAddress;
    private LocalDateTime createdAt;
    
    // UI Display
    private String username;
    private String realName;
    private String role;
}
