package com.gy.resource.service.impl;


import com.gy.resource.mapper.MyBrowseMapper;
import com.gy.resource.mapper.MyFollowMapper;
import com.gy.resource.request.rest.MyFollowRequest;
import com.gy.resource.response.rest.MyBrowseResponse;
import com.gy.resource.response.rest.MyFollowPeopleResourceResponse;
import com.gy.resource.response.rest.MyFollowUserInfoResponse;
import com.gy.resource.service.MyBrowesService;
import com.gy.resource.service.MyFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  我的浏览记录服务类
 *
 * @author zhuxiankun
 * @since 2020-02-14
 */
@Service
public class MyBrowseServiceImpl implements MyBrowesService{

    @Autowired
    private MyBrowseMapper myBrowseMapper;

    /**
     * 根据用户id查询我的浏览记录
     * @param \
     */
    @Override
    public  List<MyBrowseResponse> queryMyBrowesByUserId(Long userId){
        return myBrowseMapper.queryMyBrowesByUserId(userId);
    }


}