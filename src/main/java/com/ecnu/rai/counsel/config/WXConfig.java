package com.ecnu.rai.counsel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Component
@Data
@ConfigurationProperties(prefix = "wx")
public class WXConfig {
 private String appId;
 private String appSecret;
}
