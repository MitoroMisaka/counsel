package com.ecnu.rai.counsel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.rai.counsel.dao.CounselorMonthlyStar;
import com.ecnu.rai.counsel.dao.CounselorMonthlyWork;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervise;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounselorMapper extends BaseMapper<Counselor> {

    @Update("UPDATE counselor SET status = 'ONLINE' WHERE username = #{username}")
    void setStatusOnline(@Param("username") String username);

    //update counselor rating
    @Update("UPDATE counselor SET rating = #{rating} WHERE name = #{name}")
    void updateCounselorRating(@Param("name") String name, @Param("rating") Integer rating);

    //set Status offline
    @Update("UPDATE counselor SET status = 'OFFLINE' WHERE username = #{username}")
    void setStatusOffline(@Param("username") String username);

    //find by id
    @Select("SELECT * FROM counselor WHERE id = #{id}")
    Counselor findById(@Param("id") Long id);

    @Select("SELECT id FROM counselor")
    List<Long> findId();

    @Select("SELECT counselor AS counselorId, COUNT(id) AS finishedConsults " +
            "FROM conversation WHERE `year` = YEAR(CURDATE()) AND `month` = MONTH(CURDATE()) AND `status` = \"FINISHED\" " +
            "GROUP BY counselor ORDER BY finishedconsults DESC limit #{len}")
    List<CounselorMonthlyWork> findCounselorRankingByWork(Integer len);

    @Select("SELECT counselor AS counselorId, COUNT(id) AS favouriteCommentNum " +
            "FROM conversation WHERE `year` = YEAR(CURDATE()) AND `month` = MONTH(CURDATE()) AND `evaluate` = 5 " +
            "GROUP BY counselor ORDER BY favouriteCommentNum DESC limit #{len}")
    List<CounselorMonthlyStar> findCounselorRankingByComments(Integer len);

    @Select("SELECT * FROM supervise WHERE counselor_id = #{id}")
    List<Supervise> findSupervisors(@Param("id") Long id);

    @Select("SELECT * FROM supervise WHERE supervisor_id = #{supervisorId} ORDER BY ${order}")
    List<Supervise> findCounselors(@Param("supervisorId") Long supervisorId,
                                   @Param("order") String order);

    @Select("SELECT * FROM counselor ORDER BY ${order}")
    List<Counselor> findAllCounselors(@Param("order") String order);

    @Select("SELECT * FROM counselor where status ='ONLINE' ORDER BY ${order}")
    List<Counselor> findAllCounselorsOnline(@Param("order") String order);

    //generate SQL statement to update counselor info
    @Update("UPDATE counselor SET " +
            "name = #{counselor.name}, " +
            "username = #{counselor.username}, " +
            "password = #{counselor.password}, " +
            "role = #{counselor.role}, " +
            "avatar = #{counselor.avatar}, " +
            "gender = #{counselor.gender}, " +
            "age = #{counselor.age}, " +
            "phone = #{counselor.phone}, " +
            "department = #{counselor.department}, " +
            "update_time = #{counselor.updateTime}, " +
            "title = #{counselor.title}, " +
            "max_consult = #{counselor.maxConsult}, " +
            "id_number = #{counselor.idNumber}, " +
            "email = #{counselor.email}, " +
            "status = #{counselor.status} " +
            "WHERE id = #{counselor.id}")
    void updateCounselor(@Param("counselor") Counselor counselor);

    @Insert("INSERT INTO counselor (id, name, username, password, role, avatar,id_number,  gender,age ,  phone, email, department, title, create_time, update_time,enabled, deleted, status, rating,  max_consult) " +
        "VALUES (#{counselor.id}, #{counselor.name}, #{counselor.username}, #{counselor.password}, #{counselor.role}, #{counselor.idNumber}, #{counselor.avatar}, " +
        "#{counselor.gender}, #{counselor.age}, #{counselor.phone}, #{counselor.email}, #{counselor.department}, #{counselor.title}, #{counselor.createTime}, #{counselor.updateTime} , " +
        "#{counselor.enabled}, #{counselor.deleted}, #{counselor.status},#{counselor.rating}, #{counselor.maxConsult})")
    void insertCounselor(@Param("counselor") Counselor counselor);

    //find all
    @Select("SELECT * FROM counselor")
    List<Counselor> findAll();

}