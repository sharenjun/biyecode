package com.czh.leave.mapper;

import com.czh.leave.entity.LeaveApplication;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface LeaveApplicationMapper {
    @Insert("INSERT INTO leave_applications (applicant_id, leave_type_id, flow_id, start_time, end_time, reason, contact_phone, attachment_url, status, current_step) " +
            "VALUES (#{applicantId}, #{leaveTypeId}, #{flowId}, #{startTime}, #{endTime}, #{reason}, #{contactPhone}, #{attachmentUrl}, #{status}, #{currentStep})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(LeaveApplication application);

    @Update("UPDATE leave_applications SET status = #{status}, current_step = #{currentStep}, updated_at = NOW() WHERE id = #{id}")
    int updateStatusAndStep(Long id, String status, Integer currentStep);

    @Select("SELECT * FROM leave_applications WHERE id = #{id}")
    LeaveApplication selectById(Long id);
    
    // For Teacher Task List (Filtered by Class)
    @Select("SELECT la.*, u.real_name as applicantName, u.class_name as applicantClassName " +
            "FROM leave_applications la " +
            "JOIN users u ON la.applicant_id = u.id " +
            "JOIN approval_flows af ON la.flow_id = af.id " +
            "JOIN approval_steps aps ON af.id = aps.flow_id " +
            "WHERE la.status = 'PENDING' " +
            "AND la.current_step = aps.step_order " +
            "AND aps.approver_role = 'TEACHER' " +
            "AND u.class_name = #{teacherClassName}") 
    List<LeaveApplication> selectPendingForTeacher(String teacherClassName);

    // For Student History
    @Select("SELECT la.*, u.real_name as applicantName, u.class_name as applicantClassName " +
            "FROM leave_applications la " +
            "JOIN users u ON la.applicant_id = u.id " +
            "WHERE applicant_id = #{applicantId} " +
            "ORDER BY created_at DESC")
    List<LeaveApplication> selectByApplicantId(Long applicantId);

    // For Teacher Dashboard: Count today's leaves
    @Select("SELECT COUNT(*) FROM leave_applications WHERE DATE(created_at) = CURDATE()")
    int countTodayLeaves();
}
