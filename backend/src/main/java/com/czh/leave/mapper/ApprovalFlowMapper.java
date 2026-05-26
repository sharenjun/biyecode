package com.czh.leave.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ApprovalFlowMapper {
    @Select("SELECT id FROM approval_flows WHERE department_id = #{departmentId} AND leave_type_id = #{leaveTypeId} LIMIT 1")
    Long selectFlowId(Long departmentId, Long leaveTypeId);
    
    @Select("SELECT id FROM approval_flows WHERE department_id = 0 AND leave_type_id = #{leaveTypeId} LIMIT 1")
    Long selectDefaultFlowId(Long leaveTypeId);
}
