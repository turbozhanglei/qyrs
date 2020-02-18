package com.gy.resource.controller.rest;


import com.gy.resource.request.rest.MyBrowseRequest;
import com.gy.resource.request.rest.MyFollowRequest;
import com.gy.resource.response.rest.MyBrowseResponse;
import com.gy.resource.response.rest.MyFollowPeopleResourceResponse;
import com.gy.resource.response.rest.MyFollowResponse;
import com.gy.resource.response.rest.MyFollowUserInfoResponse;
import com.gy.resource.service.MyBrowesService;
import com.gy.resource.service.MyFollowService;
import com.gy.resource.utils.DESWrapper;
import com.jic.common.base.vo.RestResult;
import com.jic.common.redis.RedisClientTemplate;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: zhuxiankun
 * @date: 2020-02-14
 * @remark:
 */
@RestController
@RequestMapping("/myBrowse")
@Api(tags = {"我的浏览记录接口"})
@Slf4j
public class MyBrowseController {
    @Autowired
    RedisClientTemplate redisClientTemplate;
    @Autowired
    MyBrowesService myBrowesService;
    /*
     *
     *查询我的浏览记录
     *
     * */
    @ResponseBody
    @RequestMapping(value = "/queryMyFollow")
    public RestResult<MyFollowResponse> queryMyFollow(@RequestBody MyBrowseRequest  myBrowseRequest) {
        RestResult restResult = new RestResult<>();
        log.info("------查询我的浏览记录,req{}-----", myBrowseRequest);
        // 获取用户id
        String userId =redisClientTemplate.get("H5_LOGIN_TOKEN_" + myBrowseRequest.getToken());
        if (StringUtils.isEmpty(userId)){
            return RestResult.error("4000","非法请求");
        };
        try {
            List<MyBrowseResponse> reseult=myBrowesService.queryMyBrowesByUserId(Long.valueOf(userId));
            MyFollowResponse myFollowResponse=new MyFollowResponse();


           return RestResult.success(myFollowResponse);
        } catch (Exception e) {
            e.printStackTrace();
            restResult = RestResult.error("9999", e.getLocalizedMessage());
        }
        return restResult;

    }





}
