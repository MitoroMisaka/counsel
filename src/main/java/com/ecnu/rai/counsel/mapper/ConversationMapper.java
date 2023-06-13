package com.ecnu.rai.counsel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.rai.counsel.entity.Arrange;
import com.ecnu.rai.counsel.entity.Conversation;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ConversationMapper extends BaseMapper<Conversation> {

    @Select("SELECT * FROM conversation WHERE counselor = #{counselor} AND user = #{user} AND status = 'FINISHED' ORDER BY id DESC")
    List<Conversation> findGroupMsgByCounselorUser(String counselor, String user);

    @Select("SELECT * FROM conversation WHERE counselor = #{counselor} AND status = 'FINISHED' ORDER BY id DESC")
    List<Conversation> findGroupMsgByCounselor(String counselor);

    @Select("SELECT max_consult FROM counselor WHERE name = #{counselor}")
    Integer getMaxConsult(@Param("counselor") String counselor);

    @Select("SELECT count(*) FROM conversation WHERE counselor = #{counselor} AND status = 'STARTED'")
    Integer getConsultNum(@Param("counselor") String counselor);

    @Select("SELECT * FROM conversation WHERE id = #{id}")
    Conversation findById(@Param("id") Long id);

    @Insert("INSERT INTO conversation " +
            "(id, create_time, creator, last_update_time, last_updater, " +
            "year, month, day, start_time, end_time, user, counselor, status, " +
            "visitor_name, evaluate, conversation_type) VALUES " +
            "(#{conversation.id}, #{conversation.createTime}, #{conversation.creator}, #{conversation.lastUpdateTime}, #{conversation.lastUpdater}, " +
            "#{conversation.year}, #{conversation.month}, #{conversation.day}, #{conversation.startTime}, #{conversation.endTime}, #{conversation.user}, #{conversation.counselor}, #{conversation.status}, " +
            "#{conversation.visitorName}, #{conversation.evaluate}, #{conversation.conversationType})")
    void insertConversationByID(@Param("conversation") Conversation conversation);

    @Select("SELECT * FROM conversation ORDER BY id DESC LIMIT 1")
    Conversation getLastConversation();

    @Insert("INSERT INTO conversation (create_time, creator, last_update_time, last_updater, year, month, day, start_time, end_time, user, counselor, status, visitor_name, evaluate, conversation_type, message) " +
            "VALUES (#{conversation.createTime}, #{conversation.creator}, #{conversation.lastUpdateTime}, #{conversation.lastUpdater}, #{conversation.year}, #{conversation.month}, #{conversation.day}, #{conversation.startTime}, #{conversation.endTime}, #{conversation.user}, #{conversation.counselor}, #{conversation.status}, #{conversation.visitorName}, #{conversation.evaluate}, #{conversation.conversationType}, #{conversation.message})")
    @Options(useGeneratedKeys = true, keyProperty = "conversation.id")
    void insertConversation(@Param("conversation") Conversation conversation);

    @Update("UPDATE conversation SET " +
            "create_time = #{conversation.createTime}, " +
            "creator = #{conversation.creator}, " +
            "last_update_time = #{conversation.lastUpdateTime}, " +
            "last_updater = #{conversation.lastUpdater}, " +
            "year = #{conversation.year}, " +
            "month = #{conversation.month}, " +
            "day = #{conversation.day}, " +
            "start_time = #{conversation.startTime}, " +
            "end_time = #{conversation.endTime}, " +
            "user = #{conversation.user}, " +
            "counselor = #{conversation.counselor}, " +
            "status = #{conversation.status}, " +
            "visitor_name = #{conversation.visitorName}, " +
            "evaluate = #{conversation.evaluate}, " +
            "conversation_type = #{conversation.conversationType}" +
            "WHERE id = #{conversation.id}")
    void updateConversation(@Param("conversation") Conversation conversation);

    @Select("SELECT * FROM conversation WHERE user = #{user}")
    List<Conversation> findByUser(@Param("user") Long user);

    @Select("SELECT * FROM conversation WHERE counselor = #{counselor}")
    List<Conversation> findByCounselor(@Param("counselor") Long counselor);

    @Select("SELECT * FROM conversation WHERE " +
            "counselor = #{counselor} AND " +
            "user = #{user} AND " +
            "DATE(start_time) = #{date}")
    List<Conversation> findByCounselorUserDate(@Param("counselor") Long counselor,
                                               @Param("user") Long user,
                                               @Param("date") Date date);

    @Select("SELECT SUM( ( UNIX_TIMESTAMP(end_time) - UNIX_TIMESTAMP(start_time) ) / 60 ) " +
            "FROM conversation " +
            "WHERE counselor= #{counselor}")
    Integer findTotalByCounselor(@Param("counselor") Long counselor);

    @Select("SELECT COUNT(*) " +
            "FROM conversation " +
            "WHERE counselor= #{counselor} AND " +
            "DATE(start_time) = DATE(SYSDATE())")
    Integer findTodayNumByCounselor(@Param("counselor") Long counselor);

    @Select("SELECT SUM( ( UNIX_TIMESTAMP(end_time) - UNIX_TIMESTAMP(start_time) ) / 60 ) " +
            "FROM conversation " +
            "WHERE counselor= #{counselor} AND " +
            "DATE(start_time) = DATE(SYSDATE())")
    Integer findTodayTotalByCounselor(@Param("counselor") Long counselor);

    @Select("SELECT COUNT(*) " +
            "FROM conversation " +
            "WHERE counselor= #{counselor} AND " +
            "status = 'active'")
    Integer findCurrentNumByCounselor(@Param("counselor") Long counselor);

}
