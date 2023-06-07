package com.ecnu.rai.counsel.mapper;

import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervisor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
}
