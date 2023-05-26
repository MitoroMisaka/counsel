package com.ecnu.rai.counsel.service.impl;

import com.ecnu.rai.counsel.config.UserSigConfig;
import com.ecnu.rai.counsel.service.UserSigService;
import com.ecnu.rai.counsel.util.TLSSigAPIv2;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSigServiceImpl implements UserSigService {

    @Autowired
    UserSigConfig userSigConfig;

    @Override
    public String generateUserSig(String userid) {
        TLSSigAPIv2 api = new TLSSigAPIv2(userSigConfig.getSdkAppId(), userSigConfig.getSecretKey());

        return api.genSig(userid, userSigConfig.getExpire());
    }
}
