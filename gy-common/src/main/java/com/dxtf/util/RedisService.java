package com.dxtf.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.g4studio.core.resource.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class RedisService {
    @Autowired
    protected RedisTemplate<String, String> redisTemplate;

    /**
     * 放置key value 失效时间
     * @param key
     * @param value
     * @param seconds 按秒计算
     */
    public void setValue(final String key,final String value,final Long seconds) {
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.setEx(redisTemplate.getStringSerializer().serialize(key),
                        seconds, redisTemplate.getStringSerializer().serialize(value));
                return null;
            }
        });
    }

    /**
     * 放置key value 失效时间
     * @param key
     * @param value
     */
    public void setValue(final String key,final String value) {
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(redisTemplate.getStringSerializer().serialize(key),
                        redisTemplate.getStringSerializer().serialize(value));
                return null;
            }
        });
    }

    /**
     * 获取value
     * @param key
     * @return
     */
    public String getValue(final String key) {

        return redisTemplate.execute(new RedisCallback<String>() {
            String value = "";
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] byteKey = redisTemplate.getStringSerializer().serialize(key);
                if (connection.exists(byteKey)) {
                    byte[] bytes = connection.get(byteKey);
                    if(bytes.length>0){
                        value = redisTemplate.getStringSerializer().deserialize(bytes);
                        return value;
                    }
                }
                return value;
            }
        });
    }

    /**
     * 返回对象泛型
     * @param key
     * @param cla
     * @return
     */
    public <T> T getObject(final String key,Class<?> cla) {
        if(StringUtils.isNotEmpty(key)){
            String value = this.getValue(key);
            if(!StringUtils.isNotEmpty(value)){
                return null;
            }
            try {
                @SuppressWarnings("unchecked")
                T t = (T) JSON.parseObject(value, cla);
                return t;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }
    }

    /**
     * 返回对象list 泛型
     * @param key
     * @param cla
     * @return
     */
    public List<?> getList(final String key,Class<?> cla) {
        String value = this.getValue(key);
        if(!StringUtils.isNotEmpty(value)){
            return null;
        }
        try {
            List<?> tlist = JSON.parseArray(value, cla);
            return tlist;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除
     * @param key
     */
    public void delete(String key) {
        List<String> list = new ArrayList<String>();
        list.add(key);
        delete(list);
    }

    /**
     * 删除多个
     * @param keys
     */
    public void delete(List<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 获取value
     * @param pattern
     * @return
     */
    public List<String> getKeysLike(final String pattern) {
        return redisTemplate.execute(new RedisCallback<List<String>>() {
            List<String> list = new ArrayList<String>();
            @Override
            public List<String> doInRedis(RedisConnection connection) throws DataAccessException {

                Set<String> keys = redisTemplate.keys(pattern);
                Iterator<String> iterator2 = keys.iterator();
                while(iterator2.hasNext()){
                    String value =iterator2.next();
                    if(StringUtils.isNotEmpty(value)){
                        list.add(value);
                    }
                }
                return list;
            }
        });
    }
    public void deleteByPrex(String prex) {
        Set<String> keys = redisTemplate.keys(prex+"*");
        if (CollectionUtils.isNotEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }

    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }
}

