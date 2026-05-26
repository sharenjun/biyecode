package com.czh.leave.entity;

import lombok.Data;

@Data
public class LeaveType {
    private Long id;
    private String name;
    private String code;
    private Integer requireAttachment; // 0: No, 1: Yes
    private Integer maxDays;
    private String description;
    private Integer enabled;
}
