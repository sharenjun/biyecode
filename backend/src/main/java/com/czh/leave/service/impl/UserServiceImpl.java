package com.czh.leave.service.impl;

import com.czh.leave.dto.LoginRequest;
import com.czh.leave.dto.RegisterRequest;
import com.czh.leave.entity.User;
import com.czh.leave.mapper.UserMapper;
import com.czh.leave.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(LoginRequest request) {
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return user;
    }

    @Override
    public User register(RegisterRequest request) {
        User existing = userMapper.findByUsername(request.getUsername());
        if (existing != null) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // In real app, hash this!
        user.setRealName(request.getRealName());
        user.setRole(request.getRole());
        user.setDepartmentId(request.getDepartmentId());
        user.setClassName(request.getClassName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        
        userMapper.insert(user);
        return user;
    }
}
