package com.guoye.controller.news;

import com.guoye.base.BizAction;
import com.guoye.util.BaseResult;
import com.guoye.util.StatusConstant;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.resource.util.StringUtils;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

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
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result =new BaseResult();
         Dto aDto=(Dto)bizService.queryForDto("newsArticle.queryArticleDetail", dto);
        result.setData(aDto);
        result.setMsg("调用成功");
        return  result;
    }

    /*
     * 前台二级分类Id查询咨询文章*/
    @ResponseBody
    @RequestMapping(value = "/queryArticleListByCategoryId")
    public BaseResult queryArticleListByCategoryId(HttpServletRequest request){
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result =new BaseResult();
        List<Dto> articleList=bizService.queryForList("newsArticle.queryArticleListByCategoryId", dto);
        result.setData(articleList);
        result.setMsg("调用成功");
        return  result;
    }
}
