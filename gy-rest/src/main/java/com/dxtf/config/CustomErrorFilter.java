package com.dxtf.config;

import com.dxtf.util.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义异常
 */
@Slf4j
@RestController
public class CustomErrorFilter implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public BaseResult error(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        try {
            Integer status = (Integer) request.getAttribute("javax.servlet.error.status_code");
            if (status == 404) {
                result.setCode("9999");
                result.setData("接口路径错误");
            } else if (status == 500) {
                result.setCode("9999");
                result.setData("服务异常");
            } else if (status == 504) {
                result.setCode("9999");
                result.setData("服务连接超时");
            }
        } catch (Exception ex) {
            ReflectionUtils.rethrowRuntimeException(ex);
        }
        return result;
    }
}
