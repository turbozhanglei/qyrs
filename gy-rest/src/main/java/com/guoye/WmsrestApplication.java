package com.guoye;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableScheduling
@ComponentScan("com.guoye")
@EnableEurekaClient
@SpringBootApplication(exclude = MybatisAutoConfiguration.class)
public class WmsrestApplication {

    public static void main(String[] args) {
        SpringApplication.run(WmsrestApplication.class, args);
    }




}
