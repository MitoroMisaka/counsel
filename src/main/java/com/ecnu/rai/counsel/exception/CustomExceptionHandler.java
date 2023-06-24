package com.ecnu.rai.counsel.exception;

import com.ecnu.rai.counsel.common.Result;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.function.Supplier;


@ControllerAdvice
public class CustomExceptionHandler {

//    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleException(Exception ex) {
//        // 获取异常信息
//        String errorMessage = ex.getMessage();
////        logger.error((Supplier<String>) errorMessage, ex);
//
//        // 创建一个包含异常信息的 JSON 响应
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.fail(errorMessage));
//    }
}

