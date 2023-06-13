package com.ecnu.rai.counsel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.rai.counsel.dao.UserBasicInfo;
import com.ecnu.rai.counsel.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<User> {

    //get id by name
    @Select("SELECT id FROM user WHERE name = #{name}")
    Long findIdByName(@Param("name") String name);

    //get name by id
    @Select("SELECT name FROM user WHERE id = #{id}")
    String findNameById(@Param("id") String id);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(@Param("id") Long id);

    @Select("SELECT * FROM user")
    List<UserBasicInfo> findAllUsers();

    @Select("SELECT * FROM user WHERE name = #{name}")
    User findByName(@Param("name") String name);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    @Update("UPDATE user SET name = #{user.name}, username = #{user.username}, password = #{user.password}, role = #{user.role},state = #{user.state}  " +
            "WHERE id = #{user.id}")
    void updateUser(@Param("user") User user);

    @Select("SELECT * FROM user")
    List<User> getUserList();

    @Insert("INSERT INTO user (name, username, password, role) " +
        "VALUES (#{user.name}, #{user.username}, #{user.password}, #{user.role})")
    void insertUser(@Param("user") User user);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO user(role,state) values ('visitor',1)")
    void addVisitor(@Param("user")User user);

    @Select("SELECT state FROM user WHERE id = #{id}")
    int findStateById(@Param("id") Long id);

    @Select("SELECT role FROM user WHERE id = #{id}")
    String findRoleById(@Param("id") Long id);



}
