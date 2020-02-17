package com.gy.resource.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.gy.resource.service.TokenService;
import com.jic.common.redis.RedisClientTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: gaolanyu
 * @date: 2020-02-17
 * @remark:
 */
@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    RedisClientTemplate redisClientTemplate;
    @Override
    public String getUserIdByToken(String token) {
        //TODO 先放在这里 token 解析后期放网关
        // 获取用户id
        String userStr = redisClientTemplate.get("mp_login_token:" + token);
        if (org.apache.commons.lang.StringUtils.isEmpty(userStr)) {
            return "";
        }
        Map<String, Object> userMap = JSONArray.parseObject(userStr, HashMap.class);
        String userId = userMap.get("id").toString();
        return userId;
    }
}
