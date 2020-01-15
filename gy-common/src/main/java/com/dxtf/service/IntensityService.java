package com.dxtf.service;

/**
 * @Auther: WuMengChang
 * @Date: 2018/6/2 12:04
 * @Description: 验证密码强度
 */
public interface IntensityService {
    /**
     * 验证密码强度
     * @param PassWord
     * @return 1：强 2：中 3：弱
     */
    Integer judgePW(String PassWord);
}
