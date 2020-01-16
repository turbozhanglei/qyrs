package com.guoye.config;

import lombok.extern.slf4j.Slf4j;

/***
 * 本地全局变量，切换数据源
 * <p>Title: DataSourceContextHolder</p>
 * <p>Description: </p>
 * @author Carlton
 * @date 2018年9月28日
 */
@Slf4j
public class DataSourceContextHolder {

    private static final ThreadLocal<String> local = new ThreadLocal<String>();

    public static ThreadLocal<String> getLocal() {
        return local;
    }

    /**
     * 读可能是多个库
     */
    public static void slave() {
        local.set(DataSourceType.slave.getType());
    }

    /**
     * 写只有一个库
     */
    public static void master() {
        log.debug("master");
        local.set(DataSourceType.master.getType());
    }

    public static String getJdbcType() {
        return local.get();
    }
}
