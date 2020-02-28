package com.gy.resource.service;

import com.gy.resource.request.rest.SetPhoneSwitchRequest;
import com.gy.resource.request.rest.UserRequest;
import com.jic.common.base.vo.RestResult;

/**
 * @author: gaolanyu
 * @date: 2020-02-27
 * @remark:
 */
public interface UserSerivice {
    public RestResult<Boolean> setPhoneSwitch(SetPhoneSwitchRequest setPhoneSwitchRequest);

    RestResult<String> queryPhoneSwitch(UserRequest userRequest);

    String queryPhoneSwitchByUserId(Long userId);
}
