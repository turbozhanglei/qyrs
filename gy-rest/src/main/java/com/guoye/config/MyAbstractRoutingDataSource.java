package com.guoye.config;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.concurrent.atomic.AtomicInteger;

/***
 * 多数据源切换，负载均衡定义
 * <p>Title: MyAbstractRoutingDataSource</p>
 * <p>Description: </p>
 * @author Carlton
 * @date 2018年9月28日
 */
@Slf4j
public class MyAbstractRoutingDataSource extends AbstractRoutingDataSource {

    private final int dataSourceNumber;
    private AtomicInteger count = new AtomicInteger(0);

    public MyAbstractRoutingDataSource(int dataSourceNumber) {
        this.dataSourceNumber = dataSourceNumber;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        /*String typeKey = DataSourceContextHolder.getJdbcType();
        if (typeKey.equals(DataSourceType.master.getType()))
			return DataSourceType.master.getType();
		// 读 简单负载均衡
		int number = count.getAndAdd(1);
		int lookupKey = number % dataSourceNumber;
		Integer i = lookupKey;
		return i;*/
        Object resultObject = null;
        String typeKey = DataSourceContextHolder.getJdbcType();
        //只对主库开启事务，如果typeKey为空表示获取主库的datasource
        if (StringUtils.isEmpty(typeKey)) {
            resultObject = DataSourceType.master.getType();
        } else {
            // 读简单负载均衡
            int number = count.getAndAdd(1);
            int lookupKey = number % dataSourceNumber;
            resultObject = new Integer(lookupKey);
        }
        log.info("determineCurrentLookupKey:" + resultObject);
        return resultObject;
    }

}
