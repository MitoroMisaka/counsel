package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.entity.Conversation;
import com.ecnu.rai.counsel.response.ConsultInfo;

import java.sql.Date;
import java.util.List;

public interface ConversationService {

    Conversation findConversationByID(Long id);

    Conversation insertConversationByID(Conversation conversation);

    Conversation updateConversation(Conversation conversation);

    Page<Conversation> findConversationByUser(Long user, Integer page, Integer size, String order);

    Page<Conversation> findConversationByCounselor(Long counselor, Integer page, Integer size, String order);

    Page<Conversation> findConversationByCounselorUserDate(Long counselor, Long user, Date date, Integer page, Integer size, String order);

    ConsultInfo findConsultInfobyCounselor(Long counselor);

}
