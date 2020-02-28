package com.gy.resource.controller.rest;


import com.gy.resource.constant.ResourceConstant;
import com.gy.resource.entity.ResourceInfo;
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

import java.util.ArrayList;
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
        String userId = tokenService.getUserIdByToken(myFollowRequest.getToken(), channel_WX);
        if (StringUtils.isEmpty(userId)) {
            return RestResult.error("1000", "请重新登录");
        }
        try {
            myFollowRequest.setUserId(Long.valueOf(userId));
            myFollowRequest.setStart((myFollowRequest.getStart() - 1) * myFollowRequest.getLimit());
            List<MyFollowUserInfoResponse> reseult = myFollowService.queryMyFollowByUserId(myFollowRequest);//我的关注
            if (reseult.size() != 0) {// 我的关注最新资源
                DESWrapper desWrapper=new DESWrapper();
                String password = "9588028820109132570743325311898426347857298773549468758875018579537757772163084478873699447306034466200616411960574122434059469100235892702736860872901247123456";
                for (MyFollowUserInfoResponse myFollowUserInfoResponse : reseult) {
                    if (!String.valueOf(myFollowUserInfoResponse.getUserId()).isEmpty()) {
                        MyFollowPeopleResourceResponse myFollowPeopleResourceResponse = myFollowService.queryMyFollowResourceByUserId(myFollowUserInfoResponse.getUserId());
                       if(myFollowPeopleResourceResponse!=null){
                           myFollowPeopleResourceResponse.setContent(setResourceContentByLength(myFollowPeopleResourceResponse.getContent()));
                           if(StringUtils.isNotEmpty(myFollowPeopleResourceResponse.getAuditTime())){
                               myFollowPeopleResourceResponse.setAuditTime(myFollowPeopleResourceResponse.getAuditTime().substring(0,10));
                           }
                           try {
                               String mobile="";
                               mobile=desWrapper.decrypt(myFollowPeopleResourceResponse.getMobile(),password);
                               myFollowPeopleResourceResponse.setMobile(mobile);//解密手机号
                           } catch (Exception e) {
                              log.error("queryMyFollow========》",e);
                           }
                       }
                        myFollowUserInfoResponse.setNickname(getNickName(myFollowUserInfoResponse.getNickname()));
                        myFollowUserInfoResponse.setMyFollowPeopleResourceResponse(myFollowPeopleResourceResponse);
                    }
                }
            }
                Integer total = myFollowService.queryMyFollowTotal(Long.valueOf(userId));
                MyFollowResponse myFollowResponse = new MyFollowResponse();
                myFollowResponse.setMyFollowUserInfoResponses(reseult);
                myFollowResponse.setTotal(total);
                return RestResult.success(myFollowResponse);
            } catch(Exception e){
               log.error("queryMyFollow========》",e);
                restResult = RestResult.error("9999", e.getLocalizedMessage());
            }
            return restResult;

    }

    public String setResourceContentByLength(String content){
        if(org.apache.commons.lang.StringUtils.isBlank(content)){
            return "";
        }
        //TODO 先写死50，具体也不知道截取多少
        if(content.length()<=50){
            return content;
        }
        return content.substring(0,50);
    }
    public String getNickName(String nickName) {
        //2020-02-24 客户提需求去掉脱敏
//        if (org.springframework.util.StringUtils.isEmpty(nickName)) {
//            return nickName;
//        }
//        if (nickName.length() == 1) {
//            return nickName;
//        }
//        if (nickName.length() == 2) {
//            return nickName.substring(0, 1)+"*";
//        }
//        if (nickName.length() > 2) {
//            StringBuffer sb=new StringBuffer();
//            sb.append(nickName.charAt(0));
//            for(int i=0;i<nickName.length()-2;i++){
//                sb.append("*");
//            }
//            sb.append(nickName.charAt(nickName.length()-1));
//            return sb.toString();
//        }
        return nickName;
    }
}
