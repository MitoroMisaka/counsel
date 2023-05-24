package com.ecnu.rai.counsel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.rai.counsel.entity.Arrange;
import com.ecnu.rai.counsel.entity.Conversation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationMapper extends BaseMapper<Conversation> {

    @Select("SELECT * FROM conversation WHERE id = #{id}")
    Conversation findById(@Param("id") Long id);

    @Update("UPDATE conversation SET create_time = #{conversation.createTime} , creator = #{conversation.creator} , last_update_time = #{conversation.lastUpdate_time} , " +
            "last_updater = #{conversation.lastUpdater} , year = #{conversation.year} , month = #{conversation.month} , day = #{conversation.day} , " +
            "start_time = #{conversation.startTime} , end_time = #{conversation.endTime}  , user = #{conversation.user} , counselor = #{conversation.counselor} , " +
            "status = #{conversation.status} , visitor_name = #{conversation.visitorName} , evaluate = #{conversation.evaluate} , conversation_type = #{conversation.conversationType}" +
            "WHERE id = #{conversation.id}")
    void updateConversation(@Param("conversation") Conversation conversation);

    @Select("SELECT * FROM conversation WHERE user = #{user}")
    List<Conversation> findByUser(@Param("user") Long user);

    @Select("SELECT * FROM conversation WHERE counselor = #{counselor}")
    List<Conversation> findByCounselor(@Param("counselor") Long counselor);

}
