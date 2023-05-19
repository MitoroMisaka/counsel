package com.ecnu.rai.counsel.util;

import com.auth0.jwt.algorithms.Algorithm;
import com.ecnu.rai.counsel.entity.WXUser;
import com.auth0.jwt.JWT;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenGenUtil {
    //密钥
    public static final String SECRET = "sdjhakdhajdklsl;o653632";
    //过期时间:秒
    public static final int EXPIRE = 5;
    public static String TokenGen(WXUser u){
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.SECOND, EXPIRE);
        Date expireDate = nowTime.getTime();
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token ="";
        token = JWT.create()
                .withHeader(map)//头
                .withIssuedAt(new Date())//签名时间
                .withExpiresAt(expireDate)//过期时间
                .sign(Algorithm.HMAC256(u.getOpenid()+u.getSession_key()));//签名
        return token;


    };
}
