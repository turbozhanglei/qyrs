package com.gy.resource.service;

import com.gy.resource.entity.ResourceInfo;
import com.gy.resource.request.rest.IssureResourceRequest;
import com.gy.resource.request.rest.QueryResourceByConditionRequest;
import com.gy.resource.request.rest.QueryResourceRequest;
import com.gy.resource.request.rest.UserRequest;
import com.gy.resource.response.rest.QueryResourceByConditionResponse;
import com.gy.resource.response.rest.QueryResourceByUserIdResponse;
import com.gy.resource.response.rest.QueryResourceResponse;
import com.gy.resource.response.rest.RecommendResourceResponse;
import com.jic.common.base.vo.Page;
import com.jic.common.base.vo.PageResult;
import com.jic.common.base.vo.RestResult;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 资源信息
 *
 * @author : gaoly
 * @version : v1.0.0
 * @email : 774329481@qq.com
 * @since : 2020-02-14 11:08:42
 */
public interface ResourceInfoService {
    public RestResult<String> issureResourceApi(IssureResourceRequest resourceRequest);

    public RestResult<QueryResourceResponse> queryResource(QueryResourceRequest resourceRequest);

    public RestResult<PageResult<QueryResourceByUserIdResponse>> queryResourceByUserId(UserRequest request);

    public RestResult<List<RecommendResourceResponse>> recommendResource();

    public RestResult<PageResult<QueryResourceByConditionResponse>> queryResourceByCondition(QueryResourceByConditionRequest resourceByConditionRequest);

    long insert(ResourceInfo resourceInfo);

    long delete(Long id);

    long update(ResourceInfo resourceInfo);

    ResourceInfo queryByPrimaryKey(Long id);

    List<ResourceInfo> query(ResourceInfo resourceInfo);

    PageResult<ResourceInfo> queryPageOrderByAuditTime(ResourceInfo resourceInfo, Page pageQuery);

    public PageResult<ResourceInfo> queryPageByCondition(ResourceInfo resourceInfo, Page pageQuery,List<Integer> releaseTypeList,
                                                         List<Integer> resourceLabelList,
                                                         List<Integer> resourceAreaList,
                                                         List<Integer> tradeTypeList,
                                                         Integer refType,
                                                         Integer sortType);
    public PageResult<ResourceInfo> queryResourceInfoListManager(ResourceInfo resourceInfo, Page pageQuery);

    public PageResult<ResourceInfo> queryReportListManager(ResourceInfo resourceInfo, Page pageQuery,Integer refType);

}