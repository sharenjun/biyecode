package com.czh.leave.service;

import com.czh.leave.dto.ApprovalActionRequest;
import com.czh.leave.entity.LeaveApplication;
import java.util.List;

public interface TeacherApprovalService {
    List<LeaveApplication> getPendingTasks(Long teacherId);
    void processApproval(ApprovalActionRequest request);
}
