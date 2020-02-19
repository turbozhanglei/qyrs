package com.gy.resource.service.impl;

import com.gy.resource.constant.ResourceConstant;
import com.gy.resource.entity.GlobalCorrelationModel;
import com.gy.resource.entity.ResourceInfo;
import com.gy.resource.mapper.ResourceInfoMapper;
import com.gy.resource.request.rest.IssureResourceRequest;
import com.gy.resource.request.rest.QueryResourceByConditionRequest;
import com.gy.resource.request.rest.QueryResourceRequest;
import com.gy.resource.request.rest.UserRequest;
import com.gy.resource.response.rest.QueryResourceByConditionResponse;
import com.gy.resource.response.rest.QueryResourceByUserIdResponse;
import com.gy.resource.response.rest.QueryResourceResponse;
import com.gy.resource.response.rest.RecommendResourceResponse;
import com.gy.resource.service.PGlobalCorrelationService;
import com.gy.resource.service.ResourceInfoService;
import com.gy.resource.service.TokenService;
import com.jic.common.base.vo.Page;
import com.jic.common.base.vo.PageResult;
import com.jic.common.base.vo.RestResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * 资源信息
 *
 * @author : gaoly
 * @version : v1.0.0
 * @email : 774329481@qq.com
 * @since : 2020-02-14 11:08:42
 */
@Service
@Slf4j
public class ResourceInfoServiceImpl implements ResourceInfoService {

    @Autowired
    private ResourceInfoMapper resourceInfoMapper;

    @Autowired
    TokenService tokenService;

    @Resource
    PGlobalCorrelationService pGlobalCorrelationService;

    @Override
    public RestResult<String> issureResourceApi(IssureResourceRequest resourceRequest) {
        ResourceInfo resourceInfo = getResourceInfo(resourceRequest);
        insert(resourceInfo);
        return RestResult.success(Long.toString(resourceInfo.getId()));
    }

    @Override
    public RestResult<QueryResourceResponse> queryResource(QueryResourceRequest request) {
        browse(request);
        ResourceInfo resourceInfo = resourceInfoMapper.queryByPrimaryKey(Long.parseLong(request.getResourceId()));
        QueryResourceResponse response = new QueryResourceResponse();
        response.setIssureUserId(Long.toString(resourceInfo.getUserId()));
        response.setIssureHeadImage(getHeadImage(resourceInfo));
        response.setIssureNickName(getNickName(resourceInfo));
        response.setCompanyName(getCompany(resourceInfo));
        response.setResourceTitle(resourceInfo.getTitle());
        response.setIssureDate(resourceInfo.getAuditTime());
        response.setResourceType(resourceInfo.getReleaseType().toString());
        response.setResourceLabel(resourceInfo.getResourceLabel().toString());
        response.setResourceArea(resourceInfo.getResourceArea().toString());
        response.setTradeType(resourceInfo.getResourceTrade().toString());
        response.setResourceContent(resourceInfo.getContent());
        response.setBrowseNum(getBrowseNum(resourceInfo));
        response.setShareNum(getShareNum(resourceInfo));
        response.setIssurePhone(resourceInfo.getMobile());
        response.setShareLink("");
        response.setCreateTime(resourceInfo.getCreateTime());
        response.setIssureStatus(resourceInfo.getStatus().toString());
        response.setTopStatus(resourceInfo.getSticky().toString());
        response.setCheckAccount(resourceInfo.getAuditor());
        response.setCheckDate(resourceInfo.getAuditTime());
        return RestResult.success(response);
    }

    @Override
    public RestResult<PageResult<QueryResourceByUserIdResponse>> queryResourceByUserId(UserRequest request) {
        ResourceInfo resourceInfo = new ResourceInfo();
        resourceInfo.setUserId(Long.parseLong(request.getLoginUserId()));
        resourceInfo.setDeleteFlag(ResourceConstant.deleteFlag.no);
        Page page = setPage(request.getStart(), request.getLimit());
        PageResult<ResourceInfo> resourceInfoPageResult = queryPageOrderByAuditTime(resourceInfo, page);

        PageResult<QueryResourceByUserIdResponse> pageResult = new PageResult<>();
        pageResult.setTotal(resourceInfoPageResult.getTotal());

        List<ResourceInfo> resourceInfos = resourceInfoPageResult.getRows();
        if (CollectionUtils.isEmpty(resourceInfos)) {
            pageResult.setRows(new ArrayList<>());
            return RestResult.success(pageResult);
        }

        List<QueryResourceByUserIdResponse> responseList = new ArrayList<>();
        for (ResourceInfo param : resourceInfos) {
            QueryResourceByUserIdResponse response = new QueryResourceByUserIdResponse();
            response.setBrowseNum(getBrowseNum(param));
            response.setIssureDate(param.getAuditTime());
            response.setIssureStatus(param.getStatus().toString());
            response.setResourceId(Long.toString(param.getId()));
            response.setResourceTitle(param.getTitle());
            response.setShareNum(getShareNum(param));
            responseList.add(response);
        }
        pageResult.setRows(responseList);

        return RestResult.success(pageResult);
    }

    /**
     * 逻辑: 首页资源信息读取后台审核通过的十条资源信息，优先读取置顶的资源信息，不够十条则补充发布时间最近的资源信息， 排序根据发布时间由近到远排序。
     * 展示元素包括发布人头像和昵称、资源信息标题、发布时间、浏览量、拨打电话按钮。无头像展示默认头像； 昵称需要脱敏，脱敏规则为：显示首字母（中文字）和尾字母（中文字），
     * 其余用“”代替，例如“王心苑”，输出“王苑”、例如“happy”，输出“h*y”； 若用户名仅为2个字母（中文字），则末尾用“”代替，例如“桂鹏”，输出“桂”；
     * 若用户名仅为1个字母（中文字），则全部展示；资源信息标题一行展示不下打省略号； 发布时间取该信息最新的审核通过时间，只展示年月日；浏览量取用户进入信息详情页的次数，
     * 浏览量每个用户ID只记一次；点击拨打电话按钮跳转至拨打手机页面，号码为用户登录的手机号码， 使用该功能需提前登录。点击资源信息左侧区域跳转至对应资源信息详情页，需提前登录。
     */
    @Override
    public RestResult<List<RecommendResourceResponse>> recommendResource() {
        List<ResourceInfo> topList = getTopResource();
        if (!CollectionUtils.isEmpty(topList) && topList.size() == 10) {
            List<RecommendResourceResponse> responseList = getRecommendResourceResponse(topList);
            return RestResult.success(responseList);
        }

        if (CollectionUtils.isEmpty(topList)) {
            List<ResourceInfo> resourceInfos = getCheckSuccessList(10);
            if (CollectionUtils.isEmpty(resourceInfos)) {
                return RestResult.success(new ArrayList<>());
            }
            List<RecommendResourceResponse> responseList = getRecommendResourceResponse(resourceInfos);
            return RestResult.success(responseList);
        }

        if (!CollectionUtils.isEmpty(topList) && topList.size() < 10) {
            int limit = 10 - topList.size();
            List<ResourceInfo> resourceInfos = getCheckSuccessList(limit);
            if (CollectionUtils.isEmpty(resourceInfos)) {
                return RestResult.success(new ArrayList<>());
            }
            List<RecommendResourceResponse> responseList = getRecommendResourceResponse(resourceInfos);
            return RestResult.success(responseList);
        }

        return RestResult.success(new ArrayList<>());
    }

    @Override
    public RestResult<List<QueryResourceByConditionResponse>> queryResourceByCondition(QueryResourceByConditionRequest resourceByConditionRequest) {
        ResourceInfo param = setResourceInfo(resourceByConditionRequest);
        List<ResourceInfo> resultList = queryResourceInfoList(param,resourceByConditionRequest);
        if(CollectionUtils.isEmpty(resultList)){
            return RestResult.success(new ArrayList<>());
        }
        List<QueryResourceByConditionResponse> responseList=setQueryResourceByConditionResponseList(resultList);
        if (getValueByParam(resourceByConditionRequest.getBrowseUpNum()) == null &&
                getValueByParam(resourceByConditionRequest.getShareUpNum()) == null) {
            return RestResult.success(responseList);
        }

        Integer browseSortFlag=getValueByParam(resourceByConditionRequest.getBrowseUpNum());
        Integer shareSortFlag=getValueByParam(resourceByConditionRequest.getShareUpNum());
        if(browseSortFlag!=null&&browseSortFlag==ResourceConstant.brownSort.up){
            responseList.stream().sorted(Comparator.comparing(QueryResourceByConditionResponse::getBrowseNum)).collect(Collectors.toList());
        }else if(browseSortFlag!=null&&browseSortFlag==ResourceConstant.brownSort.down){
            responseList.stream().sorted(Comparator.comparing(QueryResourceByConditionResponse::getBrowseNum).reversed()).collect(Collectors.toList());
        }

        if(shareSortFlag!=null&&shareSortFlag==ResourceConstant.shareSort.up){
            responseList.stream().sorted(Comparator.comparing(QueryResourceByConditionResponse::getShareNum)).collect(Collectors.toList());
        }else if(shareSortFlag!=null&&shareSortFlag==ResourceConstant.shareSort.down){
            responseList.stream().sorted(Comparator.comparing(QueryResourceByConditionResponse::getShareNum).reversed()).collect(Collectors.toList());
        }


        return RestResult.success(responseList);
    }

    public List<ResourceInfo> queryResourceInfoList(ResourceInfo param,QueryResourceByConditionRequest request){
        return resourceInfoMapper.queryByCondition(param,
                getFieldList(request.getResourceType()),
                getFieldList(request.getResourceLabel()),
                getFieldList(request.getResourceArea()),
                getFieldList(request.getTradeType()));
    }

    public List<Integer> getFieldList(String field){
        if (org.apache.commons.lang.StringUtils.equals(field, "-1") || StringUtils.isEmpty(field)){
            return null;
        }
        if(field.contains(",")){
            String [] fieldArray=field.split(",");
            List<Integer> list=Stream.of(fieldArray).map(item-> Integer.parseInt(item)).collect(Collectors.toList());
            return list;
        }
        List<Integer> listOne=new ArrayList<>();
        listOne.add(Integer.parseInt(field));
        return listOne;
    }

    public List<QueryResourceByConditionResponse> setQueryResourceByConditionResponseList(List<ResourceInfo> resultList) {
        if (CollectionUtils.isEmpty(resultList)) {
            return new ArrayList<>();
        }
        List<QueryResourceByConditionResponse> responseList=new ArrayList<>();
        for (ResourceInfo resourceInfo : resultList) {
            QueryResourceByConditionResponse response=new QueryResourceByConditionResponse();
            response.setBrowseNum(getBrowseNum(resourceInfo));
            response.setShareNum(getShareNum(resourceInfo));
            response.setIssureDate(resourceInfo.getAuditTime());
            response.setIssureHeadImage(getHeadImage(resourceInfo));
            response.setIssureNickName(getNickName(resourceInfo));
            response.setIssurePhone(resourceInfo.getMobile());
            response.setIssureUserId(Long.toString(resourceInfo.getUserId()));
            response.setResourceId(Long.toString(resourceInfo.getId()));
            response.setResourceTitle(resourceInfo.getTitle());
            responseList.add(response);
        }
        return responseList;
    }

    public ResourceInfo setResourceInfo(QueryResourceByConditionRequest request) {
        ResourceInfo param = new ResourceInfo();
//        param.setReleaseType(getValueByParam(request.getResourceType()));
//        param.setResourceLabel(getValueByParam(request.getResourceLabel()));
//        param.setResourceArea(getValueByParam(request.getResourceArea()));
//        param.setResourceTrade(getValueByParam(request.getTradeType()));
        param.setTitle(getValue(request.getResourceTitle()));
        param.setUserId(getLongValue(request.getIssureUserId()));
        return param;
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

    public List<ResourceInfo> getCheckSuccessList(int limit) {
        return resourceInfoMapper.queryResourceCheckSuccess(limit);
    }

    public List<RecommendResourceResponse> getRecommendResourceResponse(List<ResourceInfo> topList) {
        List<RecommendResourceResponse> responseList = new ArrayList<>();
        for (ResourceInfo param : topList) {
            RecommendResourceResponse response = new RecommendResourceResponse();
            response.setBrowseNum(getBrowseNum(param));
            response.setIssureDate(param.getAuditTime());
            response.setIssureHeadImage(getHeadImage(param));
            response.setIssureId(Long.toString(param.getUserId()));
            response.setIssureNickName(getNickName(param));
            response.setIssurePhone(param.getMobile());
            response.setResourceId(Long.toString(param.getId()));
            response.setResourceTitle(param.getTitle());
            responseList.add(response);
        }
        return responseList;
    }

    public List<ResourceInfo> getTopResource() {
        return resourceInfoMapper.queryTopResourceTen();
    }

    public Page setPage(int start, int limit) {
        Page page = new Page();
        page.setStart(start);
        page.setLimit(limit);
        return page;
    }

    public String getShareNum(ResourceInfo resourceInfo) {
        GlobalCorrelationModel modelEntity=new GlobalCorrelationModel();
        modelEntity.setRefId(resourceInfo.getId());
        modelEntity.setRefType(ResourceConstant.refType.resource_share_num);
        Integer shareNum=pGlobalCorrelationService.globalCorrelationQueryCount(modelEntity);
        return Integer.toString(shareNum);
    }

    public String getBrowseNum(ResourceInfo resourceInfo) {
        GlobalCorrelationModel modelEntity=new GlobalCorrelationModel();
        modelEntity.setRefId(resourceInfo.getId());
        modelEntity.setRefType(ResourceConstant.refType.resource_brown_num);
        Integer browseNum=pGlobalCorrelationService.globalCorrelationQueryCount(modelEntity);
        return Integer.toString(browseNum);
    }

    public String getCompany(ResourceInfo resourceInfo) {
        String company = resourceInfoMapper.getCompanyName(resourceInfo.getUserId());
        if (StringUtils.isEmpty(company)) {
            return "";
        }
        return company;
    }

    public String getNickName(ResourceInfo resourceInfo) {
        String nickName = resourceInfoMapper.getNickName(resourceInfo.getUserId());
        if (StringUtils.isEmpty(nickName)) {
            return nickName;
        }
        if (nickName.length() == 1) {
            return nickName;
        }
        if (nickName.length() == 2) {
            return nickName.substring(0, 1)+"*";
        }
        if (nickName.length() > 2) {
            StringBuffer sb=new StringBuffer();
            sb.append(nickName.charAt(0));
            for(int i=0;i<nickName.length()-2;i++){
                sb.append("*");
            }
            sb.append(nickName.charAt(nickName.length()-1));
            return sb.toString();
        }
        return nickName;
    }

    public String getHeadImage(ResourceInfo resourceInfo) {
        String headImage = resourceInfoMapper.getHeadImage(resourceInfo.getUserId());
        if (StringUtils.isEmpty(headImage)) {
            //默认头像 TODO
            headImage = "";
        }
        return headImage;
    }

    public void browse(QueryResourceRequest request) {
        //TODO 记录浏览记录
        pGlobalCorrelationService.addBrowse(Long.parseLong(request.getLoginUserId()),
                Long.parseLong(request.getResourceId()),
                ResourceConstant.refType.resource_brown_num);
    }

    public ResourceInfo getResourceInfo(IssureResourceRequest request) {
        ResourceInfo resourceInfo = new ResourceInfo();
        resourceInfo.setUserId(Long.parseLong(request.getIssureId()));
        resourceInfo.setMobile(tokenService.decryptMobile(resourceInfoMapper.getMobile(resourceInfo.getUserId())));
        resourceInfo.setTitle(request.getResourceTitle());
        resourceInfo.setPlatform(ResourceConstant.platform.weixin);
        resourceInfo.setReleaseType(Integer.parseInt(request.getResourceType()));
        resourceInfo.setResourceLabel(Integer.parseInt(request.getResourceLabel()));
        resourceInfo.setResourceArea(Integer.parseInt(request.getResourceArea()));
        resourceInfo.setResourceTrade(Integer.parseInt(request.getTradeType()));
        resourceInfo.setStatus(ResourceConstant.check.check_init);
        resourceInfo.setSticky(ResourceConstant.top.down);
        resourceInfo.setSensitiveFlag(getSensitiveFlag(request));
        resourceInfo.setImageFlag(ResourceConstant.imageFlag.no);
        resourceInfo.setContent(request.getResourceContent());
        resourceInfo.setAuditor(null);
        resourceInfo.setAuditTime(null);
        resourceInfo.setDeleteFlag(ResourceConstant.deleteFlag.no);
        resourceInfo.setCreateTime(new Date());
        resourceInfo.setUpdateTime(new Date());
        return resourceInfo;
    }

    //TODO 调用敏感词接口判断是否包含敏感词
    public int getSensitiveFlag(IssureResourceRequest request) {
        return ResourceConstant.sensitive.no_contanins;
    }

    @Override
    public long insert(ResourceInfo resourceInfo) {
        long id = resourceInfoMapper.insert(resourceInfo);
        return id;
    }

    @Override
    public long delete(Long id) {
        return resourceInfoMapper.delete(id);
    }

    @Override
    public long update(ResourceInfo resourceInfo) {
        return resourceInfoMapper.update(resourceInfo);
    }

    @Override
    public ResourceInfo queryByPrimaryKey(Long id) {
        return resourceInfoMapper.queryByPrimaryKey(id);
    }

    @Override
    public List<ResourceInfo> query(ResourceInfo resourceInfo) {
        return resourceInfoMapper.query(resourceInfo);
    }

    @Override
    public PageResult<ResourceInfo> queryPageOrderByAuditTime(ResourceInfo resourceInfo, Page pageQuery) {
        //计算下标
        int startIndex = (pageQuery.getStart() - 1) * pageQuery.getLimit();
        List<ResourceInfo> list = resourceInfoMapper.queryPageOrderByAuditTime(startIndex,
                pageQuery.getLimit(),
                resourceInfo,
                resourceInfo.getCreateStartTime(),
                resourceInfo.getCreateEndTime());
        long count = resourceInfoMapper.queryPageCount(resourceInfo,
                resourceInfo.getCreateStartTime(),
                resourceInfo.getCreateEndTime());
        PageResult pageResult = new PageResult();
        pageResult.setRows(list);
        pageResult.setTotal(count);
        return pageResult;
    }
}