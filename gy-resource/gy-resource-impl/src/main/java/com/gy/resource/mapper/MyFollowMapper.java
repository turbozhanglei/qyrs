package com.gy.resource.mapper;

import com.gy.resource.entity.SearchHistoryModel;
import com.gy.resource.response.rest.MyFollowUserInfoResponse;
import com.gy.resource.response.rest.SearchHistoryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 我的关注 dao
 *
 * @author zhuxiankun
 * @since 2020-02-14
 */
@Mapper
public interface MyFollowMapper {


    /**
     *根据用户id查询我的关注
     *@Param Long
     */
    List<MyFollowUserInfoResponse> queryMyFollowByUserId(@Param("userId") Long userId);

    /**
     *根据用户id查询我的关注
     *@Param Long
     */
   Integer queryMyFollowTotal(@Param("userId") Long userId);

}
