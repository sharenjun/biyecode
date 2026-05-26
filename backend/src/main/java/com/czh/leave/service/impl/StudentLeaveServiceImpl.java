package com.czh.leave.service.impl;

import com.czh.leave.dto.LeaveApplyRequest;
import com.czh.leave.entity.LeaveApplication;
import com.czh.leave.entity.LeaveType;
import com.czh.leave.mapper.ApprovalFlowMapper;
import com.czh.leave.mapper.LeaveApplicationMapper;
import com.czh.leave.mapper.LeaveTypeMapper;
import com.czh.leave.mapper.UserMapper;
import com.czh.leave.service.StudentLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@Service
public class StudentLeaveServiceImpl implements StudentLeaveService {

    @Autowired
    private LeaveTypeMapper leaveTypeMapper;

    @Autowired
    private LeaveApplicationMapper leaveApplicationMapper;
    
    @Autowired
    private ApprovalFlowMapper approvalFlowMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeMapper.selectAllEnabled();
    }

    @Override
    @Transactional
    public void submitApplication(LeaveApplyRequest req) {
        // 1. Get User's Department
        Long deptId = userMapper.selectDepartmentIdByUserId(req.getApplicantId());
        if (deptId == null) {
            throw new RuntimeException("User department not found");
        }

        // 2. Find Approval Flow
        Long flowId = approvalFlowMapper.selectFlowId(deptId, req.getLeaveTypeId());
        if (flowId == null) {
            // Try default flow (dept_id = 0)
            flowId = approvalFlowMapper.selectDefaultFlowId(req.getLeaveTypeId());
        }
        if (flowId == null) {
            throw new RuntimeException("No matching approval flow found for this leave type");
        }

        // 3. Calculate Duration
        long minutes = Duration.between(req.getStartTime(), req.getEndTime()).toMinutes();
        BigDecimal hours = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_HALF_UP);

        // 4. Create Application
        LeaveApplication app = new LeaveApplication();
        app.setApplicantId(req.getApplicantId());
        app.setLeaveTypeId(req.getLeaveTypeId());
        app.setFlowId(flowId);
        app.setStartTime(req.getStartTime());
        app.setEndTime(req.getEndTime());
        app.setDurationHours(hours);
        app.setReason(req.getReason());
        app.setContactPhone(req.getContactPhone());
        app.setAttachmentUrl(req.getAttachmentUrl());
        app.setStatus("PENDING");
        app.setCurrentStep(1); // Start at step 1

        leaveApplicationMapper.insert(app);
    }
}
