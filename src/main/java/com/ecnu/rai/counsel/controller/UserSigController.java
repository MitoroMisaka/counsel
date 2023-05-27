package com.ecnu.rai.counsel.controller;

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
    public String getUserSig(@RequestParam("userid") String userid) {
        return userSigService.generateUserSig(userid);
    }
}
