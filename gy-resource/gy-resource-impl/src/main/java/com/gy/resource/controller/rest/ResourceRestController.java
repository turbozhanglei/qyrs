package com.gy.resource.controller.rest;

import com.gy.resource.api.rest.ResourceApi;
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

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gaolanyu
 * @date: 2020-02-14
 * @remark:
 */
@RestController
@RequestMapping("/resource-rest")
@Api(tags = {"资源小程序接口"})
@Slf4j
public class ResourceRestController implements ResourceApi {
    @ApiOperation(value = "发布资源api，返回资源id")
    @PostMapping(value = "/issure-resource")
    public RestResult<String> issureResourceApi(@RequestBody IssureResourceRequest resourceRequest) {
        return null;
    }

    @ApiOperation(value = "查询资源详情包括内容")
    @PostMapping(value = "/query-resource-detail")
    public RestResult<QueryResourceResponse> queryResource(@RequestBody QueryResourceRequest resourceRequest) {
        return null;
    }

    @ApiOperation(value = "首页推荐资源")
    @PostMapping(value = "/recommend-resource")
    public RestResult<List<RecommendResourceResponse>> recommendResource() {
        return null;
    }

    @ApiOperation(value = "根据筛选条件查询资源列表")
    @PostMapping(value = "/query-resource-condition")
    public RestResult<List<QueryResourceByConditionResponse>> queryResourceByCondition(@RequestBody QueryResourceByConditionRequest resourceByConditionRequest) {
        return null;
    }

    @ApiOperation(value = "查询用户发布的资源列表")
    @PostMapping(value = "/query-resource-user")
    public RestResult<PageResult<QueryResourceByUserIdResponse>> queryResourceByUserId(@RequestBody UserRequest request) {
        return null;
    }
}
