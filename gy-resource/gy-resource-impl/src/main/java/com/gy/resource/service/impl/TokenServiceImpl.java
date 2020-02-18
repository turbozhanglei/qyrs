package com.gy.resource.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.gy.resource.constant.ResourceConstant;
import com.gy.resource.service.TokenService;
import com.gy.resource.utils.DESWrapper;
import com.jic.common.redis.RedisClientTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: gaolanyu
 * @date: 2020-02-17
 * @remark:
 */
@Service
@Slf4j
public class TokenServiceImpl implements TokenService {
    @Value("${phone_key}")
    private String phone_key;
    @Autowired
    RedisClientTemplate redisClientTemplate;
    @Override
    public String getUserIdByToken(String token,String channel) {
        //TODO 先放在这里 token 解析后期放网关
        // 获取用户id
        String userStr="";
        if(ResourceConstant.channel.WX.equals(channel)){
            userStr=redisClientTemplate.get("mp_login_token:" + token);
        }else if(ResourceConstant.channel.Manager.equals(channel)){
            userStr=redisClientTemplate.get(token);
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(userStr)) {
            return "";
        }
        Map<String, Object> userMap = JSONArray.parseObject(userStr, HashMap.class);
        String userId = userMap.get("id").toString();
        return userId;
    }

    @Override
    public String decryptMobile(String mobile) {
        String encryMobile="";
        try{
            encryMobile=DESWrapper.decrypt(mobile,phone_key);
        }catch (Exception e){
            log.error("解密手机号出错",e);
        }
        return encryMobile;
    }
}
