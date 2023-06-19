package com.ecnu.rai.counsel.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.dao.CounselorMonthlyStar;
import com.ecnu.rai.counsel.dao.CounselorMonthlyWork;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervise;
import com.ecnu.rai.counsel.entity.Supervisor;
import com.ecnu.rai.counsel.dao.AvailableCounselor;
import com.ecnu.rai.counsel.entity.*;
import com.ecnu.rai.counsel.mapper.*;
import com.ecnu.rai.counsel.service.CounselorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CounselorServiceImpl implements CounselorService {
    @Autowired
    private CounselorMapper counselorMapper;

    @Autowired
    private ConversationMapper conversationMapper;

    @Autowired
    private SupervisorMapper supervisorMapper;

    @Autowired
    private ArrangeMapper arrangeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VisitorMapper visitorMapper;

    @Autowired
    private UserSigMapper userSigMapper;

    @Override
    public Page<Counselor> getCounselorList(Integer page, Integer size, String order){
        PageHelper.startPage(page, size, order);
        List<Counselor> list = counselorMapper.findAll();
        PageInfo<Counselor> pageInfo = new PageInfo<>(list);
        return new Page<>(pageInfo);
    }

    @Override
    public Counselor findCounselorByID(Long id) {
        Counselor counselor = counselorMapper.findById(id);

        List<Supervise> superviselist = counselorMapper.findSupervisors(id);
        List<Supervisor> availablesupervisorList = new ArrayList<>();
        for (Supervise supervise : superviselist) {
            availablesupervisorList.add(supervisorMapper.findById(supervise.getSupervisorId()));
        }
        counselor.setSupervisors(availablesupervisorList);
        return counselor;
    }

    @Override
    public List<CounselorMonthlyWork> getCounselorRankingByWork(Integer len) {
        List<CounselorMonthlyWork> list = counselorMapper.findCounselorRankingByWork(len);
        for (CounselorMonthlyWork item : list) {
            item.setName(counselorMapper.findById(item.getCounselorId()).getName());
            item.setUsername(counselorMapper.findById(item.getCounselorId()).getUsername());
        }
        return list;
    }

    @Override
    public List<CounselorMonthlyStar> getCounselorRankingByStar(Integer len) {
        List<CounselorMonthlyStar> list = counselorMapper.findCounselorRankingByComments(len);
        for (CounselorMonthlyStar item : list) {
            item.setName(counselorMapper.findById(item.getCounselorId()).getName());
            item.setUsername(counselorMapper.findById(item.getCounselorId()).getUsername());
        }
        return list;
    }

    @Override
    public void updateCounselor(Counselor counselor) {
        counselorMapper.updateCounselor(counselor);
    }

    @Override
    public void addCounselor(Counselor counselor) {
        counselorMapper.insertCounselor(counselor);
    }

    @Override
    public Page<AvailableCounselor> getAvailableCounselor(Integer page, Integer size, String order, String token ) {
        List<Long> availableCounselorIdList = arrangeMapper.findCounselorByCurrentTime();
        List<AvailableCounselor> counselorList = new ArrayList<>();

        PageHelper.startPage(page, size, order);
        for (Long availableCounselorId : availableCounselorIdList) {
            Counselor counselor = counselorMapper.findById(availableCounselorId);
            AvailableCounselor availableCounselor = new AvailableCounselor(counselor);
            Integer currentConsult = conversationMapper.getConsultNum(counselor.getName());
            if(currentConsult <= 5){
                availableCounselor.setBusy("空闲");
            }else if(currentConsult < counselor.getMaxConsult()) {
                availableCounselor.setBusy("繁忙");
            }
            DecodedJWT jwt = JWT.decode(token);
            String openid = jwt.getClaim("openid").asString();
            Visitor visitor = visitorMapper.findByopenid(openid);
            User user = userMapper.findById(visitor.getId());
            String counselor_id = String.valueOf(userMapper.findIdByName(counselor.getName()));
            String user_id = String.valueOf(userMapper.findIdByName(user.getName()));
            List<Conversation> conversations = conversationMapper.findGroupMsgByCounselorUser(counselor_id, user_id);
            if(conversations.size() > 0){
                availableCounselor.setConsulted("已咨询");
            }else {
                availableCounselor.setConsulted("未咨询");
            }

            String imid = userSigMapper.getImidByName(counselor.getName());
            availableCounselor.setImid(imid);
            if(!currentConsult.equals(counselor.getMaxConsult())){
                counselorList.add(availableCounselor);
            }
        }
        return new Page<>(new PageInfo<>(counselorList));
    }



}
