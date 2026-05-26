package com.czh.leave.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private Long userId;
    private String email;
    private String phone;
}
