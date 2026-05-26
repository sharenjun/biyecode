package com.czh.leave.controller;

import com.czh.leave.entity.OperationLog;
import com.czh.leave.mapper.OperationLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @GetMapping("/logs")
    public ResponseEntity<List<OperationLog>> getAllLogs() {
        return ResponseEntity.ok(operationLogMapper.selectAll());
    }
}
