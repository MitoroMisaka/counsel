package com.ecnu.rai.counsel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.rai.counsel.dao.CounselorMonthlyStar;
import com.ecnu.rai.counsel.dao.CounselorMonthlyWork;
import com.ecnu.rai.counsel.entity.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

@Repository
public interface DialogueMapper extends BaseMapper<Dialogue>{
    @Select("SELECT * FROM dialogue WHERE id = #{dialogue_id}")
    Conversation getDialogueById(@Param("dialogue_id") Long dialogue_id);

    @Select("SELECT * FROM dialogue WHERE counselor = #{counselor} AND supervisor = #{supervisor} AND status = 'FINISHED' ORDER BY id DESC")
    List<Dialogue> findGroupMsgByBoth(String counselor, String supervisor);

    @Select("SELECT * FROM dialogue WHERE counselor = #{counselor} AND status = 'FINISHED' ORDER BY id DESC")
    List<Dialogue> findGroupMsgByCounselor(String counselor);

    @Select("SELECT * FROM dialogue WHERE supervisor = #{supervisor} AND status = 'FINISHED' ORDER BY id DESC")
    List<Dialogue> findGroupMsgBySupervisor(String supervisor);

    @Select("SELECT max_consult FROM supervisor WHERE id = #{supervisor}")
    Integer getMaxConsult(@Param("supervisor") String supervisor);

    @Select("SELECT count(*) FROM dialogue WHERE supervisor = #{supervisor} AND status = 'STARTED'")
    Integer getConsultNum(@Param("supervisor") String supervisor);

    @Select("SELECT * FROM dialogue WHERE id = #{id}")
    Dialogue findById(@Param("id") Long id);

    @Insert("INSERT INTO dialogue " +
            "(id, create_time, creator, last_update_time, last_updater, " +
            "year, month, day, start_time, end_time, supervisor, counselor, status, " +
            "visitor_name) VALUES " +
            "(#{dialogue.id}, #{dialogue.createTime}, #{dialogue.creator}, #{dialogue.lastUpdateTime}, #{dialogue.lastUpdater}, " +
            "#{dialogue.year}, #{dialogue.month}, #{dialogue.day}, #{dialogue.startTime}, #{dialogue.endTime}, #{dialogue.supervisor}, #{dialogue.counselor}, #{dialogue.status}, " +
            "#{dialogue.visitorName})")
    void insertDialogueByID(@Param("dialogue") Dialogue dialogue);

    @Select("SELECT * FROM dialogue ORDER BY id DESC LIMIT 1")
    Dialogue getLastDialogue();

    @Insert("INSERT INTO dialogue (create_time, creator, last_update_time, last_updater, year, month, day, start_time, end_time, supervisor, counselor, status, visitor_name,  conversation_type, message) " +
            "VALUES (#{dialogue.createTime}, #{dialogue.creator}, #{dialogue.lastUpdateTime}, #{dialogue.lastUpdater}, #{dialogue.year}, #{dialogue.month}, #{dialogue.day}, #{dialogue.startTime}, #{dialogue.endTime}, #{dialogue.supervisor}, #{dialogue.counselor}, #{dialogue.status}, #{dialogue.visitorName}, #{dialogue.message})")
    @Options(useGeneratedKeys = true, keyProperty = "dialogue.id")
    void insertDialogue(@Param("dialogue") Dialogue dialogue);

    @Update("UPDATE dialogue SET " +
            "create_time = #{dialogue.createTime}, " +
            "creator = #{dialogue.creator}, " +
            "last_update_time = #{dialogue.lastUpdateTime}, " +
            "last_updater = #{dialogue.lastUpdater}, " +
            "year = #{dialogue.year}, " +
            "month = #{dialogue.month}, " +
            "day = #{dialogue.day}, " +
            "start_time = #{dialogue.startTime}, " +
            "end_time = #{dialogue.endTime}, " +
            "supervisor = #{dialogue.supervisor}, " +
            "counselor = #{dialogue.counselor}, " +
            "status = #{dialogue.status}, " +
            "visitor_name = #{dialogue.visitorName}, " +
            "dialogue_type = #{dialogue.dialogueType}," +
            "message = #{dialogue.message}" +
            "WHERE id = #{dialogue.id}")
    void updateDialogue(@Param("dialogue") Dialogue dialogue);

    @Select("SELECT * FROM dialogue WHERE supervisor = #{supervisor}")
    List<Dialogue> findBySupervisor(@Param("supervisor") Long supervisor);

    @Select("SELECT * FROM dialogue WHERE counselor = #{counselor}")
    List<Dialogue> findByCounselor(@Param("counselor") Long counselor);

    @Select("SELECT * FROM dialogue WHERE " +
            "counselor = #{counselor} AND " +
            "supervisor = #{supervisor} AND " +
            "DATE(start_time) = #{date}")
    List<Dialogue> findByBothDate(@Param("counselor") Long counselor,
                                               @Param("supervisor") Long supervisor,
                                               @Param("date") Date date);

    @Select("SELECT SUM( ( UNIX_TIMESTAMP(end_time) - UNIX_TIMESTAMP(start_time) ) / 60 ) " +
            "FROM dialogue " +
            "WHERE supervisor= #{supervisor}")
    Integer findTotalBySupervisor(@Param("supervisor") Long supervisor);

    @Select("SELECT COUNT(*) " +
            "FROM dialogue " +
            "WHERE supervisor= #{supervisor}")
    Integer findTotalNumSupervisor(@Param("supervisor") Long supervisor);

    @Select("SELECT COUNT(*) " +
            "FROM dialogue " +
            "WHERE supervisor= #{supervisor} AND  +" +
            "DATE(start_time) = DATE(SYSDATE()) ")
    Integer findTodayNumBySupervisor(@Param("supervisor") Long supervisor);

    @Select("SELECT COUNT(*) " +
            "FROM dialogue " +
            "WHERE  DATE(start_time) = DATE(SYSDATE())")
    Integer findTodayNum();

    @Select("SELECT COUNT(*) " +
            "FROM dialogue " +
            "WHERE  DATE(start_time) = DATE_SUB(DATE(SYSDATE()), INTERVAL #{days} DAY)")
    Integer findNumDaysAgo(@Param("days") Integer days);

    @Select("SELECT COUNT(*) " +
            "FROM dialogue " +
            "WHERE  DATE(start_time) =  DATE_SUB(DATE(SYSDATE()), INTERVAL #{days} DAY) AND HOUR(start_time) < #{hours} " +
            "AND HOUR(start_time) >= #{hours}-2")
    Integer findNumByHours(@Param("hours") Integer hours ,@Param("days") Integer days);

    @Select("SELECT SUM( ( UNIX_TIMESTAMP(end_time) - UNIX_TIMESTAMP(start_time) ) / 60 ) " +
            "FROM dialogue " +
            "WHERE supervisor= #{supervisor} AND " +
            "DATE(start_time) = DATE(SYSDATE())")
    Integer findTodayTotalBySupervisor(@Param("supervisor") Long supervisor);

    @Select("SELECT SUM( ( UNIX_TIMESTAMP(end_time) - UNIX_TIMESTAMP(start_time) ) / 60 ) " +
            "FROM dialogue " +
            "WHERE DATE(start_time) = DATE(SYSDATE())")
    Integer findTodayTotal();

    @Select("SELECT COUNT(*) " +
            "FROM dialogue " +
            "WHERE  status = 'STARTED' ")
    Integer findCurrentNum();

    @Select("SELECT COUNT(*) " +
            "FROM dialogue " +
            "WHERE supervisor= #{supervisor} AND " +
            "status = 'active'")
    Integer findCurrentNumBySupervisor(@Param("supervisor") Long supervisor);


}
