package com.ecnu.rai.counsel.exception;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class CustomExceptionHandler implements HandlerExceptionResolver {

    @ExceptionHandler({AuthorizationException.class, UnauthenticatedException.class})
    public ModelAndView handleAuthorizationException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        // 构造自定义的错误信息
        String errorMessage;
        if (ex instanceof AuthorizationException) {
            errorMessage = "您没有访问该接口的权限";
        } else {
            errorMessage = "请先进行身份验证";
        }

        // 返回自定义的错误信息
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", errorMessage);
        modelAndView.setStatus(HttpStatus.FORBIDDEN);
        modelAndView.setViewName("error"); // 设置错误页面的视图名称
        return modelAndView;
    }

    @ExceptionHandler(AuthorizationException.class)
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        return null;
    }
}
