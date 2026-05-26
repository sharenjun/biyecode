package com.czh.leave.service;

import com.czh.leave.dto.LeaveApplyRequest;
import com.czh.leave.entity.LeaveType;
import java.util.List;

public interface StudentLeaveService {
    List<LeaveType> getAllLeaveTypes();
    void submitApplication(LeaveApplyRequest request);
}
