package com.ecnu.rai.counsel.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.entity.Visitor;
import com.ecnu.rai.counsel.mapper.UserMapper;
import com.ecnu.rai.counsel.response.GetUserResponse;
import com.ecnu.rai.counsel.service.AccountService;
import com.ecnu.rai.counsel.util.PasswordUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码(长度6-20)", required = true, paramType = "query", dataType = "String")
    })
    public Result Login(@RequestParam("username")@NotNull String username,
                        @RequestParam("password")@NotNull @Size(min = 6,max = 20)String password){

        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password) ) {
            return Result.fail("用户名,密码和姓名不能为空！");
        }

        // AuthenticationToken token = new UsernamePasswordToken(username, PasswordUtil.convert(password));
        AuthenticationToken token = new UsernamePasswordToken(username, password);

        try {

            //尝试登陆，将会调用realm的认证方法
            SecurityUtils.getSubject().login(token);

        }catch (AuthenticationException e) {
            if (e instanceof UnknownAccountException) {
                return Result.fail("用户不存在");
            } else if (e instanceof LockedAccountException) {
                return Result.fail("用户被禁用");
            } else if (e instanceof IncorrectCredentialsException) {
                return Result.fail("密码错误");
            } else {
                return Result.fail("用户认证失败");
            }
        }

        User principal = (User) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole(principal.getRole());
        System.out.println(principal);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username",username);

        return Result.success("登录成功",new GetUserResponse(userMapper.selectOne(wrapper), principal.getRole()));
    }

    @PostMapping("/logout")
    @ApiOperation("登出")
    public Result logout() throws IOException {
        SecurityUtils.getSubject().logout();
        return Result.success(null);
    }


    @RequiresUser
    @PostMapping("/hello")
    @ApiImplicitParam(name = "str", value = "参数", required = true, paramType = "query", dataType = "String")
    public Result Hello(@RequestParam("str")@NotNull String str) {
        return Result.success(null);
    }

    @GetMapping("/users")
    @ApiOperation("获取用户列表")
    public Result getUsers() {
        return Result.success("获取成功", userMapper.getUserList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody User user
    ) {
        User updatedUser = accountService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }
}
