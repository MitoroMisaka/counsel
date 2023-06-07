package com.ecnu.rai.counsel.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ecnu.rai.counsel.entity.Visitor;
import com.ecnu.rai.counsel.mapper.VisitorMapper;
import java.util.Date;

public class TokenUtil {

    public static final String secret = "sdjhakdhajdklsl;o653632";


    public static  boolean token_check(String token) {
        if(token == null) {
            System.out.println("Token is null");
            return false;
        }
        try {
            DecodedJWT jwt = JWT.decode(token);
            Date expirationTime = jwt.getExpiresAt();
            System.out.println(expirationTime);
            Date currentTime = new Date();
            System.out.println(currentTime);
            if (expirationTime.before(currentTime)) {
                System.out.println("Token overtime");
                return false;
            }
        } catch (Exception e) {
            // Error decoding or verifying the token
            System.out.println("Error decoding/verifying the token: " + e.getMessage());
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            System.out.println("Signature verification successful");
            return true;
        } catch (JWTVerificationException e) {
            // Invalid signature
            System.out.println("Signature verification failed: " + e.getMessage());
            return false;
        }


    }
}
