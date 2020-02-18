package com.gy.resource.service;
import com.gy.resource.entity.SearchHistoryModel;
import com.gy.resource.request.rest.MyFollowRequest;
import com.gy.resource.response.rest.MyFollowPeopleResourceResponse;
import com.gy.resource.response.rest.MyFollowResponse;
import com.gy.resource.response.rest.MyFollowUserInfoResponse;
import com.gy.resource.response.rest.SearchHistoryResponse;
import com.jic.common.base.vo.Page;
import com.jic.common.base.vo.PageResult;

import java.util.List;
import java.util.Map;

/**
 *  我的关注服务类
 *
 * @author zhuxiankun
 * @since 2020-02-14
 */

public interface MyFollowService {

    /**
     *  查询我的关注列表
     * @param \
     */
    List<MyFollowUserInfoResponse> queryMyFollowByUserId(MyFollowRequest myFollowRequest);

    /**
     *  查询我的关注列表条数
     * @param \
     */
   Integer  queryMyFollowTotal(Long userId);

    /**
     *  查询我的关注人的最新发布的一篇文章
     * @param \
     */
   MyFollowPeopleResourceResponse queryMyFollowResourceByUserId(Long userId);
}