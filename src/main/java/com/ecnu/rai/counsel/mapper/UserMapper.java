package com.ecnu.rai.counsel.mapper;

import com.ecnu.rai.counsel.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
            "avatar = #{user.avatar}, phone = #{user.phone}, gender = #{user.gender}, department = #{user.department}, " +
            "emergent_contact = #{user.emergentContact}, emergent_phone = #{user.emergentPhone} " +
            "WHERE id = #{user.id}")
    void update(@Param("user") User user);

    @Select("SELECT * FROM user")
    List<User> getUserList();
}
