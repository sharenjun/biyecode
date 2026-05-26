package com.czh.leave.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String realName;
    private String role; // STUDENT, TEACHER
    private Long departmentId;
    private String className;
    private String email;
    private String phone;
}
