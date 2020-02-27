package com.gy.resource.service.impl;

import com.gy.resource.mapper.UserMapper;
import com.gy.resource.request.rest.SetPhoneSwitchRequest;
import com.gy.resource.request.rest.UserRequest;
import com.gy.resource.service.UserSerivice;
import com.jic.common.base.vo.RestResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: gaolanyu
 * @date: 2020-02-27
 * @remark:
 */
@Service
@Slf4j
public class UserServiceImpl implements UserSerivice {
    @Autowired
    UserMapper userMapper;

    @Override
    public RestResult<Boolean> setPhoneSwitch(SetPhoneSwitchRequest setPhoneSwitchRequest) {
        long userId = Long.parseLong(setPhoneSwitchRequest.getUserId());
        int flag = userMapper.setPhoneSwitch(setPhoneSwitchRequest.getPhoneStatus(), userId);
        if (flag > 0) {
            return RestResult.success(true);
        }
        return RestResult.error("9999", "更新失败，请重试!");
    }

    @Override
    public RestResult<String> queryPhoneSwitch(UserRequest userRequest) {
        long userId = Long.parseLong(userRequest.getLoginUserId());
        String phoneSwitch = userMapper.queryPhoneSwitch(userId);
        return RestResult.success(phoneSwitch);
    }

    @Override
    public String queryPhoneSwitchByUserId(Long userId) {
        return userMapper.queryPhoneSwitch(userId);
    }

}
