package com.dxtf.config;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogsAspect implements Ordered {

    @Override
    public int getOrder() {
        // TODO Auto-generated method stub
        return 1;
    }
}
