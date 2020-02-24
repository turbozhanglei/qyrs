package com.guoye.controller.news;

import com.guoye.base.BizAction;
import com.guoye.util.BaseResult;
import com.guoye.util.JSONUtil;
import com.guoye.util.StatusConstant;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.resource.util.StringUtils;
import org.g4studio.core.util.G4Constants;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

/**
 * @Autor:zhaosen
 * @Date: 2020/2/13 12:41
 * @Description:
 */
@RestController
@RequestMapping("/queryArticle")
public class QueryNewsArticleController extends BizAction {

      /*
      * 咨询分类初始化*/
      @ResponseBody
    @RequestMapping(value = "/queryCategoryList")
    public BaseResult getTypeList(HttpServletRequest request){
          Dto dto = WebUtils.getParamAsDto(request);
          BaseResult result =new BaseResult();
          dto.put("level",1);
          List<Dto> categoryList = (List<Dto>) bizService.queryList("newsArticle.queryCategoryList", dto);
          for(Dto cDto:categoryList){
              Dto pDto=new BaseDto();
              pDto.put("refId",cDto.getAsLong("id"));
              List<Dto> categorySecondList = (List<Dto>) bizService.queryList("newsArticle.queryCategoryList", pDto);
              cDto.put("children",categorySecondList);
          }
             result.setData(categoryList);
          return  result;
      }
    /*
     * 查询前台咨询详情*/
    @ResponseBody
    @RequestMapping(value = "/queryArticleDetail")
    public BaseResult queryArticleDetail(HttpServletRequest request){
        BaseResult result =new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        String token=dto.getAsString("token");
        if (!token.startsWith("pc_login_token:")) {
            token="mp_login_token:"+token;
        }
        Dto member = redisService.getObject(token, BaseDto.class);
//        if (null == member) {
//            result.setCode(StatusConstant.CODE_4000);
//            result.setMsg("请登录");
//            return result;
//        }
        if(member!=null){
            dto.put("userId",member.get("id"));
        }
         Dto aDto=(Dto)bizService.queryForDto("newsArticle.queryArticleDetail", dto);
        result.setData(aDto);
        result.setMsg("调用成功");
        return  result;
    }

    /*
     * 前台二级分类Id查询咨询文章*/
    @ResponseBody
    @RequestMapping(value = "/queryArticleListByCategoryId")
    public Dto queryArticleListByCategoryId(HttpServletRequest request){
        Dto dto = WebUtils.getParamAsDto(request);
        Dto retDto = new BaseDto();
        dto.put("start",dto.getAsLong("start")-1);
        dto.put("end",dto.getAsLong("limit"));
        List<Dto> articleList=bizService.queryForList("newsArticle.queryArticleListByCategoryId", dto);
        Map<String,List<Dto>> data=new HashMap<String,List<Dto>>();
       if(!articleList.isEmpty()){
           for(Dto tDto:articleList){
             if(StringUtils.isNotEmpty(tDto.getAsString("createTime"))){
                 SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 try {
                     Date date=formate.parse(tDto.getAsString("createTime"));
                     tDto.put("createTime",format(date));
                 } catch (ParseException e) {
                     e.printStackTrace();
                 }
             }
           }
           data.put("articleList",JSONUtil.formatDateList(articleList, G4Constants.FORMAT_DateTime));
           retDto.put("data",data );
           retDto.put("total",articleList.size() );
           retDto.put("msg","调用成功");
           retDto.put("code","0000");
       }else{
           retDto.put("total",0);
           retDto.put("articleList",articleList);
           retDto.put("msg","暂无咨询");
           retDto.put("code","0000");
       }
        return  retDto;
    }
    private String format(Date date) {
        DateTime now = new DateTime();
        DateTime today_start = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0, 0, 0);
        DateTime today_end = today_start.plusDays(1);
        DateTime yesterday_start = today_start.minusDays(1);
        if(date.after(today_start.toDate()) && date.before(today_end.toDate())) {
            return String.format("%s", new DateTime(date).toString("HH:mm:ss"));
        } else if(date.after(yesterday_start.toDate()) && date.before(today_start.toDate())) {
            return String.format("昨天 %s", new DateTime(date).toString("HH:mm:ss"));
        }
        return new DateTime(date).toString("yyyy-MM-dd");
    }

}
