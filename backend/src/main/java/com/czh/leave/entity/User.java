package com.czh.leave.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String realName;
    private String password;
    private String role; // STUDENT, TEACHER, ADMIN
    private Long departmentId;
    private String className; // New Field
    private String email;
    private String phone;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
