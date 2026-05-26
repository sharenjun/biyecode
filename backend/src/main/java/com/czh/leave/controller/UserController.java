package com.czh.leave.controller;

import com.czh.leave.annotation.LogOperation;
import com.czh.leave.dto.UpdatePasswordRequest;
import com.czh.leave.dto.UpdateProfileRequest;
import com.czh.leave.entity.User;
import com.czh.leave.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @LogOperation("更新个人信息")
    @PostMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody UpdateProfileRequest request) {
        User user = userMapper.selectById(request.getUserId());
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        userMapper.updateProfile(user);

        return ResponseEntity.ok("Profile updated successfully");
    }

    @LogOperation("修改密码")
    @PostMapping("/password")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequest request) {
        User user = userMapper.selectById(request.getUserId());
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (!user.getPassword().equals(request.getOldPassword())) {
            return ResponseEntity.badRequest().body("原密码错误");
        }

        userMapper.updatePassword(request.getUserId(), request.getNewPassword());
        return ResponseEntity.ok("Password updated successfully");
    }
}
