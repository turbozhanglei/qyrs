package com.guoye.controller.news;

import com.guoye.base.BizAction;
import com.guoye.util.BaseResult;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Autor:zhaosen
 * @Date: 2020/2/13 12:41
 * @Description:
 */
@RestController
@RequestMapping("/news")
public class NewsCategoryController extends BizAction {
      /*
      * 分类初始化*/
      @ResponseBody
    @RequestMapping(value = "/getTypeList")
    public BaseResult getTypeList(HttpServletRequest request){
          Dto paramsAsDto = WebUtils.getParamAsDto(request);
          BaseResult result =new BaseResult();
          List<Dto> nodes = (List<Dto>) bizService.queryList("gNewsCategory.queryTypeList", paramsAsDto);
         if(nodes==null){
             result.setCode(9999+"查询失败");
         }else {
             result.setCode("0000");
            result.setData(nodes);
         }
          return  result;
      }
}
