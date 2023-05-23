package com.ecnu.rai.counsel.mapper;

import com.ecnu.rai.counsel.entity.Visitor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitorMapper extends BaseMapper<Visitor> {

    @Select("SELECT * FROM visitor WHERE id = #{id}")
    Visitor findById(@Param("id") Long id);

    @Update("UPDATE visitor SET name = #{visitor.name}, username = #{visitor.username}, password = #{visitor.password}, role = #{visitor.role}, " +
            "avatar = #{visitor.avatar}, phone = #{visitor.phone}, gender = #{visitor.gender}, department = #{visitor.department}, " +
            "title = #{visitor.title}, emergent_contact = #{visitor.emergentContact}, emergent_phone = #{visitor.emergentPhone} , openid= #{visitor.openid}" +
            "WHERE id = #{visitor.id}")
    void updateVisitor(@Param("visitor") Visitor visitor);

    @Select("SELECT * FROM visitor")
    List<Visitor> getVisitorList();

    @Select("SELECT * FROM visitor WHERE openid = #{openid}")
    Visitor selectByOpenid(@Param("openid") String openid);

    @Insert("INSERT INTO visitor(id, openid, name, phone, emergent_contact, emergent_phone, role, gender, avatar) " +
        "VALUES(#{visitor.id}, #{visitor.openid}, #{visitor.name}, #{visitor.phone}, #{visitor.emergentContact}, #{visitor.emergentPhone}, " +
        "#{visitor.role}, #{visitor.gender}, #{visitor.avatar})")
    @Options(useGeneratedKeys = true, keyProperty = "visitor.id")
    void insertVisitor(@Param("visitor") Visitor visitor);
}
