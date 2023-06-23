package com.ecnu.rai.counsel.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@Data
@ConfigurationProperties(prefix = "im")
public class UserSigConfig {
    @Getter
    private Long sdkAppId;
    @Getter
    private String secretKey;
    @Getter
    private Long expire = (long) (60*60*24*365);
}
