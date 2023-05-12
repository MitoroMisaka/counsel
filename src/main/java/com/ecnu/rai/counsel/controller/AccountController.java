package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/users")
    public List<User> findAllUsers() {
        return accountService.findAllUsers();
    }
}
