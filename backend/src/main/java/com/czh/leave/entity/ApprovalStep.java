package com.czh.leave.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApprovalStep {
    private Long id;
    private Long flowId;
    private Integer stepOrder;
    private String approverRole; // TEACHER, HEAD_TEACHER, DEAN, ADMIN
    private String description;
}
