package com.gy.resource.service.impl;

import com.alibaba.excel.EasyExcel;
import com.gy.resource.constant.ResourceConstant;
import com.gy.resource.entity.ResourceInfo;
import com.gy.resource.mapper.DictionaryMapper;
import com.gy.resource.mapper.ResourceInfoMapper;
import com.gy.resource.request.manager.CheckBatchRequest;
import com.gy.resource.request.manager.CheckRequest;
import com.gy.resource.request.manager.QueryResourceManagerRequest;
import com.gy.resource.request.manager.ReportRequest;
import com.gy.resource.request.manager.TopRequest;
import com.gy.resource.response.manager.QueryResourceManagerResponse;
import com.gy.resource.response.manager.ReportResponse;
import com.gy.resource.service.ResourceInfoService;
import com.gy.resource.service.ResourceManagerService;
import com.jic.common.base.vo.Page;
import com.jic.common.base.vo.PageResult;
import com.jic.common.base.vo.RestResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: gaolanyu
 * @date: 2020-02-15
 * @remark:
 */
@Service
@Slf4j
public class ResourceManagerServiceImpl implements ResourceManagerService {
    @Autowired
    ResourceInfoService resourceInfoService;
    @Autowired
    ResourceInfoMapper resourceInfoMapper;
    @Autowired
    DictionaryMapper dictionaryMapper;

    @Override
    public RestResult<PageResult<QueryResourceManagerResponse>> queryResourceManager(QueryResourceManagerRequest resourceManagerRequest) {
        ResourceInfo resourceInfo = setResourceInfo(resourceManagerRequest);
        Page page = setPage(resourceManagerRequest.getStart(), resourceManagerRequest.getLimit());
        PageResult<ResourceInfo> pageResult = resourceInfoService.queryResourceInfoListManager(resourceInfo, page);
        PageResult<QueryResourceManagerResponse> result = setQueryResourceManagerResponse(pageResult, resourceManagerRequest);
        return RestResult.success(result);
    }

    public PageResult<QueryResourceManagerResponse> setQueryResourceManagerResponse(PageResult<ResourceInfo> pageResult, QueryResourceManagerRequest request) {
        PageResult<QueryResourceManagerResponse> responsePageResult = new PageResult<>();
        if (CollectionUtils.isEmpty(pageResult.getRows())) {
            return responsePageResult;
        }
        List<ResourceInfo> resourceInfoList = pageResult.getRows();
        List<QueryResourceManagerResponse> responseList = setQueryResourceManagerResponseList(resourceInfoList);
        if (org.apache.commons.lang.StringUtils.isNotBlank(request.getBrowseStartNum()) &&
                org.apache.commons.lang.StringUtils.isNotBlank(request.getBrowseEndNum())) {
            responseList.stream().filter(s -> Integer.parseInt(s.getBrowseNum()) >= Integer.parseInt(request.getBrowseStartNum())
                    && Integer.parseInt(s.getBrowseNum()) <= Integer.parseInt(request.getBrowseEndNum()))
                    .collect(Collectors.toList());
        }

        if (org.apache.commons.lang.StringUtils.isNotBlank(request.getShareStartNum()) &&
                org.apache.commons.lang.StringUtils.isNotBlank(request.getShareEndNum())) {
            responseList.stream().filter(s -> Integer.parseInt(s.getShareNum()) >= Integer.parseInt(request.getShareStartNum())
                    && Integer.parseInt(s.getShareNum()) <= Integer.parseInt(request.getShareEndNum()))
                    .collect(Collectors.toList());
        }
        responsePageResult.setRows(responseList);
        responsePageResult.setTotal(pageResult.getTotal());
        return responsePageResult;
    }

    public List<QueryResourceManagerResponse> setQueryResourceManagerResponseList(List<ResourceInfo> resourceInfoList) {
        List<QueryResourceManagerResponse> responseList = new ArrayList<>(resourceInfoList.size());
        resourceInfoList.stream().forEach(item -> {
            QueryResourceManagerResponse response = new QueryResourceManagerResponse();
            response.setBrowseNum(Integer.toString(item.getBrowNum()));
            response.setCreateTime(item.getCreateTime());
            response.setIssureId(Long.toString(item.getUserId()));
            response.setIssurePhone(item.getMobile());
            response.setIssureStatus(Integer.toString(item.getStatus()));
            response.setResourceArea(Integer.toString(item.getResourceArea()));
            response.setResourceId(Long.toString(item.getId()));
            response.setResourceLabel(Integer.toString(item.getResourceLabel()));
            response.setResourceTitle(item.getTitle());
            response.setResourceType(Integer.toString(item.getReleaseType()));
            response.setShareNum(Integer.toString(item.getShareNum()));
            response.setTopStatus(Integer.toString(item.getSticky()));
            response.setTradeType(Integer.toString(item.getResourceTrade()));
            responseList.add(response);
        });
        return responseList;
    }

    public String getShareNum(ResourceInfo resourceInfo) {
        return "0";
    }

    public String getBrowseNum(ResourceInfo resourceInfo) {
        return "0";
    }

    public String getPhoneNum(ResourceInfo resourceInfo) {
        return "0";
    }

    public Page setPage(int start, int limit) {
        Page page = new Page();
        page.setStart(start);
        page.setLimit(limit);
        return page;
    }

    public ResourceInfo setResourceInfo(QueryResourceManagerRequest request) {
        ResourceInfo resourceInfo = new ResourceInfo();
        Long id=getLongValue(request.getResourceId());
        if(id!=null){
            resourceInfo.setId(getLongValue(request.getResourceId()));
        }
        if(!StringUtils.isEmpty(request.getResourceTitle())){
            resourceInfo.setTitle(request.getResourceTitle());
        }
        resourceInfo.setReleaseType(getValueByParam(request.getResourceType()));
        resourceInfo.setStatus(getValueByParam(request.getIssureStatus()));
        resourceInfo.setSticky(getValueByParam(request.getTopStatus()));
        resourceInfo.setCreateStartTime(request.getCreateStartTime());
        resourceInfo.setCreateEndTime(request.getCreateEndTime());
        Long userId=getLongValue(request.getIssureId());
        if(userId!=null){
            resourceInfo.setUserId(getLongValue(request.getIssureId()));
        }
        resourceInfo.setMobile(request.getIssurePhone());
        resourceInfo.setImageFlag(getValueByParam(request.getIssureImage()));
        resourceInfo.setSensitiveFlag(getValueByParam(request.getSensitiveCode()));
        resourceInfo.setResourceLabel(getValueByParam(request.getResourceLabel()));
        resourceInfo.setResourceArea(getValueByParam(request.getResourceArea()));
        resourceInfo.setResourceTrade(getValueByParam(request.getTradeType()));
        resourceInfo.setBrowNumStart(getValueByParam(request.getBrowseStartNum()));
        resourceInfo.setBrowNumEnd(getValueByParam(request.getBrowseEndNum()));
        resourceInfo.setShareNumStart(getValueByParam(request.getShareStartNum()));
        resourceInfo.setShareNumEnd(getValueByParam(request.getShareEndNum()));
        return resourceInfo;
    }

    public Long getLongValue(String param) {
        if (StringUtils.isEmpty(param)) {
            return null;
        }
        return Long.parseLong(param);
    }

    public String getValue(String param) {
        if (StringUtils.isEmpty(param)) {
            return null;
        }
        return param;
    }

    public Integer getValueByParam(String param) {
        if (org.apache.commons.lang.StringUtils.equals(param, "-1") || StringUtils.isEmpty(param)) {
            return null;
        }
        return Integer.parseInt(param);
    }

    @Override
    public RestResult<Boolean> checkBatch(CheckBatchRequest request) {
        try {
            if (CollectionUtils.isEmpty(request.getResourceIdList()) ||
                    org.apache.commons.lang.StringUtils.isBlank(request.getCheckStatus())) {
                return RestResult.error("9999", "参数有误");
            }
            List<Long> idList = request.getResourceIdList().stream().map(n -> Long.parseLong(n)).collect(Collectors.toList());
            long flag = resourceInfoMapper.checkBatch(Integer.parseInt(request.getCheckStatus()), request.getCheckUserId(), idList);
            if (flag > 0) {
                return RestResult.success(true);
            }
            return RestResult.error("9999", "请检查参数和数据");
        } catch (Exception e) {
            log.error("checkBatch==>error", e);
            return RestResult.error("9999", e.getMessage());
        }
    }

    @Override
    public RestResult<Boolean> check(CheckRequest request) {
        log.info("check==>req:{}", request);
        try {
            long flag = resourceInfoMapper.check(Integer.parseInt(request.getCheckStatus()),
                    request.getCheckUserId(),
                    Long.parseLong(request.getResourceIdList()));
            if (flag > 0) {
                return RestResult.success(true);
            }
            return RestResult.error("9999", "该单号审核异常,请重试!");
        } catch (Exception e) {
            log.error("checkBatch==>error", e);
            return RestResult.error("9999", e.getMessage());
        }
    }

    @Override
    public RestResult<Boolean> top(TopRequest request) {
        try {
            long flag = resourceInfoMapper.top(Integer.parseInt(request.getTopStatus()), request.getOperaId(), Long.parseLong(request.getResourceIdList()));
            if (flag > 0) {
                return RestResult.success(true);
            }
            return RestResult.error("9999", "未操作成功，请重试!");
        } catch (Exception e) {
            log.error("top==>error", e);
            return RestResult.error("9999", e.getMessage());
        }
    }

    @Override
    public RestResult<PageResult<ReportResponse>> resourceReport(ReportRequest reportRequest) {
        ResourceInfo resourceInfo = setResourceInfo(reportRequest);
        Page page = setPage(reportRequest.getStart(), reportRequest.getLimit());
        PageResult<ResourceInfo> pageResult = resourceInfoService.queryPageOrderByAuditTime(resourceInfo, page);
        PageResult<ReportResponse> result=setReportResponsePageResult(pageResult,reportRequest);
        return RestResult.success(result);
    }

    @Override
    public void resourceInfoListDownLoad(HttpServletResponse response, QueryResourceManagerRequest resourceManagerRequest) {
        resourceManagerRequest.setStart(1);
        //excel单sheet最大6万多。此数量也要限制，以防IO过大
        resourceManagerRequest.setLimit(80000);
        RestResult<PageResult<QueryResourceManagerResponse>> result= queryResourceManager(resourceManagerRequest);
        try{
            String fileName="resourceInfoList";
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setContentType("application/x-download");
            List<QueryResourceManagerResponse> responseList=result.getData().getRows();
            if(!CollectionUtils.isEmpty(responseList)){
                responseList=responseList.stream().map(item->{
                    item.setResourceType(dictionaryMapper.queryDesc(ResourceConstant.category.release_type,item.getResourceType()));
                    item.setResourceLabel(dictionaryMapper.queryDesc(ResourceConstant.category.resource_label,item.getResourceLabel()));
                    item.setResourceArea(dictionaryMapper.queryDesc(ResourceConstant.category.resource_area,item.getResourceArea()));
                    item.setTradeType(dictionaryMapper.queryDesc(ResourceConstant.category.resource_trade,item.getTradeType()));
                    item.setIssureStatus(getCheckStatus(Integer.parseInt(item.getIssureStatus())));
                    item.setTopStatus(getTopStatus(Integer.parseInt(item.getTopStatus())));
                    return item;
                }).collect(Collectors.toList());
            }
            EasyExcel.write(response.getOutputStream(), QueryResourceManagerResponse.class).sheet("资源信息列表").doWrite(responseList);
        }catch (Exception e){
            log.error("resourceInfoListDownLoad==>error",e);
        }
    }

    public String getTopStatus(Integer topStatus){
        if(ResourceConstant.top.top==topStatus){
            return "置顶";
        }else if(ResourceConstant.top.down==topStatus){
            return "未置顶";
        }
        return "";
    }

    public String getCheckStatus(Integer checkStatus){
        //0、待审核，1、系统审核通过，2、待人工审核，3、人工审核通过，4、人工审核不通过
        if(ResourceConstant.check.check_init==checkStatus){
            return "待审核";
        }else if(ResourceConstant.check.system_check_success==checkStatus){
            return "系统审核通过";
        }else if(ResourceConstant.check.person_check_need==checkStatus){
            return "待人工审核";
        }else if(ResourceConstant.check.person_check_success==checkStatus){
            return "人工审核通过";
        }else if(ResourceConstant.check.person_check_fail==checkStatus){
            return "人工审核不通过";
        }
        return "";
    }

    @Override
    public void reportDownLoad(HttpServletResponse response, ReportRequest reportRequest) {
        reportRequest.setStart(1);
        //excel单sheet最大6万多。此数量也要限制，以防IO过大
        reportRequest.setLimit(80000);
        RestResult<PageResult<ReportResponse>> result= resourceReport(reportRequest);
        try{
            String fileName="resourceReportList";
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setContentType("application/x-download");
            List<ReportResponse> responseList=result.getData().getRows();
            if(!CollectionUtils.isEmpty(responseList)){
                responseList=responseList.stream().map(item->{
                    item.setResourceType(dictionaryMapper.queryDesc(ResourceConstant.category.release_type,item.getResourceType()));
                    item.setResourceLabel(dictionaryMapper.queryDesc(ResourceConstant.category.resource_label,item.getResourceLabel()));
                    item.setResourceArea(dictionaryMapper.queryDesc(ResourceConstant.category.resource_area,item.getResourceArea()));
                    item.setTradeType(dictionaryMapper.queryDesc(ResourceConstant.category.resource_trade,item.getTradeType()));
                    item.setIssureStatus(getCheckStatus(Integer.parseInt(item.getIssureStatus())));
                    item.setTopStatus(getTopStatus(Integer.parseInt(item.getTopStatus())));
                    return item;
                }).collect(Collectors.toList());
            }
            EasyExcel.write(response.getOutputStream(), QueryResourceManagerResponse.class).sheet("资源信息报表").doWrite(responseList);
        }catch (Exception e){
            log.error("reportDownLoad==>error",e);
        }
    }

    public PageResult<ReportResponse> setReportResponsePageResult(PageResult<ResourceInfo> pageResult, ReportRequest reportRequest) {
        PageResult<ReportResponse> responsePageResult = new PageResult<>();
        if (CollectionUtils.isEmpty(pageResult.getRows())) {
            responsePageResult.setRows(new ArrayList<>());
            responsePageResult.setTotal(pageResult.getTotal());
            return responsePageResult;
        }
        List<ReportResponse> responseList = setResponseList(pageResult);
        if (StringUtils.isEmpty(reportRequest.getResourceSort())) {
            responsePageResult.setRows(responseList);
            responsePageResult.setTotal(pageResult.getTotal());
            return responsePageResult;
        }
        if (org.apache.commons.lang.StringUtils.equals(reportRequest.getResourceSort(), ResourceConstant.sortType.brownUp)) {
            responseList.stream().sorted(Comparator.comparing(ReportResponse::getBrowseNum).reversed()).collect(Collectors.toList());
        } else if (org.apache.commons.lang.StringUtils.equals(reportRequest.getResourceSort(), ResourceConstant.sortType.shareUp)) {
            responseList.stream().sorted(Comparator.comparing(ReportResponse::getShareNum).reversed()).collect(Collectors.toList());
        } else if (org.apache.commons.lang.StringUtils.equals(reportRequest.getResourceSort(), ResourceConstant.sortType.phoneUp)){
            responseList.stream().sorted(Comparator.comparing(ReportResponse::getPhoneNum).reversed()).collect(Collectors.toList());
        }
        responsePageResult.setRows(responseList);
        responsePageResult.setTotal(pageResult.getTotal());
        return responsePageResult;
    }

    public List<ReportResponse> setResponseList(PageResult<ResourceInfo> pageResult) {
        List<ReportResponse> responseList = new ArrayList<>();
        for (ResourceInfo resourceInfo : pageResult.getRows()) {
            ReportResponse report = new ReportResponse();
            report.setBrowseNum(getBrowseNum(resourceInfo));
            report.setCreateTime(resourceInfo.getCreateTime());
            report.setIssurePhone(resourceInfo.getMobile());
            report.setIssureStatus(Integer.toString(resourceInfo.getStatus()));
            report.setIssureUserId(Long.toString(resourceInfo.getUserId()));
            report.setPhoneNum(getPhoneNum(resourceInfo));
            report.setResourceArea(Integer.toString(resourceInfo.getResourceArea()));
            report.setResourceId(Long.toString(resourceInfo.getId()));
            report.setResourceLabel(Integer.toString(resourceInfo.getResourceLabel()));
            report.setResourceTitle(resourceInfo.getTitle());
            report.setResourceType(Integer.toString(resourceInfo.getReleaseType()));
            report.setShareNum(getShareNum(resourceInfo));
            report.setTopStatus(Integer.toString(resourceInfo.getSticky()));
            report.setTradeType(Integer.toString(resourceInfo.getResourceTrade()));
            responseList.add(report);
        }
        return responseList;
    }

    public ResourceInfo setResourceInfo(ReportRequest reportRequest) {
        ResourceInfo resourceInfo = new ResourceInfo();
        resourceInfo.setCreateStartTime(reportRequest.getCreateStartTime());
        resourceInfo.setCreateEndTime(reportRequest.getCreateEndTime());
        resourceInfo.setMobile(reportRequest.getIssurePhone());
        return resourceInfo;
    }
}
