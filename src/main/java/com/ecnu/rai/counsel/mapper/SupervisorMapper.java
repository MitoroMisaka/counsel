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
public interface SupervisorMapper extends BaseMapper<Supervisor> {
    //find by id
    @Select("SELECT * FROM supervisor WHERE id = #{id}")
    Supervisor findById(Long id);

    @Select("SELECT * FROM supervise WHERE counselor_id = #{id}")
    List<Supervise> findSupervisors(@Param("id") Long id);

    @Select("SELECT * FROM supervise WHERE supervisor_id = #{id}")
    List<Supervise> findCounselors(@Param("id") Long id);

    @Update("UPDATE supervisor SET " +
            "name = #{supervisor.name}, " +
            "username = #{supervisor.username}, " +
            "password = #{supervisor.password}, " +
            "role = #{supervisor.role}, " +
            "avatar = #{supervisor.avatar}, " +
            "gender = #{supervisor.gender}, " +
            "phone = #{supervisor.phone}, " +
            "department = #{supervisor.department}, " +
            "title = #{supervisor.title}, " +
            "qualification = #{supervisor.qualification}, " +
            "qualification_code = #{supervisor.qualificationCode} " +
            "WHERE id = #{supervisor.id}")
    void updateSupervisor(@Param("supervisor") Supervisor supervisor);

    @Insert("INSERT INTO supervisor (id, name, username, password, role, avatar, gender, phone, department, title, qualification, qualification_code) " +
            "VALUES (#{supervisor.id}, #{supervisor.name}, #{supervisor.username}, #{supervisor.password}, #{supervisor.role}, " +
            "#{supervisor.avatar}, #{supervisor.gender}, #{supervisor.phone}, " +
            "#{supervisor.department}, #{supervisor.title}, #{supervisor.qualification}, #{supervisor.qualificationCode})")
    void insertSupervisor(@Param("supervisor") Supervisor supervisor);
}
