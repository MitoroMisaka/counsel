package com.ecnu.rai.counsel.common;

public enum EnumExceptionType {
    SYSTEM_INTERNAL_ANOMALY(-1, "网络不给力，请稍后重试。"),
    PASSWORD_SAME(-2, "新密码与旧密码相同"),
    USER_ALREADY_EXIST(-3,"用户名重复"),
    LOGIN_INVALID(-4,"登录状态失效，请重新登录");




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
