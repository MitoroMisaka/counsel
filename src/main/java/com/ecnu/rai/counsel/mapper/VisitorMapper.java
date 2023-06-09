package com.ecnu.rai.counsel.mapper;

import com.ecnu.rai.counsel.entity.Visitor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Repository
public interface VisitorMapper extends BaseMapper<Visitor> {

    @Select("SELECT * FROM visitor WHERE id = #{id}")
    Visitor findById(@Param("id") Long id);

    @Update("UPDATE visitor SET name = #{visitor.name}, username = #{visitor.username}, password = #{visitor.password}, role = #{visitor.role}, " +
            "avatar = #{visitor.avatar}, phone = #{visitor.phone}, gender = #{visitor.gender}, department = #{visitor.department}, " +
            "title = #{visitor.title}, emergent_contact = #{visitor.emergentContact}, emergent_phone = #{visitor.emergentPhone} " +
            "WHERE id = #{visitor.id}")
    void updateVisitor(@Param("visitor") Visitor visitor);


    @Insert("INSERT INTO visitor (openid,id,name) VALUES (#{openid},#{id},'用户')")
    void insertVisitor(@Param("openid")String openid ,@Param("id")Long id);


    @Select("SELECT * FROM visitor")
    List<Visitor> getVisitorList();

    @Select("SELECT * FROM visitor WHERE openid = #{openid}")
    Visitor findByopenid(@Param("openid") String openid);

    @Delete("DELETE FROM visitor WHERE openid =#{openid}")
    void deleteVisitor(@Param("openid") String openid);

    @Select("SELECT count(*) FROM visitor where phone=#{phone} limit 1")
    int ifPhoneExist(@Param("phone")String phone);


    @Select("SELECT count(*) FROM visitor where openid=#{openid} limit 1")
    int ifVisitorExist(@Param("openid")String openid);

    @Select("SELECT id FROM visitor where openid=#{openid}")
    Long findIdbyopenid(@Param("openid")String openid);

    @Select("SELECT name,counselor.id  ,start_time ,end_time FROM (arrange join counselor on arrange.user=counselor.id) where #{localDateTime}>arrange.start_time and #{localDateTime}<arrange.end_time")
    List<HashMap<String,Object>> findAvailableCounselor(@Param("localDateTime") LocalDateTime localDateTime);
}
