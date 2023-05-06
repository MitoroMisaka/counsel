package com.ecnu.rai.counsel.common;

import lombok.Getter;

/**
 * @author phoenix
 * @version 2022/1/19 19:21
 */
@Getter
public class CommonException extends RuntimeException {

    private CommonErrorCode commonErrorCode;

    private Object errorMsg;

    public CommonException() {
    }

    public CommonException(CommonErrorCode commonErrorCode) {
        this.commonErrorCode = commonErrorCode;
    }

    public CommonException(CommonErrorCode commonErrorCode, Object errorMsg) {
        this.commonErrorCode = commonErrorCode;
        this.errorMsg = errorMsg;
    }
}
