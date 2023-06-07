package com.ecnu.rai.counsel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervise;
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
    List<Supervise> findSupervisors(@Param("id") Long id);

    @Select("SELECT * FROM supervise WHERE supervisor_id = #{id}")
    List<Supervise> findCounselors(@Param("id") Long id);

    //generate SQL statement to update counselor info
    @Update("UPDATE counselor SET " +
            "name = #{counselor.name}, " +
            "username = #{counselor.username}, " +
            "password = #{counselor.password}, " +
            "role = #{counselor.role}, " +
            "avatar = #{counselor.avatar}, " +
            "gender = #{counselor.gender}, " +
            "age = #{counselor.age}, " +
            "phone = #{counselor.phone}, " +
            "department = #{counselor.department}, " +
            "update_time = #{counselor.updateTime}, " +
            "title = #{counselor.title}, " +
            "max_consult = #{counselor.maxConsult}, " +
            "id_number = #{counselor.idNumber}, " +
            "email = #{counselor.email}, " +
            "status = #{counselor.status} " +
            "WHERE id = #{counselor.id}")
    void updateCounselor(@Param("counselor") Counselor counselor);

    @Insert("INSERT INTO counselor (id, name, username, password, role, avatar,id_number,  gender,age ,  phone, email, department, title, create_time, update_time,enabled, deleted, status, rating,  max_consult) " +
        "VALUES (#{counselor.id}, #{counselor.name}, #{counselor.username}, #{counselor.password}, #{counselor.role}, #{counselor.idNumber}, #{counselor.avatar}, " +
        "#{counselor.gender}, #{counselor.age}, #{counselor.phone}, #{counselor.email}, #{counselor.department}, #{counselor.title}, #{counselor.createTime}, #{counselor.updateTime} , " +
        "#{counselor.enabled}, #{counselor.deleted}, #{counselor.status},#{counselor.rating}, #{counselor.maxConsult})")
    void insertCounselor(@Param("counselor") Counselor counselor);
}