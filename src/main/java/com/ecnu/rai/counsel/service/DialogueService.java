package com.ecnu.rai.counsel.service;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.dao.ConversationResponse;
import com.ecnu.rai.counsel.entity.Conversation;
import com.ecnu.rai.counsel.entity.Dialogue;
import com.ecnu.rai.counsel.response.ConsultInfo;

import java.sql.Date;

public interface DialogueService {
    //Page<ConversationResponse> findGroupMsgByCounselorUser(String counselor, String user, Integer page, Integer size, String order);

    Dialogue findDialogueByID(Long id);

    Dialogue insertDialogueByID(Dialogue dialogue);

    Dialogue updateDialogue(Dialogue dialogue);

    Page<Dialogue> findDialogueBySupervisor(Long supervisor, Integer page, Integer size, String order);

    Page<Dialogue> findDialogueByCounselor(Long counselor, Integer page, Integer size, String order);

    ConsultInfo findConsultInfobySupervisor(Long supervisor);

    Page<Dialogue> findDialogueByBothDate(Long counselor, Long supervisor, Date date, Integer page, Integer size, String order);
}
