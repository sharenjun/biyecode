package com.czh.leave.mapper;

import com.czh.leave.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM users WHERE id = #{id}")
    User selectById(Long id);

    @Insert("INSERT INTO users (username, password, real_name, role, department_id, class_name, email, phone, status) " +
            "VALUES (#{username}, #{password}, #{realName}, #{role}, #{departmentId}, #{className}, #{email}, #{phone}, 1)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE users SET email = #{email}, phone = #{phone}, updated_at = NOW() WHERE id = #{id}")
    int updateProfile(User user);

    @Update("UPDATE users SET password = #{password}, updated_at = NOW() WHERE id = #{id}")
    int updatePassword(Long id, String password);

    @Select("SELECT department_id FROM users WHERE id = #{userId}")
    Long selectDepartmentIdByUserId(Long userId);
    
    @Select("SELECT COUNT(*) FROM users")
    int count();
}
