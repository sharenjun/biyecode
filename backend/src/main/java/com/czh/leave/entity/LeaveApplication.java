package com.czh.leave.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
public class LeaveApplication {
    private Long id;
    private Long applicantId;
    private Long leaveTypeId;
    private Long flowId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal durationHours;
    private String reason;
    private String contactPhone; // New field
    private String attachmentUrl;
    private String status; // DRAFT, PENDING, APPROVED, REJECTED
    private Integer currentStep;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Extra fields for UI display (Not in table)
    private String applicantName;
    private String applicantClassName;
}
