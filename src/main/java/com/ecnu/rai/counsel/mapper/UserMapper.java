package com.ecnu.rai.counsel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.entity.Visitor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(@Param("id") Long id);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    @Update("UPDATE user SET name = #{user.name}, username = #{user.username}, password = #{user.password}, role = #{user.role}  " +
            "WHERE id = #{user.id}")
    void updateUser(@Param("user") User user);

    @Select("SELECT * FROM user")
    List<User> getUserList();

    @Insert("INSERT INTO user (name, username, password, role, create_time, update_time, enabled, deleted) " +
        "VALUES (#{user.name}, #{user.username}, #{user.password}, #{user.role}, #{user.createTime}, " +
        "#{user.updateTime}, #{user.enabled}, #{user.deleted})")
    void insertUser(@Param("user") User user);
}
