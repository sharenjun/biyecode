package com.czh.leave.controller;

import com.czh.leave.annotation.LogOperation;
import com.czh.leave.dto.LoginRequest;
import com.czh.leave.dto.LoginResponse;
import com.czh.leave.dto.RegisterRequest;
import com.czh.leave.entity.User;
import com.czh.leave.service.UserService;
import com.czh.leave.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @LogOperation("用户登录")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.login(request);

            // Generate Token
            String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());

            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setUser(user);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @LogOperation("用户注册")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
