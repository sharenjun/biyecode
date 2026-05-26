package com.czh.leave.controller;

import com.czh.leave.annotation.LogOperation;
import com.czh.leave.dto.LeaveApplyRequest;
import com.czh.leave.entity.LeaveApplication;
import com.czh.leave.entity.LeaveType;
import com.czh.leave.entity.Notification;
import com.czh.leave.mapper.LeaveApplicationMapper;
import com.czh.leave.mapper.NotificationMapper;
import com.czh.leave.service.StudentLeaveService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student/leave")
@CrossOrigin(origins = "*") // Allow frontend access
public class StudentLeaveController {

    @Autowired
    private StudentLeaveService studentLeaveService;

    @Autowired
    private LeaveApplicationMapper leaveApplicationMapper; // Simple queries can use Mapper directly

    @Autowired
    private NotificationMapper notificationMapper;

    @GetMapping("/types")
    public ResponseEntity<List<LeaveType>> getLeaveTypes() {
        return ResponseEntity.ok(studentLeaveService.getAllLeaveTypes());
    }

    @LogOperation("提交请假申请")
    @PostMapping("/apply")
    public ResponseEntity<String> applyLeave(@RequestBody LeaveApplyRequest request) {
        try {
            studentLeaveService.submitApplication(request);
            return ResponseEntity.ok("Application submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<PageInfo<LeaveApplication>> getMyHistory(
            @RequestParam Long studentId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<LeaveApplication> list = leaveApplicationMapper.selectByApplicantId(studentId);
        return ResponseEntity.ok(new PageInfo<>(list));
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getNotifications(@RequestParam Long studentId) {
        return ResponseEntity.ok(notificationMapper.selectByUserId(studentId));
    }
}
