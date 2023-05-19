package com.ecnu.rai.counsel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.entity.Visitor;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(@Param("id") Long id);

    @Update("UPDATE user SET name = #{user.name}, username = #{user.username}, password = #{user.password}, role = #{user.role}, " +
            "WHERE id = #{user.id}")
    void update(@Param("user") User user);

    @Select("SELECT * FROM user")
    List<User> getUserList();
}
