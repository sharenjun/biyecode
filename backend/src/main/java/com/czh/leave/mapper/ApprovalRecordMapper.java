package com.czh.leave.mapper;

import com.czh.leave.dto.ApprovalHistoryDTO;
import com.czh.leave.entity.ApprovalRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ApprovalRecordMapper {
    @Insert("INSERT INTO approval_records (application_id, step_id, approver_id, action, comment) " +
            "VALUES (#{applicationId}, #{stepId}, #{approverId}, #{action}, #{comment})")
    int insert(ApprovalRecord record);

    @Select("SELECT ar.id as recordId, ar.action, ar.comment, ar.created_at as actionTime, " +
            "la.id as applicationId, la.applicant_id as applicantId, u.real_name as realName, u.class_name as className, " +
            "lt.name as leaveTypeName, la.contact_phone as contactPhone, la.attachment_url as attachmentUrl, " +
            "la.reason, la.start_time as startTime, la.end_time as endTime, la.duration_hours as durationHours " +
            "FROM approval_records ar " +
            "JOIN leave_applications la ON ar.application_id = la.id " +
            "JOIN users u ON la.applicant_id = u.id " +
            "JOIN leave_types lt ON la.leave_type_id = lt.id " +
            "WHERE ar.approver_id = #{approverId} " +
            "ORDER BY ar.created_at DESC")
    List<ApprovalHistoryDTO> selectHistoryByApproverId(Long approverId);

    @Select("SELECT COUNT(*) FROM approval_records WHERE approver_id = #{approverId} AND DATE(created_at) = CURDATE()")
    int countProcessedToday(Long approverId);
}
