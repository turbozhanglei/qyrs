package com.gy.resource.controller.rest;


import com.gy.resource.constant.ResourceConstant;
import com.gy.resource.request.rest.MyBrowseRequest;
import com.gy.resource.request.rest.MyFollowRequest;
import com.gy.resource.request.rest.SearchHistoryRequest;
import com.gy.resource.response.rest.*;
import com.gy.resource.service.MyBrowesService;
import com.gy.resource.service.MyFollowService;
import com.gy.resource.service.TokenService;
import com.gy.resource.service.UserSerivice;
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
    @Autowired
    UserSerivice userSerivice;
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
            Map<String,List<MyBrowseResponse>> myBrowseResponseList=new LinkedHashMap<String,List<MyBrowseResponse>>();
            MyBrowseGroupByDateResponse myBrowseGroupByDateResponse=new MyBrowseGroupByDateResponse();
            //数据组装
          if(reseult.size()!=0){
              List<String> list=new ArrayList<>();
              for(MyBrowseResponse myBrowseResponse:reseult){
                  list.add(myBrowseResponse.getCreateTime().substring(0,10));
              }
              //list去重
              List<String> keys=new ArrayList<>();
              int  index=0;
//              keys.add(list.get(0));
              for(String str:list){
                  if(keys.toString().indexOf(list.get(index))==-1){
                      keys.add(list.get(index));
                  }
                  index++;
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
                          if(StringUtils.isNotEmpty(myBrowseResponse.getAuditTime())){
                              myBrowseResponse.setAuditTime(myBrowseResponse.getAuditTime().substring(0,10));
                          }
                          myBrowseResponse.setNickname(getNickName(myBrowseResponse.getNickname()));
                          String phoneSwitch=userSerivice.queryPhoneSwitchByUserId(myBrowseResponse.getUserId());
                          myBrowseResponse.setPhoneSwitch(phoneSwitch);
                          myBrowseResponse.setResourceContent(setResourceContentByLength(myBrowseResponse.getResourceContent()));
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
        //2020-02-24 客户提需求去掉脱敏，后期将复用服务提取出来共用
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
