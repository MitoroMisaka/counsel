package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.common.Page;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/users")
    public Page<User> findAllUsers(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return accountService.findAllUsers(page, size);
    }
}
