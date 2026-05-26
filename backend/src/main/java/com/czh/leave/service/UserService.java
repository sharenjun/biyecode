package com.czh.leave.service;

import com.czh.leave.dto.LoginRequest;
import com.czh.leave.dto.RegisterRequest;
import com.czh.leave.entity.User;

public interface UserService {
    User login(LoginRequest request);
    User register(RegisterRequest request);
}
