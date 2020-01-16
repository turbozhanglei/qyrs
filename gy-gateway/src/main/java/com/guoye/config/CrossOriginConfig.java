/**
 * ssc
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.guoye.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author arvin
 * @version $Id: CorsConfiguration.java, v 0.1 2017年9月16日 下午3:33:34 arvin Exp $
 */
@Configuration
public class CrossOriginConfig {

    @Bean
    @Order(Integer.MAX_VALUE)
    public CorsFilter corsFilter() {

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        final CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // 允许cookies跨域

        config.addAllowedOrigin("*");// #允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin

        config.addAllowedHeader("*");// #允许访问的头信息,*表示全部

        config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了

        config.addAllowedMethod("*");// 允许提交请求的方法，*表示全部允许

        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
