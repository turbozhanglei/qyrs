package com.gy.resource.controller.rest;

import com.gy.resource.api.rest.ResourceApi;
import com.gy.resource.constant.ResourceConstant;
import com.gy.resource.entity.AssociationalWordModel;
import com.gy.resource.entity.DictionaryCodeModel;
import com.gy.resource.entity.GlobalCorrelationModel;
import com.gy.resource.enums.DeleteFlagEnum;
import com.gy.resource.enums.FollowTypeEnum;
import com.gy.resource.enums.RefTypeEnum;
import com.gy.resource.request.rest.AddCorrelationRequest;
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
import com.gy.resource.service.TokenService;
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

import java.util.Date;
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
    private static final String channel_WX= ResourceConstant.channel.WX;

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

    @Autowired
    TokenService tokenService;

    @ApiOperation(value = "发布资源api，返回资源id")
    @PostMapping(value = "/issure-resource")
    public RestResult<String> issureResourceApi(@RequestBody IssureResourceRequest resourceRequest) {
        String userId = tokenService.getUserIdByToken(resourceRequest.getToken(),channel_WX);
        if (StringUtils.isBlank(userId)) {
            return RestResult.error("1000", "请重新登录");
        }
        resourceRequest.setIssureId(userId);
        return resourceInfoService.issureResourceApi(resourceRequest);
    }

    @ApiOperation(value = "查询资源详情包括内容")
    @PostMapping(value = "/query-resource-detail")
    public RestResult<QueryResourceResponse> queryResource(@RequestBody QueryResourceRequest resourceRequest) {
        String userId = tokenService.getUserIdByToken(resourceRequest.getToken(),channel_WX);
        if (StringUtils.isBlank(userId)) {
            return RestResult.error("1000", "请重新登录");
        }
        resourceRequest.setLoginUserId(userId);
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
        String userId = tokenService.getUserIdByToken(request.getToken(),channel_WX);
        if (StringUtils.isBlank(userId)) {
            return RestResult.error("1000", "请重新登录");
        }
        request.setLoginUserId(userId);
        return resourceInfoService.queryResourceByUserId(request);
    }

    @ApiOperation(value = "查询搜索模糊匹配列表")
    @PostMapping(value = "/query-words")
    @Override
    public RestResult<List<QueryWordsResponse>> queryWords(@RequestBody QueryWordsRequest req) {
        log.info("------进入查询搜索模糊匹配列表,req{}-----", req);
        List<AssociationalWordModel> associationalWordModels =
                pAssociationalWordService.associationalWordFuzzyWordQuery(req.getTitle());
        List<QueryWordsResponse> qwrList = ListUtils.entityListToModelList(
                associationalWordModels, QueryWordsResponse.class);
        if(qwrList != null && !qwrList.isEmpty()){
            for (QueryWordsResponse queryWordsResponse : qwrList) {
                Map dicQueryMap = new HashMap(8);
                dicQueryMap.put("code", queryWordsResponse.getCode());
                dicQueryMap.put("category", "resource_label");
                DictionaryCodeModel dictionaryCodeModel = pDictionaryCodeService.dictionaryCodeQuery(dicQueryMap);
                queryWordsResponse.setCodeName(dictionaryCodeModel.getDesc());
            }
        }

        return RestResult.success(qwrList);
    }

    @ApiOperation(value = "关注 取消关注 发布资源的用户 (点赞)")
    @PostMapping(value = "/follow-ref")
    @Override
    public RestResult<Boolean> followRef(@RequestBody FollowRefRequest req) {
        log.info("-----关注 取消关注 发布资源的用户 (点赞){}-----", req);

        // 获取用户id
//        String userStr = redisClientTemplate.get(UserLoginTokenPrefix.LOGIN_H5 + req.getToken());
//        if (StringUtils.isEmpty(userStr)) {
//            return RestResult.error("4000", "非法请求");
//        }
//        Map<String,Object> userMap = JSONArray.parseObject(userStr, HashMap.class);
//        Long userId = Long.valueOf(userMap.get("id").toString());

        Long userId = 2L;
        Map map = new HashMap(8);
        map.put("userId", userId);
        map.put("refId", req.getRefId());
        map.put("refType", req.getRefType());


        // 通过用户id，资源id，与资源类型，查询该实例是否存在，
        // ps: 这里不指定deleteFlag，是因为其充当了业务逻辑，
        // 且表中存在唯一键uq_userId_refId_refType_delete
        // 在关注或者点赞的时候，很容易赞成唯一键冲突。
        GlobalCorrelationModel globalCorrelationModel =
                pGlobalCorrelationService.globalCorrelationQuery(map);
        //如果是关注  或者  点赞   类型
        if (FollowTypeEnum.FOLLOW.getCode().equals(req.getFollowType())) {
            // 如果是不存在记录，则直接添加此记录
            if (globalCorrelationModel == null) {
                GlobalCorrelationModel model = new GlobalCorrelationModel();
                model.setUserId(userId);
                model.setRefId(req.getRefId());
                model.setRefType(req.getRefType());
                pGlobalCorrelationService.globalCorrelationAdd(model);

            }
            // 如果存在记录，判断此记录是否是 delete 状态，即取消关注或者取消点赞状态。
            // 此时需要修改deleteFlag状态从1改为0
            else if (globalCorrelationModel != null &&
                    DeleteFlagEnum.DELETE.getCode().equals(globalCorrelationModel.getDeleteFlag())) {
                GlobalCorrelationModel modelValue = new GlobalCorrelationModel();
                // 设置删除状态 为未删除状态
                modelValue.setDeleteFlag(DeleteFlagEnum.UN_DELETE.getCode());
                GlobalCorrelationModel whereCondition = new GlobalCorrelationModel();
                whereCondition.setUserId(userId);
                whereCondition.setRefId(req.getRefId());
                whereCondition.setRefType(req.getRefType());
                pGlobalCorrelationService.globalCorrelationEdit(modelValue, whereCondition);
            }
        }
        //如果是取消关注  或者  取消点赞   类型
        else {
            // 如果是不存在记录，则直接添加此记录，并且指定deleteFlag删除状态为 删除
            if (globalCorrelationModel == null) {
                GlobalCorrelationModel model = new GlobalCorrelationModel();
                model.setUserId(userId);
                model.setRefId(req.getRefId());
                model.setRefType(req.getRefType());
                model.setDeleteFlag(DeleteFlagEnum.DELETE.getCode());
                pGlobalCorrelationService.globalCorrelationAdd(model);

            }
            // 如果存在记录，判断此记录是否是 un_delete 状态，即关注或者点赞状态。
            // 此时需要修改deleteFlag状态从0改为1
            else if (globalCorrelationModel != null &&
                    DeleteFlagEnum.UN_DELETE.getCode().equals(globalCorrelationModel.getDeleteFlag())) {
                GlobalCorrelationModel modelValue = new GlobalCorrelationModel();
                // 设置删除状态 为删除状态
                modelValue.setDeleteFlag(DeleteFlagEnum.DELETE.getCode());
                GlobalCorrelationModel whereCondition = new GlobalCorrelationModel();
                whereCondition.setUserId(userId);
                whereCondition.setRefId(req.getRefId());
                whereCondition.setRefType(req.getRefType());
                pGlobalCorrelationService.globalCorrelationEdit(modelValue, whereCondition);
            }
        }
        return RestResult.success(Boolean.TRUE);
    }

    @ApiOperation(value = "查询当前用户是否关注了发布资源用户")
    @PostMapping(value = "/query-follow-status")
    @Override
    public RestResult<Boolean> queryFollowStatus(@RequestBody QueryFollowStatusRequest req) {
        log.info("------进入查询当前用户是否关注了发布资源用户,req{}-----", req);
        Map map = new HashMap(8);

        Long userId = 2L;
        map.put("userId", userId);
        map.put("refId", req.getRefId());
        map.put("refType", req.getRefType());
        map.put("deleteFlag", DeleteFlagEnum.UN_DELETE.getCode());
        GlobalCorrelationModel model = pGlobalCorrelationService.globalCorrelationQuery(map);
        if (model != null) {
            return RestResult.success(Boolean.TRUE);
        }
        return RestResult.success(Boolean.FALSE);
    }

    @ApiOperation(value = "查询关注我的人数量 或者 点赞某个资讯的数量")
    @PostMapping(value = "/query-follow-count")
    @Override
    public RestResult<Integer> queryFollowCount(QueryFollowCountRequest req) {
        log.info("------进入查询关注我的(资源)人数量,req{}-----", req);
        GlobalCorrelationModel model = new GlobalCorrelationModel();
        // TODO 如果用户 token 与 refId 同时为空，则数据校验不通过

        Long userId = 2L;
        //如果是关注用户类型，则refId 为用户 id，是用 token 转成的
        if (RefTypeEnum.FOLLOW_USER.getCode().equals(req.getRefType())) {
            model.setRefId(userId);
        }
        //如果是资讯点赞类型，则refId 为资讯 id，是取自 refType 的
        else if (RefTypeEnum.INFO_FOLLOW.getCode().equals(req.getRefType())) {
            model.setRefId(req.getRefId());
        }

        model.setRefType(req.getRefType());
        model.setDeleteFlag(DeleteFlagEnum.UN_DELETE.getCode());
        Integer count = pGlobalCorrelationService.globalCorrelationQueryCount(model);
        return RestResult.success(count);
    }

    /**
     * 页面逻辑：点击页面右下角清空按钮可直接清空浏览记录； 浏览记录根据浏览日期分组排序，根据用户浏览的时间倒序排序， 数据记录采用更新模式，例如用户10-01浏览了A信息，10-03又浏览了A信息，
     * 则浏览记录中A信息排在10-03下面，页面初始为10条记录，向下滑动加载下一页，每页10条
     */
    @ApiOperation(value = "记录浏览记录(单独处理)、分享记录、拨打电话记录")
    @PostMapping(value = "/add-correlation")
    @Override
    public RestResult<Boolean> addCorrelation(@RequestBody AddCorrelationRequest req) {
        log.info("------记录浏览记录(单独处理)、分享记录、拨打电话记录、点赞, req{}-----", req);
        Long userId = 2L;
        Map map = new HashMap(8);
        map.put("userId", userId);
        map.put("refId", req.getRefId());
        map.put("refType", req.getRefType());
        GlobalCorrelationModel dbModel =
                pGlobalCorrelationService.globalCorrelationQuery(map);
        if (dbModel == null) {
            GlobalCorrelationModel model = new GlobalCorrelationModel();
            model.setUserId(userId);
            model.setRefId(req.getRefId());
            model.setRefType(req.getRefType());
            pGlobalCorrelationService.globalCorrelationAdd(model);
        }
        // 记录浏览记录的逻辑需要 单拎出来
        if (RefTypeEnum.SOURCE_BROWSE.getCode().equals(req.getRefType())) {
            if (dbModel != null) {
                GlobalCorrelationModel modifyEntity = new GlobalCorrelationModel();
                modifyEntity.setUpdateTime(new Date());
                GlobalCorrelationModel whereCondition = new GlobalCorrelationModel();
                whereCondition.setUserId(userId);
                whereCondition.setRefId(req.getRefId());
                whereCondition.setRefType(req.getRefType());
                pGlobalCorrelationService.globalCorrelationEdit(modifyEntity, whereCondition);
            }
        }
        return RestResult.success(Boolean.TRUE);
    }
}
