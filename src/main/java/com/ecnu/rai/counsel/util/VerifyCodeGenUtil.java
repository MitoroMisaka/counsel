package com.ecnu.rai.counsel.util;

public class VerifyCodeGenUtil {
    public static String generateCode(int count) {

        return String.valueOf((int)((Math.random()*9+1)* Math.pow(10,count-1)));
    }

}
