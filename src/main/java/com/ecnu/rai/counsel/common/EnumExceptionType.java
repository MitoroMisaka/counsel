package com.ecnu.rai.counsel.common;

public enum EnumExceptionType {
    SYSTEM_INTERNAL_ANOMALY(1000, "网络不给力，请稍后重试。"),
    PASSWORD_SAME(1001, "新密码与旧密码相同"),
    USER_ALREADY_EXIST(1002,"用户名重复"),
    LOGIN_INVALID(1003,"登录状态失效，请重新登录"),
    CAR_BEING_USED(1004,"该车辆正在运输"),
    DRIVER_BEING_USED(1005,"该司机正在运输");




    private int errorCode;

    private String codeMessage;

    EnumExceptionType(int errorCode, String codeMessage) {
        this.errorCode = errorCode;
        this.codeMessage = codeMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getCodeMessage() {
        return codeMessage;
    }
}
