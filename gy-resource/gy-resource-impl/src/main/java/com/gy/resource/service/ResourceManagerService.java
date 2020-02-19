package com.gy.resource.service;

import com.gy.resource.request.manager.CheckBatchRequest;
import com.gy.resource.request.manager.CheckRequest;
import com.gy.resource.request.manager.DownloadRequest;
import com.gy.resource.request.manager.QueryResourceManagerRequest;
import com.gy.resource.request.manager.ReportRequest;
import com.gy.resource.request.manager.TopRequest;
import com.gy.resource.response.manager.QueryResourceManagerResponse;
import com.gy.resource.response.manager.ReportResponse;
import com.jic.common.base.vo.PageResult;
import com.jic.common.base.vo.RestResult;

import javax.servlet.http.HttpServletResponse;

/**
 * @author: gaolanyu
 * @date: 2020-02-15
 * @remark:
 */
public interface ResourceManagerService {
    public RestResult<Boolean> check(CheckRequest request);

    public RestResult<PageResult<QueryResourceManagerResponse>> queryResourceManager(QueryResourceManagerRequest resourceManagerRequest);

    public RestResult<Boolean> checkBatch(CheckBatchRequest request);

    public RestResult<Boolean> top(TopRequest request);

    public RestResult<PageResult<ReportResponse>> resourceReport(ReportRequest reportRequest);

    public void resourceInfoListDownLoad(HttpServletResponse response, QueryResourceManagerRequest resourceManagerRequest);

    public void reportDownLoad(HttpServletResponse response,ReportRequest reportRequest);

}
