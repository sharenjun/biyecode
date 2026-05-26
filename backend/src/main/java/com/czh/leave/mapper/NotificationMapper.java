package com.czh.leave.mapper;

import com.czh.leave.entity.Notification;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface NotificationMapper {
    @Insert("INSERT INTO notifications (user_id, content) VALUES (#{userId}, #{content})")
    void insert(Long userId, String content);

    @Select("SELECT * FROM notifications WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Notification> selectByUserId(Long userId);
    
    @Update("UPDATE notifications SET is_read = 1 WHERE user_id = #{userId}")
    void markAllAsRead(Long userId);
}
