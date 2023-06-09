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

    @Select("SELECT id FROM supervisor")
    List<Long> findId();


    @Update("UPDATE supervisor SET status = 'ONLINE' WHERE username = #{username}")
    void setStatusOnline(@Param("username") String username);

    @Update("UPDATE supervisor SET status = 'OFFLINE' WHERE username = #{username}")
    void setStatusOffline(@Param("username") String username);

    @Select("SELECT * FROM supervisor ORDER BY ${order}")
    List<Supervisor> getAll(@Param("order") String order);

    @Select("SELECT * FROM supervisor where status ='ONLINE' ORDER BY ${order}")
    List<Supervisor> findAllSupervisorsOnline(@Param("order") String order);

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
            "max_consult = #{counselor.maxConsult}, " +
            "status = #{counselor.status} " +
            "WHERE id = #{supervisor.id}")
    void updateSupervisor(@Param("supervisor") Supervisor supervisor);

    @Insert("INSERT INTO supervisor (id, name, username, password, role, avatar, gender, phone, department, title, qualification, qualification_code) " +
            "VALUES (#{supervisor.id}, #{supervisor.name}, #{supervisor.username}, #{supervisor.password}, #{supervisor.role}, " +
            "#{supervisor.avatar}, #{supervisor.gender}, #{supervisor.phone}, " +
            "#{supervisor.department}, #{supervisor.title}, #{supervisor.qualification}, #{supervisor.qualificationCode})")
    void insertSupervisor(@Param("supervisor") Supervisor supervisor);

    @Select("SELECT user FROM arrange WHERE NOW()>start_time AND NOW()<end_time AND role='SUPERVISOR' ORDER BY ${order}")
    List<Long> findSupervisorByCurrentTime(@Param("order") String order);
}
