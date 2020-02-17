package com.gy.resource.controller.manager;

import com.gy.resource.api.manager.ResourceManagerApi;
import com.gy.resource.request.manager.CheckBatchRequest;
import com.gy.resource.request.manager.CheckRequest;
import com.gy.resource.request.manager.DownloadRequest;
import com.gy.resource.request.manager.QueryResourceManagerRequest;
import com.gy.resource.request.manager.ReportRequest;
import com.gy.resource.request.manager.TopRequest;
import com.gy.resource.response.manager.QueryResourceManagerResponse;
import com.gy.resource.response.manager.ReportResponse;
import com.gy.resource.service.ResourceManagerService;
import com.jic.common.base.vo.PageResult;
import com.jic.common.base.vo.RestResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gaolanyu
 * @date: 2020-02-14
 * @remark:
 */
@RestController
@RequestMapping("/resource-manager")
@Api(tags = {"资源后台管理接口"})
@Slf4j
public class ResourceManagerController implements ResourceManagerApi {

    @Autowired
    ResourceManagerService resourceManagerService;

    @ApiOperation(value = "资源信息列表分页查询")
    @PostMapping(value = "/query-resource-manager")
    public RestResult<PageResult<QueryResourceManagerResponse>> queryResourceManager(@RequestBody  QueryResourceManagerRequest resourceManagerRequest) {
        return resourceManagerService.queryResourceManager(resourceManagerRequest);
    }

    @ApiOperation(value = "批量审核")
    @PostMapping(value = "/batch-check")
    public RestResult<Boolean> checkBatch(@RequestBody CheckBatchRequest request) {
        return null;
    }

    @ApiOperation(value = "单个审核")
    @PostMapping(value = "/check")
    public RestResult<Boolean> check(@RequestBody CheckRequest request) {
        return null;
    }

    @ApiOperation(value = "置顶")
    @PostMapping(value = "/top")
    public RestResult<Boolean> top(@RequestBody TopRequest request) {
        return null;
    }

    @ApiOperation(value = "导出")
    @PostMapping(value = "/download")
    public void download(@RequestBody DownloadRequest downloadRequest) {

    }

    @ApiOperation(value = "资源信息汇总报表")
    @PostMapping(value = "/resource-report")
    public RestResult<ReportResponse> resourceReport(@RequestBody ReportRequest reportRequest) {
        return null;
    }
}
