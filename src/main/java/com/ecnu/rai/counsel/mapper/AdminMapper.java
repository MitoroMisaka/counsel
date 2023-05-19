package com.ecnu.rai.counsel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.rai.counsel.entity.Admin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMapper extends BaseMapper<Admin> {
    //find by id
    @Select("SELECT * FROM admin WHERE id = #{id}")
    Admin findById(Long id);

    //update admin info : include name username password role avatar gender phone department title
    @Select("UPDATE admin SET name = #{admin.name}, username = #{admin.username}, password = #{admin.password}, role = #{admin.role} , " +
            "avatar = #{admin.avatar} , gender = #{admin.gender} , phone = #{admin.phone} , department = #{admin.department}, title = #{admin.title}" +
            "WHERE id = #{admin.id}")
    void updateAdmin(@Param("admin")Admin admin);

}
