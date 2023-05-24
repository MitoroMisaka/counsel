package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.entity.Conversation;

import java.util.List;

public interface ConversationService {

    Conversation findConversationByID(Long id);

    Conversation updateConversation(Conversation conversation);

    Page<Conversation> findConversationByUser(Long user, Integer page, Integer size, String order);

    public Page<Conversation> findConversationByCounselor(Long counselor, Integer page, Integer size, String order);

}
