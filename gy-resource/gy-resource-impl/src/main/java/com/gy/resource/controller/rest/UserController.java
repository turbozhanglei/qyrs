package com.gy.resource.controller.rest;

import com.gy.resource.api.rest.UserApi;
import com.gy.resource.constant.ResourceConstant;
import com.gy.resource.request.rest.SetPhoneSwitchRequest;
import com.gy.resource.request.rest.UserRequest;
import com.gy.resource.service.TokenService;
import com.gy.resource.service.UserSerivice;
import com.jic.common.base.vo.RestResult;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gaolanyu
 * @date: 2020-02-27
 * @remark:
 */
@RestController
@RequestMapping("/user-rest")
@Api(tags = {"拨打电话开关接口Api"})
@Slf4j
public class UserController implements UserApi {
    private static final String channel_WX = ResourceConstant.channel.WX;
    @Autowired
    UserSerivice userSerivice;
    @Autowired
    TokenService tokenService;

    @Override
    @ApiOperation(value = "设置是否可以拨打电话API")
    @PostMapping(value = "/set-phone-switch")
    public RestResult<Boolean> setPhoneSwitch(@RequestBody SetPhoneSwitchRequest setPhoneSwitchRequest) {
        String userId = tokenService.getUserIdByToken(setPhoneSwitchRequest.getToken(), channel_WX);
        if (StringUtils.isBlank(userId)) {
            return RestResult.error("1000", "请重新登录");
        }
        setPhoneSwitchRequest.setUserId(userId);
        return userSerivice.setPhoneSwitch(setPhoneSwitchRequest);
    }

    @Override
    @ApiOperation(value = "查询拨打电话开关API")
    @PostMapping(value = "/query-phone-switch")
    public RestResult<String> queryPhoneSwitch(@RequestBody UserRequest userRequest) {
        String userId = tokenService.getUserIdByToken(userRequest.getToken(), channel_WX);
        if (StringUtils.isBlank(userId)) {
            return RestResult.error("1000", "请重新登录");
        }
        userRequest.setLoginUserId(userId);
        return userSerivice.queryPhoneSwitch(userRequest);
    }
}
