package com.guoye.config;

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

    @Before("execution(* com.guoye.base.BaseMapper.find*(..)) or execution(* com.guoye.base.BaseMapper.get*(..)) or execution(* com.guoye.base.BaseMapper.select*(..)) or execution(* com.guoye.base.BaseMapper.query*(..))")
    public void setReadDataSourceType() {
        DataSourceContextHolder.slave();
        log.info("dataSource切换到：slave");
    }

    @Before("execution(* com.guoye.base.BaseMapper.insert*(..)) or execution(* com.guoye.base.BaseMapper.del*(..)) or execution(* com.guoye.base.BaseMapper.update*(..)) or execution(* com.guoye.base.BaseMapper.add*(..)) or execution(* com.guoye.base.BaseMapper.edit*(..)) or execution(* com.guoye.base.BaseMapper.queryMaster*(..)) or execution(* com.guoye.base.BaseMapper.save*(..))")
    public void setWriteDataSourceType() {
        DataSourceContextHolder.master();
        log.info("dataSource切换到：master");
    }
}
