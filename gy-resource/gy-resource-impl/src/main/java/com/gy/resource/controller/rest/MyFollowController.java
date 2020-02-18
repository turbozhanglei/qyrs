package com.gy.resource.controller.rest;


import com.gy.resource.constant.ResourceConstant;
import com.gy.resource.request.rest.MyFollowRequest;

import com.gy.resource.response.rest.MyFollowPeopleResourceResponse;
import com.gy.resource.response.rest.MyFollowResponse;
import com.gy.resource.response.rest.MyFollowUserInfoResponse;
import com.gy.resource.service.MyFollowService;
import com.gy.resource.service.TokenService;
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
@RequestMapping("/myFollow")
@Api(tags = {"我的关注接口"})
@Slf4j
public class MyFollowController {
    private static final String channel_WX= ResourceConstant.channel.WX;
    @Autowired
    RedisClientTemplate redisClientTemplate;
    @Autowired
    MyFollowService myFollowService;
    @Autowired
    TokenService tokenService;
    /*
     *
     *我的关注
     *
     * */
    @ResponseBody
    @RequestMapping(value = "/queryMyFollow")
    public RestResult<MyFollowResponse> queryMyFollow(@RequestBody MyFollowRequest myFollowRequest) {
        RestResult restResult = new RestResult<>();
        log.info("------查询我的关注,req{}-----", myFollowRequest);
        String userId = tokenService.getUserIdByToken(myFollowRequest.getToken(),channel_WX);
        if (StringUtils.isEmpty(userId)){
            return RestResult.error("1000","请重新登录");
        };
        try {
            myFollowRequest.setUserId(Long.valueOf(userId));
            myFollowRequest.setStart((myFollowRequest.getStart()-1) * myFollowRequest.getLimit());
            List<MyFollowUserInfoResponse> reseult=myFollowService.queryMyFollowByUserId(myFollowRequest);
            Integer total=myFollowService.queryMyFollowTotal(Long.valueOf(userId));
            MyFollowResponse myFollowResponse=new MyFollowResponse();
            myFollowResponse.setMyFollowUserInfoResponses(reseult);
            myFollowResponse.setTotal(total);
           return RestResult.success(myFollowResponse);
        } catch (Exception e) {
            e.printStackTrace();
            restResult = RestResult.error("9999", e.getLocalizedMessage());
        }
        return restResult;

    }


    /*
     *
     *我的关注人的一条最新资源
     *
     * */
    @ResponseBody
    @RequestMapping(value = "/queryMyFollowResourceByUserId")
    public RestResult queryMyFollowResourceByUserId(@RequestParam Long userId) {
        DESWrapper desWrapper=new DESWrapper();
        RestResult restResult = new RestResult<>();
        log.info("------查询我的关注人最新的一篇文章,req{}-----", userId);
        String password = "9588028820109132570743325311898426347857298773549468758875018579537757772163084478873699447306034466200616411960574122434059469100235892702736860872901247123456";
        try {
            MyFollowPeopleResourceResponse reseult=myFollowService.queryMyFollowResourceByUserId(userId);
          reseult.setMobile(desWrapper.decrypt(reseult.getMobile(),password));//解密手机号
            return RestResult.success(reseult);
        } catch (Exception e) {
            e.printStackTrace();
            restResult = RestResult.error("9999", e.getLocalizedMessage());
        }
        return restResult;

    }




}
