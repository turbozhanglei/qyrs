package com.gy.resource.service;
import com.gy.resource.request.rest.MyFollowRequest;
import com.gy.resource.response.rest.MyBrowseResponse;
import com.gy.resource.response.rest.MyFollowPeopleResourceResponse;
import com.gy.resource.response.rest.MyFollowUserInfoResponse;

import java.util.List;

/**
 *  我的浏览记录服务类
 *
 * @author zhuxiankun
 * @since 2020-02-14
 */

public interface MyBrowesService {

    /**
     *  查询我的浏览记录
     * @param \
     */
    List<MyBrowseResponse> queryMyBrowesByUserId(Long userId);

}