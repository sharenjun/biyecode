package com.czh.leave.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
public class ApprovalHistoryDTO {
    // Record Info
    private Long recordId;
    private String action; // APPROVE, REJECT
    private String comment;
    private LocalDateTime actionTime;
    
    // Application Info
    private Long applicationId;
    private Long applicantId;
    private String realName; // Student Name
    private String className; // New field
    private String leaveTypeName;
    private String contactPhone;
    private String attachmentUrl;
    private String reason;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal durationHours;
}
