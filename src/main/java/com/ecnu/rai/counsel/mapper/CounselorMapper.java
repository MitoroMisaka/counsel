package com.ecnu.rai.counsel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervisor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounselorMapper extends BaseMapper<Counselor> {
    //find by id
    @Select("SELECT * FROM counselor WHERE id = #{id}")
    Counselor findById(@Param("id") Long id);

    @Select("SELECT * FROM supervise WHERE counselor_id = #{id}")
    List<Supervisor> findSupervisors(@Param("id") Long id);

    //generate SQL statement to update counselor info
    @Update("UPDATE counselor SET " +
            "name = #{counselor.name}, " +
            "username = #{counselor.username}, " +
            "password = #{counselor.password}, " +
            "role = #{counselor.role}, " +
            "avatar = #{counselor.avatar}, " +
            "gender = #{counselor.gender}, " +
            "phone = #{counselor.phone}, " +
            "department = #{counselor.department}, " +
            "title = #{counselor.title}, " +
            "supervisors = #{counselor.supervisors}, " +
            "max_consults = #{counselor.maxConsults} " +
            "WHERE id = #{counselor.id}")
    void updateCounselor(@Param("counselor") Counselor counselor);

    @Insert("INSERT INTO counselor (name, username, password, role, avatar, gender, phone, department, title, supervisors, max_consults) " +
        "VALUES (#{counselor.name}, #{counselor.username}, #{counselor.password}, #{counselor.role}, #{counselor.avatar}, " +
        "#{counselor.gender}, #{counselor.phone}, #{counselor.department}, #{counselor.title}, #{counselor.supervisors}, " +
        "#{counselor.maxConsults})")
    void insertCounselor(@Param("counselor") Counselor counselor);
}