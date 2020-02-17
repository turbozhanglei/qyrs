package com.gy.resource.service.impl;


import com.gy.resource.entity.SearchHistoryModel;
import com.gy.resource.mapper.MyFollowMapper;
import com.gy.resource.mapper.SearchHistoryMapper;
import com.gy.resource.response.rest.MyFollowUserInfoResponse;
import com.gy.resource.response.rest.SearchHistoryResponse;
import com.gy.resource.service.MyFollowService;
import com.gy.resource.service.PSearchHistoryService;
import com.jic.common.base.vo.Page;
import com.jic.common.base.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *  我的关注服务类
 *
 * @author zhuxiankun
 * @since 2020-02-14
 */
@Service
public class MyFollowServiceImpl implements MyFollowService {

    @Autowired
    private MyFollowMapper myFollowMapper;

    /**
     * 根据用户id查询我的关注列表
     * @param \
     */
    @Override
    public  List<MyFollowUserInfoResponse> queryMyFollowByUserId(Long userId){
        return myFollowMapper.queryMyFollowByUserId(userId);
    }

    /**
     * 根据用户id查询我的关注人数
     * @param \
     */
    @Override
    public  Integer queryMyFollowTotal(Long userId){
        return myFollowMapper.queryMyFollowTotal(userId);
    }
}