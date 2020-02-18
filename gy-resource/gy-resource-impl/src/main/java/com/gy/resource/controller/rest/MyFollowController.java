package com.gy.resource.controller.rest;

import com.gy.resource.entity.SearchHistoryModel;
import com.gy.resource.request.rest.MyFollowRequest;
import com.gy.resource.request.rest.SearchHistoryAddRequest;
import com.gy.resource.request.rest.SearchHistoryRequest;
import com.gy.resource.response.rest.MyFollowResponse;
import com.gy.resource.response.rest.MyFollowUserInfoResponse;
import com.gy.resource.response.rest.SearchHistoryResponse;
import com.gy.resource.service.MyFollowService;
import com.gy.resource.service.PSearchHistoryService;
import com.jic.common.base.vo.RestResult;
import com.jic.common.redis.RedisClientTemplate;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author: zhuxiankun
 * @date: 2020-02-14
 * @remark:
 */
@RestController
@RequestMapping("/myFollow")
@Api(tags = {"我的关注接口"})
@Slf4j
public class MyFollowController {
    @Autowired
    RedisClientTemplate redisClientTemplate;
    @Autowired
    MyFollowService myFollowService;
    /*
     *
     *我的关注
     *
     * */
    @ResponseBody
    @RequestMapping(value = "/queryMyFollow")
    public RestResult<MyFollowResponse> queryMyFollow(@RequestBody MyFollowRequest myFollowRequest) {
        RestResult restResult = new RestResult<>();
        // 获取用户id
        String userId =redisClientTemplate.get("H5_LOGIN_TOKEN_" + myFollowRequest.getToken());
        if (StringUtils.isEmpty(userId)){
            return RestResult.error("4000","非法请求");
        };
        try {
            List<MyFollowUserInfoResponse> reseult=myFollowService.queryMyFollowByUserId(Long.valueOf(userId));
            Integer total=myFollowService.queryMyFollowTotal(Long.valueOf(userId));
            MyFollowResponse myFollowResponse=new MyFollowResponse();
            myFollowResponse.setMyFollowUserInfoResponses(reseult);
            myFollowResponse.setTotal(total);
           return restResult.success(myFollowResponse);
        } catch (Exception e) {
            e.printStackTrace();
            restResult = RestResult.error("9999", e.getLocalizedMessage());
        }
        return restResult;

    }




}
