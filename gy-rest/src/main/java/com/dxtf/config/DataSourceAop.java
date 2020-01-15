package com.dxtf.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/***
 * 定义切面切点
 */
@Aspect
@Component
@Slf4j
@Order(1)
public class DataSourceAop {

    @Before("execution(* com.dxtf.base.BaseMapper.find*(..)) or execution(* com.dxtf.base.BaseMapper.get*(..)) or execution(* com.dxtf.base.BaseMapper.select*(..)) or execution(* com.dxtf.base.BaseMapper.query*(..))")
    public void setReadDataSourceType() {
        DataSourceContextHolder.slave();
        log.info("dataSource切换到：slave");
    }

    @Before("execution(* com.dxtf.base.BaseMapper.insert*(..)) or execution(* com.dxtf.base.BaseMapper.del*(..)) or execution(* com.dxtf.base.BaseMapper.update*(..)) or execution(* com.dxtf.base.BaseMapper.add*(..)) or execution(* com.dxtf.base.BaseMapper.edit*(..)) or execution(* com.dxtf.base.BaseMapper.queryMaster*(..)) or execution(* com.dxtf.base.BaseMapper.save*(..))")
    public void setWriteDataSourceType() {
        DataSourceContextHolder.master();
        log.info("dataSource切换到：master");
    }
}
