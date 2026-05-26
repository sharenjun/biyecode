package com.czh.leave.mapper;

import com.czh.leave.entity.LeaveType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface LeaveTypeMapper {
    @Select("SELECT * FROM leave_types WHERE enabled = 1")
    List<LeaveType> selectAllEnabled();
    
    @Select("SELECT * FROM leave_types WHERE id = #{id}")
    LeaveType selectById(Long id);
}
