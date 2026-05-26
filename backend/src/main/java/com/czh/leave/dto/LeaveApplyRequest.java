package com.czh.leave.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LeaveApplyRequest {
    private Long applicantId;
    private Long leaveTypeId;
    private Long flowId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    
    private String reason;
    private String contactPhone; // New field
    private String attachmentUrl;
}
