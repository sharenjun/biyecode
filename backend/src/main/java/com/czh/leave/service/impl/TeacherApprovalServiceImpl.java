package com.czh.leave.service.impl;

import com.czh.leave.dto.ApprovalActionRequest;
import com.czh.leave.entity.ApprovalRecord;
import com.czh.leave.entity.ApprovalStep;
import com.czh.leave.entity.LeaveApplication;
import com.czh.leave.entity.User;
import com.czh.leave.mapper.ApprovalRecordMapper;
import com.czh.leave.mapper.ApprovalStepMapper;
import com.czh.leave.mapper.LeaveApplicationMapper;
import com.czh.leave.mapper.NotificationMapper;
import com.czh.leave.mapper.UserMapper;
import com.czh.leave.service.TeacherApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeacherApprovalServiceImpl implements TeacherApprovalService {

    @Autowired
    private LeaveApplicationMapper leaveApplicationMapper;

    @Autowired
    private ApprovalStepMapper approvalStepMapper;

    @Autowired
    private ApprovalRecordMapper approvalRecordMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<LeaveApplication> getPendingTasks(Long teacherId) {
        User teacher = userMapper.selectById(teacherId);
        if (teacher == null) {
            throw new RuntimeException("Teacher not found");
        }
        return leaveApplicationMapper.selectPendingForTeacher(teacher.getClassName());
    }

    @Override
    @Transactional
    public void processApproval(ApprovalActionRequest req) {
        LeaveApplication app = leaveApplicationMapper.selectById(req.getApplicationId());
        if (app == null) {
            throw new RuntimeException("Application not found");
        }

        ApprovalStep currentStep = approvalStepMapper.selectByFlowAndOrder(app.getFlowId(), app.getCurrentStep());
        if (currentStep == null) {
            throw new RuntimeException("Invalid approval step");
        }

        ApprovalRecord record = new ApprovalRecord();
        record.setApplicationId(app.getId());
        record.setStepId(currentStep.getId());
        record.setApproverId(req.getApproverId());
        record.setAction(req.getAction());
        record.setComment(req.getComment());
        approvalRecordMapper.insert(record);

        String statusMessage = "";
        if ("REJECT".equals(req.getAction())) {
            leaveApplicationMapper.updateStatusAndStep(app.getId(), "REJECTED", app.getCurrentStep());
            statusMessage = "已驳回";
        } else if ("APPROVE".equals(req.getAction())) {
            ApprovalStep nextStep = approvalStepMapper.selectByFlowAndOrder(app.getFlowId(), app.getCurrentStep() + 1);
            if (nextStep != null) {
                leaveApplicationMapper.updateStatusAndStep(app.getId(), "PENDING", nextStep.getStepOrder());
                statusMessage = "已通过初审，进入下一步";
            } else {
                leaveApplicationMapper.updateStatusAndStep(app.getId(), "APPROVED", app.getCurrentStep());
                statusMessage = "已最终通过";
            }
        }

        notificationMapper.insert(
                app.getApplicantId(),
                "您的请假单(ID:" + app.getId() + ") " + statusMessage + "。备注: "
                        + (req.getComment() == null ? "无" : req.getComment())
        );
    }
}
