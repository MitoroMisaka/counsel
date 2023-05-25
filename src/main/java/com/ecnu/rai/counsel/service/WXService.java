package com.ecnu.rai.counsel.service;
import com.ecnu.rai.counsel.entity.Visitor;

public interface WXService {
    void insertNewVisitor(String openid);
    boolean visitorExist(String openid);
    Visitor findByopenid(String openid);
    Long findIdByopenid(String openid);
}
