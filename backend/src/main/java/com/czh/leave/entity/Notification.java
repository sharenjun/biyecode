package com.czh.leave.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Notification {
    private Long id;
    private Long userId;
    private String content;
    private Integer isRead;
    private LocalDateTime createdAt;
}
