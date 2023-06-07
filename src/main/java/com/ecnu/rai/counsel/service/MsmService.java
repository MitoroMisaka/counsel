package com.ecnu.rai.counsel.service;

import org.springframework.stereotype.Service;

@Service
public interface MsmService  {

    public boolean send(String phone, String code);

}
