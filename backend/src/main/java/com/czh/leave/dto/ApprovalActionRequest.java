package com.czh.leave.dto;

import lombok.Data;

@Data
public class ApprovalActionRequest {
    private Long applicationId;
    private Long approverId; // In real app, get from Security Context
    private String action; // APPROVE, REJECT
    private String comment;
}
