package com.czh.leave.dto;

import com.czh.leave.entity.User;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private User user;
}
