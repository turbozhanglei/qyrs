package com.dxtf.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

/***
 * 自定义事务
 * <p>Title: DataSourceTransactionManager</p>
 * <p>Description: </p>
 * @author Carlton
 * @date 2018年9月28日
 */
//@Configuration
@EnableTransactionManagement
@Slf4j
@Order(2)
public class DataSourceTransactionManager extends DataSourceTransactionManagerAutoConfiguration {

	/*@Resource(name = "masterDataSource")
    private DataSource dataSource;

	*//**
     * 自定义事务
     * MyBatis自动参与到spring事务管理中，无需额外配置，只要org.mybatis.spring.SqlSessionFactoryBean引用的数据源与DataSourceTransactionManager引用的数据源一致即可，否则事务管理会不起作用。
     *
     * @return
     *//*
    @Bean(name = "transactionManager")
	public org.springframework.jdbc.datasource.DataSourceTransactionManager transactionManagers() {
		log.info("-------------------- masterTransactionManager init ---------------------");
		return new org.springframework.jdbc.datasource.DataSourceTransactionManager(dataSource);
	}*/
}
