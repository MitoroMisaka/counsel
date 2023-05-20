package com.ecnu.rai.counsel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.rai.counsel.entity.Counselor;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface CounselorMapper extends BaseMapper<Counselor> {
    //find by id
    @Select("SELECT * FROM counselor WHERE id = #{id}")
    Counselor findById(Long id);

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
}