package com.ecnu.rai.counsel.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.dao.*;
import com.ecnu.rai.counsel.entity.Counselor;
import com.ecnu.rai.counsel.entity.Supervise;
import com.ecnu.rai.counsel.entity.Supervisor;
import com.ecnu.rai.counsel.entity.*;
import com.ecnu.rai.counsel.mapper.*;
import com.ecnu.rai.counsel.service.CounselorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

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

    @Autowired
    private DialogueMapper dialogueMapper;

    @Autowired
    private SuperviseMapper superviseMapper;

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
    public
    List<CounselorBasicInfo> getFreeCounselorList() {
        List<Long> freeCounselorIdList = arrangeMapper.findFreeCounselor();
        List<CounselorBasicInfo> counselorList = new ArrayList<>();
        for (Long freeCounselorId : freeCounselorIdList) {
            CounselorBasicInfo counselor = new CounselorBasicInfo(counselorMapper.findById(freeCounselorId));
            counselorList.add(counselor);
        }
        return counselorList;
    }

    @Override
    public
    List<CounselorBasicInfo> getWorkingCounselorList() {
        List<Long> workingCounselorIdList = arrangeMapper.findWorkingCounselor();
        List<CounselorBasicInfo> counselorList = new ArrayList<>();
        for (Long freeCounselorId : workingCounselorIdList) {
            CounselorBasicInfo counselor = new CounselorBasicInfo(counselorMapper.findById(freeCounselorId));
            counselorList.add(counselor);
        }
        return counselorList;
    }

    @Override
    public List<CounselorMonthlyWork> getCounselorRankingByWork(Integer len) {
        List<CounselorMonthlyWork> list = counselorMapper.findCounselorRankingByWork(len);
        for (CounselorMonthlyWork item : list) {
            item.setName(counselorMapper.findById(item.getCounselorId()).getName());
            item.setAvatar(counselorMapper.findById(item.getCounselorId()).getAvatar());
            item.setUsername(counselorMapper.findById(item.getCounselorId()).getUsername());
        }
        return list;
    }

    @Override
    public List<CounselorMonthlyStar> getCounselorRankingByStar(Integer len) {
        List<CounselorMonthlyStar> list = counselorMapper.findCounselorRankingByComments(len);
        for (CounselorMonthlyStar item : list) {
            item.setName(counselorMapper.findById(item.getCounselorId()).getName());
            item.setAvatar(counselorMapper.findById(item.getCounselorId()).getAvatar());
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
        List<Long> availableCounselorIdList = arrangeMapper.findCounselorByCurrentTime(order);
        List<AvailableCounselor> counselorList = new ArrayList<>();

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
            List<Conversation> conversations = conversationMapper.findGroupMsgByCounselorUser(counselor_id, user_id, "id asc");
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
        return new Page(counselorList, page, size);
    }

    @Override
    public Page<CounselorSMInfo> getAllCounselor(Integer page, Integer size, String order) {
        List<Counselor> counselors = counselorMapper.findAllCounselors(order);
        List<CounselorSMInfo> counselorSMInfos = new ArrayList<>();
        for(Counselor counselor:counselors)
        {
            CounselorSMInfo counselorSMInfo = new CounselorSMInfo(counselor);
            counselorSMInfo.SetTotalNum(conversationMapper.findTotalNumCounselor(counselor.getId()));
            counselorSMInfo.SetTotalTime(conversationMapper.findTotalByCounselor(counselor.getId()));
            counselorSMInfo.setState(userMapper.findStateById(counselor.getId()));
            List<Long> bindIds = superviseMapper.findSupervisorByCounselorId(counselor.getId());
            List<String> bindNames = new ArrayList<>();
            for(Long id:bindIds)
            {
                bindNames.add(supervisorMapper.findById(id).getName());
            }
            counselorSMInfo.setSupervisors(bindNames);
            List<Integer> Days = arrangeMapper.findDayArrange(counselor.getId());
            LocalDate today = LocalDate.now();
            LocalDate monday = today;
            if (today.getDayOfWeek() != DayOfWeek.MONDAY) {
                monday = today.with(DayOfWeek.MONDAY);
            }
            for(Integer day:Days)
            {
                counselorSMInfo.setTotalDay(day);
            }
            counselorSMInfos.add(counselorSMInfo);
        }
        return new Page(counselorSMInfos, page, size);
    }



    @Override
    public Page<HashMap<String, String>> getAvailableCounselorByBusy(Integer page, Integer size, String order) {
        List<HashMap<String, String>> CounselorBusy= new ArrayList<>();

        List<Counselor> counselors = counselorMapper.findAllCounselorsOnline(order);

        System.out.println(counselors);
        for(Counselor counselor:counselors)
        {
            Integer currentConsult = conversationMapper.getConsultNum(counselor.getName());
            if(currentConsult <= 5){
                HashMap<String, String> h = new HashMap<>();
                h.put("counselor",counselor.getName());
                h.put("status","空闲");
                CounselorBusy.add(h);
            }else if(currentConsult < counselor.getMaxConsult()) {
                HashMap<String, String> h = new HashMap<>();
                h.put("counselor",counselor.getName());
                h.put("status","繁忙");
                CounselorBusy.add(h);
            }
        }
        return new Page(CounselorBusy, page, size);
    }

    @Override
    public HashMap<String, Integer> getBasicStatInfoByCounselor(Long counselorId) {
        Integer totalNumCounselor = conversationMapper.findTodayNumByCounselor(counselorId);
        Integer totalTime = conversationMapper.findTodayTotalByCounselor(counselorId);
        Integer curNum = conversationMapper.findCurrentNumByCounselor(counselorId);
        Integer totalNumSupervisor = dialogueMapper.findTodayNumByCounselor(counselorId);
        totalNumCounselor = totalNumCounselor + curNum;
        HashMap<String, Integer> h = new HashMap<>();
        h.put("totalNumCounselor",totalNumCounselor);
        h.put("totalTime",totalTime);
        h.put("curNum",curNum);
        h.put("totalNumSupervisor",totalNumSupervisor);
        return h;

    }

    @Override
    public List<TreeMap<String, Integer>> getNumByWeek() {
        List<TreeMap<String, Integer>> NumByWeek = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for(int i = 0;i<7;i++)
        {
            LocalDate previousDate = today.minusDays(6-i );
            Integer month = previousDate.getMonthValue();
            Integer day = previousDate.getDayOfMonth();
            Integer sum = conversationMapper.findNumDaysAgo(6-i );
            TreeMap<String, Integer> h = new TreeMap<>();
            h.put("month",month);
            h.put("day",day);
            h.put("sum",sum);
            NumByWeek.add(h);
        }
        return NumByWeek;

    }

    @Override
    public List<TreeMap<String, Integer>> getNumByHours() {
        List<TreeMap<String, Integer>> NumByHour = new ArrayList<>();
        for(int i = 0;i <= 12; i++)
        {
            Integer sum = 0;
            if(i==0)
            {
                 sum = conversationMapper.findNumByHours(22,1);
            }
            else{
                 sum = conversationMapper.findNumByHours(i*2,0);
            }
            TreeMap<String, Integer> h = new TreeMap<>();
            h.put("hour",i*2);
            h.put("sum",sum);
            NumByHour.add(h);
        }
        return NumByHour;
    }


}
