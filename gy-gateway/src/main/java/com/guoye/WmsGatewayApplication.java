package com.guoye;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.guoye.config.SwaggerConfig;
import com.guoye.filter.MyEurekaClientFallback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@EnableSwaggerBootstrapUI
@EnableSwagger2
public class WmsGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(WmsGatewayApplication.class, args);
    }

    @Bean
    public MyEurekaClientFallback eurekaClientFallback() {
        return new MyEurekaClientFallback();
    }

    @Bean
    @ConditionalOnMissingBean
    public Docket getDocket() {
        return new SwaggerConfig().groupRestApi();
    }
}
