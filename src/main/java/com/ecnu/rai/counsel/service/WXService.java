package com.ecnu.rai.counsel.service;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.entity.Visitor;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public interface WXService {
    void insertNewVisitor(String openid);
    boolean visitorExist(String openid);
    Visitor findByopenid(String openid);
    Long findIdByopenid(String openid);
    boolean visitorState(String openid);
    User finduserByopenid(String openid);
//    List<HashMap<String,Object>> findAvaliableCounselorInfo(LocalDateTime localDateTime);
}
