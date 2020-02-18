package com.gy.resource.mapper;

import com.gy.resource.request.rest.MyFollowRequest;
import com.gy.resource.response.rest.MyBrowseResponse;
import com.gy.resource.response.rest.MyFollowPeopleResourceResponse;
import com.gy.resource.response.rest.MyFollowUserInfoResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 我的关注 dao
 *
 * @author zhuxiankun
 * @since 2020-02-14
 */
@Mapper
public interface MyBrowseMapper {


    /**
     *根据用户id查询我的浏览记录
     *@Param Long
     */
    List<MyBrowseResponse> queryMyBrowesByUserId(@Param("userId") Long userId);


    /**
     *根据用户id删除我的浏览记录
     *@Param Long
     */
    Integer myBrowesDelete(@Param("userId") Long userId);
}
