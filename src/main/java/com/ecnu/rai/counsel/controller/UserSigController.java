package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.dao.UserSig;
import com.ecnu.rai.counsel.service.UserSigService;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserSigController {
    @Autowired
    UserSigService userSigService;

    @GetMapping("/userSig")
    public Object getUserSig(@RequestParam("userid") String userid) {
        //返回一个Object，里面第一条是userid，第二条是token
        return new UserSig(userid, userSigService.generateUserSig(userid));
    }
}
