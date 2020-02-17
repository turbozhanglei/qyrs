package com.gy.resource.service.impl;

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

import org.springframework.stereotype.Service;

/**
 * @author: gaolanyu
 * @date: 2020-02-15
 * @remark:
 */
@Service
public class ResourceManagerServiceImpl implements ResourceManagerService {
    @Override
    public RestResult<PageResult<QueryResourceManagerResponse>> queryResourceManager(QueryResourceManagerRequest resourceManagerRequest) {
        return null;
    }

    @Override
    public RestResult<Boolean> checkBatch(CheckBatchRequest request) {
        return null;
    }

    @Override
    public RestResult<Boolean> check(CheckRequest request) {
        return null;
    }

    @Override
    public RestResult<Boolean> top(TopRequest request) {
        return null;
    }

    @Override
    public void download(DownloadRequest downloadRequest) {

    }

    @Override
    public RestResult<ReportResponse> resourceReport(ReportRequest reportRequest) {
        return null;
    }
}
