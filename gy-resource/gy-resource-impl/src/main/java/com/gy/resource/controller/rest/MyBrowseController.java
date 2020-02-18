package com.gy.resource.controller.rest;


import com.gy.resource.request.rest.MyBrowseRequest;
import com.gy.resource.request.rest.MyFollowRequest;
import com.gy.resource.request.rest.SearchHistoryRequest;
import com.gy.resource.response.rest.*;
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

import java.util.*;

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
    @RequestMapping(value = "/queryMyBrowse")
    public RestResult<MyBrowseGroupByDateResponse> queryMyBrowse(@RequestBody MyBrowseRequest  myBrowseRequest) {
        RestResult restResult = new RestResult<>();
        log.info("------查询我的浏览记录,req{}-----", myBrowseRequest);
//         获取用户id
//        String userId =redisClientTemplate.get("H5_LOGIN_TOKEN_" + myBrowseRequest.getToken());
//        if (StringUtils.isEmpty(userId)){
//            return RestResult.error("4000","非法请求");
//        };
        try {
            List<MyBrowseResponse> reseult=myBrowesService.queryMyBrowesByUserId(Long.valueOf(2));
            Map<String,List<MyBrowseResponse>> myBrowseResponseList=new HashMap<>();
            MyBrowseGroupByDateResponse myBrowseGroupByDateResponse=new MyBrowseGroupByDateResponse();
            //数据组装
          if(reseult.size()!=0){
              Set<String> keys=new HashSet<String>();
              int index=0;
              for(MyBrowseResponse myBrowseResponse:reseult){
                  keys.add(myBrowseResponse.getCreateTime().substring(0,10));
              }
              for (String key:keys){
                  List<MyBrowseResponse> myBrowseResponses=new ArrayList<>();
                  for(MyBrowseResponse myBrowseResponse:reseult){
                      if(key.equals(myBrowseResponse.getCreateTime().substring(0,10))){
                          myBrowseResponses.add(myBrowseResponse);
                          myBrowseResponseList.put(key,myBrowseResponses);
                      }
                  }
              }
              myBrowseGroupByDateResponse.setMyBrowseResponseList(myBrowseResponseList);
              return RestResult.success(myBrowseGroupByDateResponse);
          }else{
              return RestResult.error("0000","暂无搜索记录");
          }

        } catch (Exception e) {
            e.printStackTrace();
            restResult = RestResult.error("9999", e.getLocalizedMessage());
        }
        return restResult;

    }

    /*
     *
     *删除浏览记录
     *
     * */
    @ResponseBody
    @RequestMapping(value = "/deleteMyBrowse")
    public RestResult deleteSearchHistoryByUserId(@RequestBody MyBrowseRequest myBrowseRequest) {
        log.info("------删除浏览记录,req{}-----", myBrowseRequest);
        RestResult restResult = new RestResult<>();
        // 获取用户id
//        String userId =redisClientTemplate.get("H5_LOGIN_TOKEN_" + searchHistoryRequest.getToken());
//        if (StringUtils.isEmpty(userId)){
//            return RestResult.error("4000","非法请求");
//        };
        try {
            myBrowesService.myBrowesDelete(Long.valueOf(2));
            return RestResult.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            restResult = RestResult.error("9999", e.getLocalizedMessage());
        }
        return restResult;

    }



}
