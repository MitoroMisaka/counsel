package com.ecnu.rai.counsel.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.mapper.UserMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyRealm extends AuthorizingRealm {

    @Autowired
    UserMapper userMapper;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        Object principal = principals.getPrimaryPrincipal();
        User user = (User) principal;

        String username = user.getUsername();
        String role = user.getRole();

        //注入角色与权限
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //管理员
        if(role == "admin"){
            info.addRole("admin");
            info.addRole("online");
            info.addRole("insert");
            info.addRole("update");
            info.addRole("delete");
            info.addRole("select");
        }

        //普通用户
        if(role == "visitor") {
            info.addRole("visitor");
            info.addRole("online");
        }
        System.out.println(principal);
        System.out.println(info.getRoles());
        System.out.println("++++++++++++++++++++++++++++++++++++++++");

        return info;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        //数据库匹配，认证
        String username = token.getUsername();
        String password = new String(token.getPassword());

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username",username);
        User user = userMapper.selectOne(wrapper);

        if (user != null && (user.getPassword() + "").equals(password)) {
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, token.getCredentials(), getName());
            return info;
        }

        //认证失败
        throw new AuthenticationException();
    }
}
