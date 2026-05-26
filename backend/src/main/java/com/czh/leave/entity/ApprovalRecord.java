package com.czh.leave.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApprovalRecord {
    private Long id;
    private Long applicationId;
    private Long stepId;
    private Long approverId;
    private String action; // APPROVE, REJECT, TRANSFER
    private String comment;
    private LocalDateTime createdAt;
}
