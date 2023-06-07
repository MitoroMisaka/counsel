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
public interface SupervisorMapper extends BaseMapper<Supervisor> {
    //find by id
    @Select("SELECT * FROM supervisor WHERE id = #{id}")
    Supervisor findById(Long id);

    @Select("SELECT * FROM supervise WHERE supervisor_id = #{id}")
    List<Counselor> findCounselors(@Param("id") Long id);


    @Update("UPDATE supervisor SET " +
            "name = #{supervisor.name}, " +
            "username = #{supervisor.username}, " +
            "password = #{supervisor.password}, " +
            "role = 'supervisor', " +
            "avatar = #{supervisor.avatar}, " +
            "gender = #{supervisor.gender}, " +
            "phone = #{supervisor.phone}, " +
            "department = #{supervisor.department}, " +
            "title = #{supervisor.title}, " +
            "qualification = #{supervisor.qualification}, " +
            "qualification_code = #{supervisor.qualificationCode} " +
            "WHERE id = #{supervisor.id}")
    void updateSupervisor(@Param("supervisor") Supervisor supervisor);

    @Insert("INSERT INTO supervisor values(#{supervisor.name}," +
            "#{supervisor.password},'supervisor',#{supervisor.avatar}," +
            " #{supervisor.gender},#{supervisor.phone},#{supervisor.department}, " +
            "#{supervisor.title},#{supervisor.qualification}, #{supervisor.qualificationCode} )")
    void insertSupervisor(@Param("supervisor") Supervisor supervisor);



}
