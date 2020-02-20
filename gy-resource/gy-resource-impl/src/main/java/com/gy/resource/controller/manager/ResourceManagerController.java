package com.gy.resource.controller.manager;

import com.gy.resource.api.manager.ResourceManagerApi;
import com.gy.resource.constant.ResourceConstant;
import com.gy.resource.request.manager.CheckBatchRequest;
import com.gy.resource.request.manager.CheckRequest;
import com.gy.resource.request.manager.DownloadRequest;
import com.gy.resource.request.manager.QueryResourceManagerRequest;
import com.gy.resource.request.manager.ReportRequest;
import com.gy.resource.request.manager.TopRequest;
import com.gy.resource.response.manager.QueryResourceManagerResponse;
import com.gy.resource.response.manager.ReportResponse;
import com.gy.resource.service.ResourceManagerService;
import com.gy.resource.service.TokenService;
import com.jic.common.base.vo.PageResult;
import com.jic.common.base.vo.RestResult;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

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
    private static final String channel_manager= ResourceConstant.channel.Manager;
    @Autowired
    ResourceManagerService resourceManagerService;
    @Autowired
    TokenService tokenService;
    @ApiOperation(value = "资源信息列表分页查询")
    @PostMapping(value = "/query-resource-manager")
    public RestResult<PageResult<QueryResourceManagerResponse>> queryResourceManager(@RequestBody  QueryResourceManagerRequest resourceManagerRequest) {
        return resourceManagerService.queryResourceManager(resourceManagerRequest);
    }

    @ApiOperation(value = "批量审核")
    @PostMapping(value = "/batch-check")
    public RestResult<Boolean> checkBatch(@RequestBody CheckBatchRequest request) {
        String userId = tokenService.getUserIdByToken(request.getToken(),channel_manager);
        if (StringUtils.isBlank(userId)) {
            return RestResult.error("1000", "请重新登录");
        }
        request.setCheckUserId(userId);
        return resourceManagerService.checkBatch(request);
    }

    @ApiOperation(value = "单个审核")
    @PostMapping(value = "/check")
    public RestResult<Boolean> check(@RequestBody CheckRequest request) {
        String userId = tokenService.getUserIdByToken(request.getToken(),channel_manager);
        if (StringUtils.isBlank(userId)) {
            return RestResult.error("1000", "请重新登录");
        }
        request.setCheckUserId(userId);
        return resourceManagerService.check(request);
    }

    @ApiOperation(value = "置顶")
    @PostMapping(value = "/top")
    public RestResult<Boolean> top(@RequestBody TopRequest request) {
        String userId = tokenService.getUserIdByToken(request.getToken(),channel_manager);
        if (StringUtils.isBlank(userId)) {
            return RestResult.error("1000", "请重新登录");
        }
        request.setOperaId(userId);
        return resourceManagerService.top(request);
    }

    @ApiOperation(value = "资源信息列表页导出")
    @PostMapping(value = "/download-resource")
    public void resourceInfoListDownLoad(HttpServletResponse response,@RequestBody QueryResourceManagerRequest resourceManagerRequest) {
        resourceManagerService.resourceInfoListDownLoad(response,resourceManagerRequest);
    }

    @ApiOperation(value = "资源信息汇总报表页导出")
    @PostMapping(value = "/download-report")
    public void reportDownLoad(HttpServletResponse response,@RequestBody ReportRequest reportRequest) {
        resourceManagerService.reportDownLoad(response,reportRequest);
    }

    @ApiOperation(value = "资源信息汇总报表")
    @PostMapping(value = "/resource-report")
    public RestResult<PageResult<ReportResponse>> resourceReport(@RequestBody ReportRequest reportRequest) {
        return resourceManagerService.resourceReport(reportRequest);
    }
}
