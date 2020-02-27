package com.gy.resource.api.rest;

import com.gy.resource.request.rest.SetPhoneSwitchRequest;
import com.gy.resource.request.rest.UserRequest;
import com.jic.common.base.vo.RestResult;

/**
 * @author: gaolanyu
 * @date: 2020-02-27
 * @remark: 不合适但是先放着，gy-rest项目有点乱，先放这里
 */
public interface UserApi {
    RestResult<Boolean> setPhoneSwitch(SetPhoneSwitchRequest setPhoneSwitchRequest);

    RestResult<String> queryPhoneSwitch(UserRequest userRequest);
}
