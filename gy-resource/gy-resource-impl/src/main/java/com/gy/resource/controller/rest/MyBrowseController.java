package com.gy.resource.controller.rest;


import com.gy.resource.constant.ResourceConstant;
import com.gy.resource.request.rest.MyBrowseRequest;
import com.gy.resource.request.rest.MyFollowRequest;
import com.gy.resource.request.rest.SearchHistoryRequest;
import com.gy.resource.response.rest.*;
import com.gy.resource.service.MyBrowesService;
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
    private static final String channel_WX= ResourceConstant.channel.WX;
    @Autowired
    RedisClientTemplate redisClientTemplate;
    @Autowired
    MyBrowesService myBrowesService;
    @Autowired
    TokenService tokenService;
    /*
     *
     *查询我的浏览记录
     *
     * */
    @ResponseBody
    @RequestMapping(value = "/queryMyBrowse")
    public RestResult<MyBrowseGroupByDateResponse> queryMyBrowse(@RequestBody MyBrowseRequest  myBrowseRequest) {
        DESWrapper desWrapper=new DESWrapper();
        String password = "9588028820109132570743325311898426347857298773549468758875018579537757772163084478873699447306034466200616411960574122434059469100235892702736860872901247123456";
        RestResult restResult = new RestResult<>();
        log.info("------查询我的浏览记录,req{}-----", myBrowseRequest);
        String userId = tokenService.getUserIdByToken(myBrowseRequest.getToken(),channel_WX);
        if (StringUtils.isEmpty(userId)){
            return RestResult.error("1000","请重新登录");
        };
        try {
            List<MyBrowseResponse> reseult=myBrowesService.queryMyBrowesByUserId(Long.valueOf(userId));
            Map<String,List<MyBrowseResponse>> myBrowseResponseList=new HashMap<String,List<MyBrowseResponse>>();
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
                          if(StringUtils.isNotEmpty(myBrowseResponse.getMobile())){
                              try {
                                  String mobile="";
                                  mobile=desWrapper.decrypt(myBrowseResponse.getMobile(),password);
                                  myBrowseResponse.setMobile(mobile);//解密手机号);
                              } catch (Exception e) {
                                  log.error("queryMyBrowse========》",e);
                              }
                          }
                          myBrowseResponse.setNickname(getNickName(myBrowseResponse.getNickname()));
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
            log.error("queryMyBrowse========》",e);
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
        String userId = tokenService.getUserIdByToken(myBrowseRequest.getToken(),channel_WX);
        if (StringUtils.isEmpty(userId)){
            return RestResult.error("1000","请重新登录");
        };
        try {
            myBrowesService.myBrowesDelete(Long.valueOf(userId));
            return RestResult.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            restResult = RestResult.error("9999", e.getLocalizedMessage());
        }
        return restResult;

    }

    public String getNickName(String nickName) {
        if (org.springframework.util.StringUtils.isEmpty(nickName)) {
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

}
