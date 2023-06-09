package com.ecnu.rai.counsel.mapper;

import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervise;
import com.ecnu.rai.counsel.entity.Supervisor;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface SuperviseMapper {
    @Insert("INSERT INTO supervise values (#{counselorid},#{supervisorid})")
    void makeSupervise(@Param("counselor")Long counselorid, @Param("supervisor")Long supervisorid);

    @Select("Select supervisor_id,name from supervisor where counselor_id = #{counselor.id}")
    List<HashMap<String,Object>> selectBindedSupervisor(@Param("counselor")Counselor counselor);

    @Select("Select counselor_id,name from supervisor where supervisor_id = #{supervisor.id}")
    List<HashMap<String,Object>> selectBindedCounselor(@Param("supervisor")Supervisor supervisor);

    @Select("SELECT * FROM supervise WHERE counselor_id = #{id}")
    List<Supervise> findSupervisors(@Param("id") Long id);

    @Select("SELECT * FROM supervise WHERE supervisor_id = #{id}")
    List<Supervise> findCounselors(@Param("id") Long id);

    @Delete("DELETE FROM supervise WHERE counselor_id = #{counselor.id} and supervisor_id = #{supervisor.id}")
    void deleteSupervise(@Param("counselor")Long counselorid, @Param("supervisor")Long supervisorid);
}
