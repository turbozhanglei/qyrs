package com.gy.resource.api.rest;

import com.gy.resource.request.rest.IssureResourceRequest;
import com.gy.resource.request.rest.QueryResourceByConditionRequest;
import com.gy.resource.request.rest.QueryResourceRequest;
import com.gy.resource.request.rest.UserRequest;
import com.gy.resource.response.rest.QueryResourceByConditionResponse;
import com.gy.resource.response.rest.QueryResourceByUserIdResponse;
import com.gy.resource.response.rest.QueryResourceResponse;
import com.gy.resource.response.rest.RecommendResourceResponse;
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


}
