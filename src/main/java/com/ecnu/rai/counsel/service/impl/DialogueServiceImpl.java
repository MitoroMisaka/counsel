package com.ecnu.rai.counsel.service.impl;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.entity.Dialogue;
import com.ecnu.rai.counsel.mapper.DialogueMapper;
import com.ecnu.rai.counsel.mapper.UserMapper;
import com.ecnu.rai.counsel.response.ConsultInfo;
import com.ecnu.rai.counsel.service.DialogueService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DialogueServiceImpl implements DialogueService {
    @Autowired
    private DialogueMapper dialogueMapper;

    @Autowired
    private UserMapper userMapper;

//    @Override
//    public Page<dialogueResponse> findGroupMsgByCounselorUser(String counselor, String user, Integer page, Integer size, String order){
//        PageHelper.startPage(page, size, order);
//        String counselor_id = String.valueOf(userMapper.findIdByName(counselor));
//        String user_id = String.valueOf(userMapper.findIdByName(user));
//        List<dialogue> dialogueList = dialogueMapper.findGroupMsgByCounselorUser(counselor_id, user_id);
//        List<dialogueResponse> dialogueResponseList = new ArrayList<>();
//        for(dialogue dialogue : dialogueList){
//            dialogueResponse dialogueResponse = new dialogueResponse(dialogue);
//            dialogueResponseList.add(dialogueResponse);
//        }
//        PageInfo<dialogueResponse> pageInfo = new PageInfo<>(dialogueResponseList);
//        return new Page<>(pageInfo);
//    }

    @Override
    public Dialogue findDialogueByID(Long id) {
        return dialogueMapper.findById(id);
    }

    @Override
    public Dialogue insertDialogueByID(Dialogue dialogue) {
        dialogueMapper.insertDialogueByID(dialogue);
        return dialogueMapper.getLastDialogue();
    }

    @Override
    public Dialogue updateDialogue(Dialogue dialogue) {
        Dialogue existingdialogue = dialogueMapper.findById(dialogue.getId());
        if (existingdialogue == null) {
            throw new RuntimeException("dialogue not found with ID: " + dialogue.getId());
        }

        // Update the supervisor properties
        existingdialogue.setCreateTime(dialogue.getCreateTime());
        existingdialogue.setCreator(dialogue.getCreator());
        existingdialogue.setLastUpdateTime(LocalDateTime.now());
        existingdialogue.setLastUpdater(dialogue.getLastUpdater());
        existingdialogue.setYear(dialogue.getYear());
        existingdialogue.setMonth(dialogue.getMonth());
        existingdialogue.setDay(dialogue.getDay());
        existingdialogue.setStartTime(dialogue.getStartTime());
        existingdialogue.setEndTime(dialogue.getEndTime());
        existingdialogue.setSupervisor(dialogue.getSupervisor());
        existingdialogue.setCounselor(dialogue.getCounselor());
        existingdialogue.setVisitorName(dialogue.getVisitorName());
        existingdialogue.setMessage(dialogue.getMessage());

        // Perform the update in the database
        dialogueMapper.updateDialogue(existingdialogue);

        return existingdialogue;
    }

    @Override
    public Page<Dialogue> findDialogueBySupervisor(Long supervisor, Integer page, Integer size, String order) {

        List<Dialogue> dialogueList = dialogueMapper.findBySupervisor(supervisor);

        for(Dialogue dialogue : dialogueList){
            dialogue.setCounselor(userMapper.findNameById(dialogue.getCounselor()));
            dialogue.setSupervisor(userMapper.findNameById(dialogue.getSupervisor()));
        }

        // 返回分页后的结果
        return new Page(dialogueList, page, size);
    }

    @Override
    public Page<Dialogue> findDialogueByCounselor(Long counselor, Integer page, Integer size, String order) {

        List<Dialogue> dialogueList = dialogueMapper.findByCounselor(counselor);
        for(Dialogue dialogue : dialogueList){
            dialogue.setCounselor(userMapper.findNameById(dialogue.getCounselor()));
            dialogue.setSupervisor(userMapper.findNameById(dialogue.getSupervisor()));
        }
        // 返回分页后的结果
        return new Page(dialogueList, page, size);
    }

    @Override
    public Page<Dialogue> findDialogueByBothDate(Long counselor, Long supervisor, Date date, Integer page, Integer size, String order) {
        // 构造分页参数
        PageHelper.startPage(page, size, order);
        List<Dialogue> dialogueList = dialogueMapper.findByBothDate(counselor, supervisor, date);

        // 返回分页后的结果
        return new Page<>(new PageInfo<>(dialogueList));
    }

    @Override
    public ConsultInfo findConsultInfobySupervisor(Long supervisor) {
        ConsultInfo consultInfo = new ConsultInfo();
        consultInfo.setTotal(dialogueMapper.findTotalBySupervisor(supervisor));
        consultInfo.setTodayNum(dialogueMapper.findTodayNumBySupervisor(supervisor));
        consultInfo.setTodayTotal(dialogueMapper.findTodayTotalBySupervisor(supervisor));
        consultInfo.setCurrentNum(dialogueMapper.findCurrentNumBySupervisor(supervisor));
        return consultInfo;
    }

}
