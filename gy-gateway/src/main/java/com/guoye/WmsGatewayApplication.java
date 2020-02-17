package com.guoye;

import com.guoye.filter.MyEurekaClientFallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class WmsGatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(WmsGatewayApplication.class, args);
	}

	@Bean
	public MyEurekaClientFallback eurekaClientFallback() {
		return new MyEurekaClientFallback();
	}


}
