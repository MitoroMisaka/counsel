package com.ecnu.rai.counsel.service.impl;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.entity.Conversation;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.mapper.ConversationMapper;
import com.ecnu.rai.counsel.response.ConsultInfo;
import com.ecnu.rai.counsel.service.ConversationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationMapper conversationMapper;

    @Override
    public Conversation findConversationByID(Long id) {
        return conversationMapper.findById(id);
    }

    public Conversation insertConversationByID(Conversation conversation) {
        conversationMapper.insertConversationByID(conversation);
        return conversationMapper.getLastConversation();
    }

    @Override
    public Conversation updateConversation(Conversation conversation) {
        Conversation existingConversation = conversationMapper.findById(conversation.getId());
        if (existingConversation == null) {
            throw new RuntimeException("Conversation not found with ID: " + conversation.getId());
        }

        // Update the supervisor properties
        existingConversation.setCreateTime(conversation.getCreateTime());
        existingConversation.setCreator(conversation.getCreator());
        existingConversation.setLastUpdateTime(LocalDateTime.now());
        existingConversation.setLastUpdater(conversation.getLastUpdater());
        existingConversation.setYear(conversation.getYear());
        existingConversation.setMonth(conversation.getMonth());
        existingConversation.setDay(conversation.getDay());
        existingConversation.setStartTime(conversation.getStartTime());
        existingConversation.setEndTime(conversation.getEndTime());
        existingConversation.setUser(conversation.getUser());
        existingConversation.setCounselor(conversation.getCounselor());
        existingConversation.setVisitorName(conversation.getVisitorName());
        existingConversation.setEvaluate(conversation.getEvaluate());
        existingConversation.setConversationType(conversation.getConversationType());
        existingConversation.setMessage(conversation.getMessage());

        // Perform the update in the database
        conversationMapper.updateConversation(existingConversation);

        return existingConversation;
    }

    @Override
    public Page<Conversation> findConversationByUser(Long user, Integer page, Integer size, String order) {
        // 构造分页参数
        PageHelper.startPage(page, size, order);

        List<Conversation> conversationList = conversationMapper.findByUser(user);

        // 返回分页后的结果
        return new Page<>(new PageInfo<>(conversationList));
    }

    @Override
    public Page<Conversation> findConversationByCounselor(Long counselor, Integer page, Integer size, String order) {
        // 构造分页参数
        PageHelper.startPage(page, size, order);

        List<Conversation> conversationList = conversationMapper.findByCounselor(counselor);

        // 返回分页后的结果
        return new Page<>(new PageInfo<>(conversationList));
    }

    @Override
    public Page<Conversation> findConversationByCounselorUserDate(Long counselor, Long user, Date date, Integer page, Integer size, String order) {
        // 构造分页参数
        PageHelper.startPage(page, size, order);

        List<Conversation> conversationList = conversationMapper.findByCounselorUserDate(counselor, user, date);

        // 返回分页后的结果
        return new Page<>(new PageInfo<>(conversationList));
    }

    @Override
    public ConsultInfo findConsultInfobyCounselor(Long counselor) {
        ConsultInfo consultInfo = new ConsultInfo();
        consultInfo.setTotal(conversationMapper.findTotalByCounselor(counselor));
        consultInfo.setTodayNum(conversationMapper.findTodayNumByCounselor(counselor));
        consultInfo.setTodayTotal(conversationMapper.findTodayTotalByCounselor(counselor));
        consultInfo.setCurrentNum(conversationMapper.findCurrentNumByCounselor(counselor));
        return consultInfo;
    }


}
