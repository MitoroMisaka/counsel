package com.ecnu.rai.counsel.mapper;

import com.ecnu.rai.counsel.entity.Visitor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
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
            "title = #{visitor.title}, emergent_contact = #{visitor.emergentContact}, emergent_phone = #{visitor.emergentPhone} " +
            "WHERE id = #{visitor.id}")
    void updateVisitor(@Param("user") Visitor visitor);

    @Insert("INSERT into visitor (openid,id) values (#{openid},#{id})")
    void insert( String openid,long id);

    @Select("SELECT * FROM visitor")
    List<Visitor> getVisitorList();

    @Select("SELECT * FROM visitor WHERE openid = #{openid}")
    Visitor findByopenid(@Param("openid") String openid);

    @Select("SELECT count(*) FROM visitor where openid=#{openid} limit 1")
    int ifVisitorExist(@Param("openid")String openid);


}
