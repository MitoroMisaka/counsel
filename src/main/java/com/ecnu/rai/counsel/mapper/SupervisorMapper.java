package com.ecnu.rai.counsel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.rai.counsel.entity.Supervisor;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface SupervisorMapper extends BaseMapper<Supervisor> {
    //find by id
    @Select("SELECT * FROM supervisor WHERE id = #{id}")
    Supervisor findById(Long id);

    @Update("UPDATE supervisor SET name = #{supervisor.name}, username = #{supervisor.username}, password = #{supervisor.password}, role = #{supervisor.role}, avatar = #{supervisor.avatar}, gender = #{supervisor.gender}, phone = #{supervisor.phone}, department = #{supervisor.department}, title = #{supervisor.title}, qualification = #{supervisor.qualification}, qualification_code = #{supervisor.qualificationCode} WHERE id = #{supervisor.id}")
    void updateSupervisor(@Param("supervisor") Supervisor supervisor);

}
