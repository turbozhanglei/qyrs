package com.guoye.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class WxConfig {
    @Value("${app_id}")
    private  String appid;
    @Value("${secret_id}")
    private  String secret;
}
