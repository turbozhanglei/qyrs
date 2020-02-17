package com.gy.resource.controller.rest;

import com.gy.resource.entity.SearchHistoryModel;
import com.gy.resource.request.rest.*;
import com.gy.resource.response.rest.SearchHistoryResponse;
import com.gy.resource.service.PSearchHistoryService;
import com.jic.common.base.vo.RestResult;
import com.jic.common.redis.RedisClientTemplate;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/searchHistoryRest")
@Api(tags = {"搜索记录接口"})
@Slf4j
public class SearchHistoryController {
    @Autowired
    RedisClientTemplate redisClientTemplate;

    @Autowired
    PSearchHistoryService pSearchHistoryService;
    /*
     *
     *新增搜索记录
     * */
    @ResponseBody
    @RequestMapping(value = "/addSearchHistory")
    public RestResult insertProductUnits(@RequestBody SearchHistoryAddRequest searchHistoryAddRequest) {
        RestResult restResult = new RestResult<>();
        // 获取用户id
        String userId =redisClientTemplate.get("H5_LOGIN_TOKEN_" + searchHistoryAddRequest.getToken());

        if (StringUtils.isEmpty(userId)){
            return RestResult.error("4000","非法请求");
        };
        try {

            if(StringUtils.isNotEmpty(searchHistoryAddRequest.getWord())){
                //搜索词去重
                String words[]=searchHistoryAddRequest.getWord().split(",");
                List<String> list = new ArrayList<>();
                list.add(words[0]);
                for(int i=1;i<words.length;i++){
                    if(list.toString().indexOf(words[i]) == -1){
                        list.add(words[i]);
                    }
                }

                for (String word:list){
                    SearchHistoryModel searchHistoryModel=new SearchHistoryModel();
                    searchHistoryModel.setWord(word);
                    searchHistoryModel.setUserId(Long.valueOf(userId));
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, 30);
                    Date validDate = cal.getTime();
                    SimpleDateFormat fDate=new SimpleDateFormat("yyyy-MM-dd");
                    searchHistoryModel.setValidTime(fDate.format(validDate));
                    pSearchHistoryService.searchHistoryAdd(searchHistoryModel);
                }
                   return  restResult.success(true);
            }else{
                return RestResult.error("4000","搜索词不能为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            restResult = RestResult.error("9999", e.getLocalizedMessage());
        }
        return restResult;

    }

    /*
     *
     *查询搜索历史记录
     *
     * */
    @ResponseBody
    @RequestMapping(value = "/querySearchHistoryByUserId")
    public RestResult<List<SearchHistoryResponse>> querySearchHistoryByUserId(@RequestBody SearchHistoryRequest searchHistoryRequest) {
        RestResult restResult = new RestResult<>();
        // 获取用户id
        String userId =redisClientTemplate.get("H5_LOGIN_TOKEN_" + searchHistoryRequest.getToken());
        if (StringUtils.isEmpty(userId)){
            return RestResult.error("4000","非法请求");
        };
        try {

           List<SearchHistoryResponse> reseult=pSearchHistoryService.querySearchHistoryByUserId(Long.valueOf(userId));
           return restResult.success(reseult);
        } catch (Exception e) {
            e.printStackTrace();
            restResult = RestResult.error("9999", e.getLocalizedMessage());
        }
        return restResult;

    }

    /*
     *
     *删除历史搜索记录
     *
     * */
    @ResponseBody
    @RequestMapping(value = "/deleteSearchHistoryByUserId")
    public RestResult deleteSearchHistoryByUserId(@RequestBody SearchHistoryRequest searchHistoryRequest) {
        RestResult restResult = new RestResult<>();
        // 获取用户id
        String userId =redisClientTemplate.get("H5_LOGIN_TOKEN_" + searchHistoryRequest.getToken());
        if (StringUtils.isEmpty(userId)){
            return RestResult.error("4000","非法请求");
        };
        try {
           pSearchHistoryService.searchHistoryDelete(Long.valueOf(userId));
            return restResult.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            restResult = RestResult.error("9999", e.getLocalizedMessage());
        }
        return restResult;

    }



}
