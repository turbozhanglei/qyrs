package com.gy.resource.config;


import com.jic.common.redis.RedisClientTemplate;
import com.jic.common.redis.RedisDataSource;
import com.jic.common.redis.RedisDataSourceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Arrays;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.database}")
    private int databaseIndex;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.host}")
    private String masterHost;
    @Value("${spring.redis.port}")
    private int masterPort;
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxTotal;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Bean
    public JedisPoolConfig createJedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(-1);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        return config;
    }

    @Bean
    public JedisShardInfo createJedisShardInfo() {
        String url = String.format("http://%s:%s/%s", masterHost, masterPort, databaseIndex);
        JedisShardInfo info = new JedisShardInfo(url);
        info.setPassword(password);
        return info;
    }

    @Bean
    public ShardedJedisPool createShardedJedisPool(JedisPoolConfig config, JedisShardInfo info) {
        ShardedJedisPool pool = new ShardedJedisPool(config, Arrays.asList(info));
        return pool;
    }

    @Bean
    public RedisDataSource createRedisDataSource(ShardedJedisPool pool) {
        return new RedisDataSourceImpl(pool);
    }

    @Bean
    public RedisClientTemplate createRedisClientTemplate(RedisDataSource dataSource) {
        return new RedisClientTemplate(dataSource);
    }

}
