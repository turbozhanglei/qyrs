package com.guoye.config;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author: gaolanyu
 * @date: 2020-02-26
 * @remark: 网关使用
 */
public class SwaggerConfig {
    public Docket groupRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(groupApiInfo());
    }

    private ApiInfo groupApiInfo() {
        return new ApiInfoBuilder()
                .title("中建投网关zuul集成接口文档地址")
                .description("<div style='font-size:14px;color:red;'>ZUUL RESTful APIs</div>")
                .termsOfServiceUrl("http://ip:port")
                .contact("774329481@qq.com")
                .version("1.0")
                .build();
    }
}

