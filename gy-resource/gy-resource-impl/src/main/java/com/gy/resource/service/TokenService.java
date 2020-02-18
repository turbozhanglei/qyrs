package com.gy.resource.service;

/**
 * @author: gaolanyu
 * @date: 2020-02-17
 * @remark:
 */
public interface TokenService {
    public String getUserIdByToken(String token,String channel);

    public String decryptMobile(String mobile);
}
