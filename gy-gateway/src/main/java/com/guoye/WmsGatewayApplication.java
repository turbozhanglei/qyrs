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

	/**
	 * 文件上传配置
	 *
	 * @return
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//  单个数据大小
		factory.setMaxFileSize("102400KB"); // KB,MB
		/// 总上传数据大小
		factory.setMaxRequestSize("409600KB");
		return factory.createMultipartConfig();
	}

}
