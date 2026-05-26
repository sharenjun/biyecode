package com.czh.leave.mapper;

import com.czh.leave.entity.OperationLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface OperationLogMapper {
    @Insert("INSERT INTO operation_logs (user_id, action, ip_address) VALUES (#{userId}, #{action}, #{ipAddress})")
    int insert(OperationLog log);

    @Select("SELECT ol.*, u.username, u.real_name as realName, u.role " +
            "FROM operation_logs ol " +
            "LEFT JOIN users u ON ol.user_id = u.id " +
            "ORDER BY ol.created_at DESC")
    List<OperationLog> selectAll();
}
