package com.guoye.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 异常
 */

@Slf4j
public class MyEurekaClientFallback implements FallbackProvider {
    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
       return new ClientHttpResponse() {
                @Override
                public HttpStatus getStatusCode ()throws IOException {
                    return HttpStatus.OK;
                }

                @Override
                public int getRawStatusCode ()throws IOException {
                    return HttpStatus.OK.value();
                }

                @Override
                public String getStatusText ()throws IOException {
                    return HttpStatus.OK.getReasonPhrase();
                }

                @Override
                public void close () {
                }

                @Override
                public InputStream getBody ()throws IOException {
                    log.info(route);
                    log.info(cause.getMessage());
                    //可替换成相应的json串的 看业务规定了
//                    return new ByteArrayInputStream("".getBytes());
                    return new ByteArrayInputStream("{\"code\":\"9999\",\"msg\":\"网络连接失败，请稍等再试\"}".getBytes());
                }

                @Override
                public HttpHeaders getHeaders () {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    return headers;
                }
            };
        }
}
