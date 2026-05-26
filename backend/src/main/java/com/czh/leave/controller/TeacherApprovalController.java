package com.czh.leave.controller;

import com.czh.leave.annotation.LogOperation;
import com.czh.leave.dto.ApprovalActionRequest;
import com.czh.leave.dto.ApprovalHistoryDTO;
import com.czh.leave.dto.DashboardStatsDTO;
import com.czh.leave.entity.LeaveApplication;
import com.czh.leave.mapper.ApprovalRecordMapper;
import com.czh.leave.mapper.LeaveApplicationMapper;
import com.czh.leave.service.TeacherApprovalService;
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
@RequestMapping("/api/teacher/approval")
@CrossOrigin(origins = "*")
public class TeacherApprovalController {

    @Autowired
    private TeacherApprovalService teacherApprovalService;

    @Autowired
    private ApprovalRecordMapper approvalRecordMapper;

    @Autowired
    private LeaveApplicationMapper leaveApplicationMapper;

    @GetMapping("/tasks")
    public ResponseEntity<List<LeaveApplication>> getMyTasks(@RequestParam Long teacherId) {
        return ResponseEntity.ok(teacherApprovalService.getPendingTasks(teacherId));
    }

    @LogOperation("审批请假单")
    @PostMapping("/action")
    public ResponseEntity<String> approveOrReject(@RequestBody ApprovalActionRequest request) {
        try {
            teacherApprovalService.processApproval(request);
            return ResponseEntity.ok("Processed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<ApprovalHistoryDTO>> getMyHistory(@RequestParam Long teacherId) {
        return ResponseEntity.ok(approvalRecordMapper.selectHistoryByApproverId(teacherId));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats(@RequestParam Long teacherId) {
        DashboardStatsDTO dto = new DashboardStatsDTO();
        dto.setTodayLeaveCount(leaveApplicationMapper.countTodayLeaves());
        dto.setMyProcessedCount(approvalRecordMapper.countProcessedToday(teacherId));
        return ResponseEntity.ok(dto);
    }
}
