package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.dao.UserSig;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.entity.Usersig;
import com.ecnu.rai.counsel.mapper.UserMapper;
import com.ecnu.rai.counsel.mapper.UserSigMapper;
import com.ecnu.rai.counsel.service.UserSigService;
import io.swagger.annotations.ApiOperation;
import lombok.Value;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserSigController {
    @Autowired
    UserSigService userSigService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserSigMapper userSigMapper;

    @GetMapping("/userSig")
    public Object getUserSig(@RequestParam("userid") String userid, @RequestParam("name") String name) {
        String userSig = userSigService.generateUserSig(userid);
        User user = userMapper.findByName(name);
        Usersig usersig1 = new Usersig(userid, userSig, name, user.getRole());
        userSigMapper.insertUserSig(usersig1);
        //User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        //if (!currentUser.getRole().equals("admin")) {
        //        return Result.fail("You have no permission to generate userSig");
        //}
        //返回一个Object，里面第一条是userid，第二条是token
        return new UserSig(userid, userSig);
    }
}
