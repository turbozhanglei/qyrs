package com.gy.resource.controller.rest;

import com.gy.resource.api.rest.ResourceApi;
import com.gy.resource.entity.AssociationalWordModel;
import com.gy.resource.entity.DictionaryCodeModel;
import com.gy.resource.entity.GlobalCorrelationModel;
import com.gy.resource.enums.DeleteFlagEnum;
import com.gy.resource.enums.FollowTypeEnum;
import com.gy.resource.request.rest.FollowRefRequest;
import com.gy.resource.request.rest.IssureResourceRequest;
import com.gy.resource.request.rest.QueryFollowCountRequest;
import com.gy.resource.request.rest.QueryFollowStatusRequest;
import com.gy.resource.request.rest.QueryResourceByConditionRequest;
import com.gy.resource.request.rest.QueryResourceRequest;
import com.gy.resource.request.rest.QueryWordsRequest;
import com.gy.resource.request.rest.UserRequest;
import com.gy.resource.response.rest.QueryResourceByConditionResponse;
import com.gy.resource.response.rest.QueryResourceByUserIdResponse;
import com.gy.resource.response.rest.QueryResourceResponse;
import com.gy.resource.response.rest.QueryWordsResponse;
import com.gy.resource.response.rest.RecommendResourceResponse;
import com.gy.resource.service.PAssociationalWordService;
import com.gy.resource.service.PDictionaryCodeService;
import com.gy.resource.service.PGlobalCorrelationService;
import com.gy.resource.service.ResourceInfoService;
import com.gy.resource.utils.ListUtils;
import com.jic.common.base.vo.PageResult;
import com.jic.common.base.vo.RestResult;
import com.jic.common.redis.RedisClientTemplate;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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

    @Resource
    PAssociationalWordService pAssociationalWordService;

    @Resource
    PDictionaryCodeService pDictionaryCodeService;

    @Resource
    PGlobalCorrelationService pGlobalCorrelationService;

    @Autowired
    RedisClientTemplate redisClientTemplate;


    @Autowired
    ResourceInfoService resourceInfoService;

    @ApiOperation(value = "发布资源api，返回资源id")
    @PostMapping(value = "/issure-resource")
    public RestResult<String> issureResourceApi(@RequestBody IssureResourceRequest resourceRequest) {
        return resourceInfoService.issureResourceApi(resourceRequest);
    }

    @ApiOperation(value = "查询资源详情包括内容")
    @PostMapping(value = "/query-resource-detail")
    public RestResult<QueryResourceResponse> queryResource(@RequestBody QueryResourceRequest resourceRequest) {
        return resourceInfoService.queryResource(resourceRequest);
    }

    @ApiOperation(value = "首页推荐资源")
    @PostMapping(value = "/recommend-resource")
    public RestResult<List<RecommendResourceResponse>> recommendResource() {
        return resourceInfoService.recommendResource();
    }

    @ApiOperation(value = "根据筛选条件查询资源列表")
    @PostMapping(value = "/query-resource-condition")
    public RestResult<List<QueryResourceByConditionResponse>> queryResourceByCondition(@RequestBody QueryResourceByConditionRequest resourceByConditionRequest) {
        return resourceInfoService.queryResourceByCondition(resourceByConditionRequest);
    }

    @ApiOperation(value = "查询用户发布的资源列表")
    @PostMapping(value = "/query-resource-user")
    public RestResult<PageResult<QueryResourceByUserIdResponse>> queryResourceByUserId(@RequestBody UserRequest request) {
        return resourceInfoService.queryResourceByUserId(request);
    }

    @ApiOperation(value = "查询搜索模糊匹配列表")
    @PostMapping(value = "/query-words")
    @Override
    public RestResult<List<QueryWordsResponse>> queryWords(QueryWordsRequest req) {
        log.info("------进入查询搜索模糊匹配列表,req{}-----", req);
        List<AssociationalWordModel> associationalWordModels =
                pAssociationalWordService.associationalWordFuzzyWordQuery(req.getTitle());
        List<QueryWordsResponse> qwrList = ListUtils.entityListToModelList(
                associationalWordModels, QueryWordsResponse.class);
        for (QueryWordsResponse queryWordsResponse : qwrList) {
            Map dicQueryMap = new HashMap(8);
            dicQueryMap.put("code", queryWordsResponse.getCode());
            dicQueryMap.put("category", "resource_label");
            DictionaryCodeModel dictionaryCodeModel = pDictionaryCodeService.dictionaryCodeQuery(dicQueryMap);
            queryWordsResponse.setCodeName(dictionaryCodeModel.getDesc());
        }
        return RestResult.success(qwrList);
    }

    @ApiOperation(value = "关注 取消关注 发布资源的用户 (点赞)")
    @PostMapping(value = "/follow-ref")
    @Override
    public RestResult<Boolean> followRef(FollowRefRequest req) {
        log.info("-----关注 取消关注 发布资源的用户 (点赞){}-----", req);

        // 获取用户id
        String userStr = redisClientTemplate.get("H5_LOGIN_TOKEN_" + req.getToken());
        if (StringUtils.isEmpty(userStr)) {
            return RestResult.error("4000", "非法请求");
        }
        ;

        Map map = new HashMap(8);
//        map.put("userId",);
        map.put("refId", req.getRefId());
        map.put("refType", req.getRefType());
        GlobalCorrelationModel globalCorrelationModel =
                pGlobalCorrelationService.globalCorrelationQuery(map);
        //如果是关注
        if (FollowTypeEnum.FOLLOW.getCode().equals(req.getFollowType())) {
            // 如果是存在记录
            if (globalCorrelationModel != null) {
                GlobalCorrelationModel model = new GlobalCorrelationModel();
//                model.setUserId();
                model.setRefId(req.getRefId());
                model.setRefType(req.getRefType());
                pGlobalCorrelationService.globalCorrelationAdd(model);

            } else {

                GlobalCorrelationModel modelValue = new GlobalCorrelationModel();
                modelValue.setDeleteFlag(0);
                GlobalCorrelationModel whereCondition = new GlobalCorrelationModel();
                //model.setUserId();
                whereCondition.setRefId(req.getRefId());
                whereCondition.setRefType(req.getRefType());
                pGlobalCorrelationService.globalCorrelationEdit(modelValue, whereCondition);
            }
        } else {
            GlobalCorrelationModel modelValue = new GlobalCorrelationModel();
            modelValue.setDeleteFlag(1);
            GlobalCorrelationModel whereCondition = new GlobalCorrelationModel();
            //model.setUserId();
            whereCondition.setRefId(req.getRefId());
            whereCondition.setRefType(req.getRefType());
            pGlobalCorrelationService.globalCorrelationEdit(modelValue, whereCondition);
        }

        return RestResult.success(Boolean.TRUE);
    }

    @ApiOperation(value = "查询当前用户是否关注了发布资源用户(资源)")
    @PostMapping(value = "/query-follow-status")
    @Override
    public RestResult<Boolean> queryFollowStatus(QueryFollowStatusRequest req) {
        log.info("------进入查询搜索模糊匹配列表,req{}-----", req);
        Map map = new HashMap(8);
//        map.put("userId",);
        map.put("refId", req.getRefId());
        map.put("refType", req.getRefType());
        map.put("deleteFlag", DeleteFlagEnum.UN_DELETE.getCode());
        GlobalCorrelationModel model = pGlobalCorrelationService.globalCorrelationQuery(map);
        if (model != null) {
            RestResult.success(Boolean.TRUE);
        }
        return RestResult.success(Boolean.FALSE);
    }

    @ApiOperation(value = "查询关注我的(资源)人数量")
    @PostMapping(value = "/query-follow-count")
    @Override
    public RestResult<Integer> queryFollowCount(QueryFollowCountRequest req) {
        log.info("------进入查询关注我的(资源)人数量,req{}-----", req);
        GlobalCorrelationModel model = new GlobalCorrelationModel();
//        model.setUserId();
        model.setRefType(req.getRefType());
        model.setDeleteFlag(DeleteFlagEnum.UN_DELETE.getCode());
        Integer count = pGlobalCorrelationService.globalCorrelationQueryCount(model);
        return RestResult.success(count);
    }

    @Override
    public RestResult<Boolean> addCorrelation(QueryFollowCountRequest req) {

        Map map = new HashMap(8);
//        map.put("userId",);
        map.put("refId", req.getRefId());
        map.put("refType", req.getRefType());
        GlobalCorrelationModel dbModel =
                pGlobalCorrelationService.globalCorrelationQuery(map);
        if (dbModel == null) {
            GlobalCorrelationModel model = new GlobalCorrelationModel();
//            model.setUserId();
            model.setRefId(req.getRefId());
            model.setRefType(req.getRefType());
            pGlobalCorrelationService.globalCorrelationAdd(model);
        }
        return RestResult.success(Boolean.TRUE);
    }
}
