package com.gy.resource.api.rest;

import com.gy.resource.request.rest.*;
import com.gy.resource.response.rest.*;
import com.jic.common.base.vo.PageResult;
import com.jic.common.base.vo.RestResult;

import java.util.List;

/**
 * @author: gaolanyu
 * @date: 2020-02-13
 * @remark: 跟资源相关的 增删改查 审核 置顶等等
 */
public interface ResourceApi {
    public RestResult<String> issureResourceApi(IssureResourceRequest resourceRequest);

    public RestResult<QueryResourceResponse> queryResource(QueryResourceRequest resourceRequest);

    public RestResult<List<RecommendResourceResponse>> recommendResource();

    public RestResult<List<QueryResourceByConditionResponse>> queryResourceByCondition(QueryResourceByConditionRequest resourceByConditionRequest);

    public RestResult<PageResult<QueryResourceByUserIdResponse>> queryResourceByUserId(UserRequest request);

    /**
     * 通过资源标题名称模糊搜索联想列表
     * @param req
     * @return 联想列表
     */
    RestResult<List<QueryWordsResponse>> queryWords(QueryWordsRequest req);

    /**
     * 通关注 取消关注 发布资源的用户 (点赞)
     * @param req
     * @return
     */
    RestResult<Boolean> followRef(FollowRefRequest req);

    /**
     * 通关注 查询当前用户是否关注了发布资源用户(资源)
     * @param req
     * @return true 关注or false 没有关注
     */
    RestResult<Boolean> queryFollowStatus(QueryFollowStatusRequest req);

    /**
     * 查询关注我的(资源)人数量
     * @param req
     * @return true 关注or false 没有关注
     */
    RestResult<Integer> queryFollowCount(QueryFollowCountRequest req);

    /**
     * 浏览分享打电话记录
     * @param req
     * @return true 关注or false 没有关注
     */
    RestResult<Boolean> addCorrelation(QueryFollowCountRequest req);


}
