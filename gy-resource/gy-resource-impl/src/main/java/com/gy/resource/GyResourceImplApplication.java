package com.gy.resource;

import com.jic.common.swagger.config.Swagger2Config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author: gaolanyu
 * @date: 2020-02-13
 * @remark:
 */
@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
@MapperScan("com.gy.resource.mapper")
@EnableScheduling
public class GyResourceImplApplication {
    public static void main(String[] args) {
        SpringApplication.run(GyResourceImplApplication.class, args);
    }

    @Bean
    @ConditionalOnMissingBean
    public Docket getDocket() {
        return new Swagger2Config().createRestApi(this.getClass().getSimpleName());
    }

}
