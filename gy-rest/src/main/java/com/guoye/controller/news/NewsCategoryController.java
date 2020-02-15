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
import java.util.ArrayList;
import java.util.Date;
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
      * 咨询分类初始化*/
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


      /*新增咨询分类
      * */
      @ResponseBody
    @RequestMapping(value = "saveCateGory")
    public BaseResult saveCateGory(HttpServletRequest request, HttpServletResponse response){
          BaseResult result = new BaseResult();
          Dto dto =WebUtils.getParamAsDto(request);
          try {
              Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);

              if (null == member) {
                  result.setCode(StatusConstant.CODE_4000);
                  result.setMsg("请登录");
                  return result;
              }
              dto.put("tableName", "gNewsCategory");
              //id 不为空则修改
              if (StringUtils.isNotEmpty(dto.getAsString("id"))){
                  dto.put("update_time",new Date());
                  dto.put("updator", member == null ? "" : member.get("id"));

                  bizService.updateInfo(dto);
              }else {
                  //插入
                  dto.put("creator", member == null ? "" : member.get("id"));
                  dto.put("updator", member == null ? "" : member.get("id"));
                  if(dto.getAsLong("refId")==1){//判断是否为顶级分类
                      dto.put("level",1);
                      dto.put("refId",dto.getAsLong("refId"));
                  }else {
                      dto.put("level",2);
                      dto.put("refId",dto.getAsString("refId"));
                  }
                  bizService.saveInfo(dto);
              }
          }catch (Exception e) {
              e.printStackTrace();
              result = reduceErr(e.getLocalizedMessage());
          }

          return result;
      }

     /*启用禁用按钮*/
     @ResponseBody
     @RequestMapping(value = "changeCateGoryStatus")
    public BaseResult changeCateGoryStatus(HttpServletRequest request){
        Dto dto =WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
       try{
           dto.put("tableName", "gNewsCategory");

           if(dto.getAsString("status").equals("0")) {//判断状态是否启用
               dto.put("status","1");
               dto.put("id",dto.getAsLong("id"));
               if (dto.getAsLong("level") == 2) {//判断是否是二级分类

                   bizService.updateInfo(dto);
               }else {
                   bizService.updateInfo(dto);
                    Dto dto1 =new BaseDto();
                    dto1.put("refId",dto.getAsLong("id"));
                    dto1.put("tableName", "gNewsCategory");
                    dto1.put("method","updateSecondCateGoryStatus");
                    dto1.put("status","1");
                    bizService.update(dto1);
               }
           }else {
               dto.put("status","0");
               dto.put("id",dto.getAsLong("id"));
               if (dto.getAsLong("level") == 2) {//判断是否是二级分类

                   bizService.updateInfo(dto);
               }else {
                   bizService.updateInfo(dto);
                   Dto dto1 =new BaseDto();
                   dto1.put("refId",dto.getAsLong("id"));
                   dto1.put("tableName", "gNewsCategory");
                   dto1.put("method","updateSecondCateGoryStatus");
                   dto1.put("status","0");
                   bizService.update(dto1);
               }
           }

       }catch (Exception e){
           e.printStackTrace();
           result=reduceErr(e.getLocalizedMessage());
       }
        return  result;

     }

     /*删除按钮*/
    @ResponseBody
        @RequestMapping(value = "/delCategory")
  public BaseResult delCategory(HttpServletRequest request){
        BaseResult result =new BaseResult();
        Dto paramsAsDto = WebUtils.getParamAsDto(request);
        try{
           if(paramsAsDto.getAsLong("level") == 2){//如果是二级分类 查询是否有下属文章
               paramsAsDto.put("tableName", "gNewsCategory");
               paramsAsDto.put("refId",paramsAsDto.getAsString("id"));
               List<Dto> totalCount = bizService.queryForList("querySecondArticleCount",paramsAsDto);
               int count=totalCount.get(0).getAsInteger("total");
               if(count>0){
               result.setData("notDel"+"该分类有下级文章，无法删除");
               }else {
                   Dto dto =new BaseDto();
                   dto.put("tableName", "gNewsCategory");
                   dto.put("ids",paramsAsDto.getAsString("id"));
                   bizService.deleteInfo(dto);
               }
           }else {//如果是一级分类 查询是否有下属二级分类
               Dto dto =new BaseDto();
               paramsAsDto.put("tableName", "gNewsCategory");
                paramsAsDto.put("refId",paramsAsDto.getAsString("id"));
               List<Dto> totalCount = bizService.queryForList("querySecondListCount",paramsAsDto);
               int count=totalCount.get(0).getAsInteger("total");
               if(count>0){
                   result.setData("notDel"+"该分类有下级分类，无法删除");
               }else {

                   dto.put("tableName", "gNewsCategory");
                   dto.put("ids",paramsAsDto.getAsLong("id"));
                   bizService.deleteInfo(dto);
                   result.setMsg("删除成功");
               }

           }
        }catch (Exception e){
            e.printStackTrace();
            result=reduceErr(e.getLocalizedMessage());
        }
        return  result;
    }

    /*分类列表初始化*/

    @ResponseBody
    @RequestMapping(value = "/queryCategoryList")
    public BaseResult queryCategoryList(HttpServletRequest request){
        Dto paramsAsDto = WebUtils.getParamAsDto(request);
        paramsAsDto.put("level",1);
        BaseResult result =new BaseResult();
        //查询所有一级分类
        List<Dto> catrgoryList = (List<Dto>) bizService.queryList("gNewsCategory.queryCategoryList", paramsAsDto);
        for(Dto dto1:catrgoryList){
            Dto sDto=new BaseDto();
            sDto.put("refId",dto1.getAsInteger("id"));
            List<Dto> children = (List<Dto>) bizService.queryList("gNewsCategory.queryCategoryList1", sDto);
            dto1.put("children",children);
        }
        if(catrgoryList==null){
            result.setCode("9999"+"查询失败");
        }else {
            result.setCode("0000");
            result.setData(catrgoryList);
        }
        return  result;
    }

    //添加子类按钮
    @ResponseBody
    @RequestMapping(value = "/saveChildrenCategory")
    public BaseResult saveChildrenCategory(HttpServletRequest request) {
        BaseResult result = new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);

        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);

            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }
            dto.put("tableName", "gNewsCategory");
            dto.put("method","saveChildrenCategory");

                //插入
                dto.put("creator", member == null ? "" : member.get("id"));
                dto.put("updator", member == null ? "" : member.get("id"));
                dto.put("level", 2);
                dto.put("refId", dto.getAsString("firstId"));

                bizService.save(dto);

        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }

        return result;
    }

    //资讯列表接口
    @ResponseBody
    @RequestMapping(value = "/newsCategoryList")
    public BaseResult getNewCategoryList(HttpServletRequest request){
        BaseResult result = new BaseResult();

        Dto dto =WebUtils.getParamAsDto(request);
        dto.put("id",dto.getAsLong("refId"));//根据一级分类id查询对应对象
        dto.put("tableName","gNewsCategory");
        Dto obj = (Dto)bizService.queryForDto("gNewsCategory.getNewsCategoryDto",dto);

        obj.put("refId",obj.getAsLong("id"));//根据返回id查询二级分类list
        List<Dto> newsCategoryList = bizService.queryList("gNewsCategory.getNewsCategoryList",obj);
        if(newsCategoryList==null){
            result.setMsg("调用失败");
        }else{
        result.setData(newsCategoryList);
        result.setCode("0000");
        result.setMsg("调用成功");
        }
        return  result;
    }
    //前端首页咨询列表接口文档
    @ResponseBody
    @RequestMapping(value = "/newsCategory")
    public  BaseResult queryNewsCategory(HttpServletRequest request){
        BaseResult result = new BaseResult();

        Dto dto =WebUtils.getParamAsDto(request);
        dto.put("id",dto.getAsLong("refId"));//根据一级分类id查询对应对象
        dto.put("tableName","gNewsCategory");
        Dto obj = (Dto)bizService.queryForDto("gNewsCategory.getNewsCategoryDto",dto);

        obj.put("refId",obj.getAsLong("id"));//根据返回id查询二级分类对应文章
        List<Dto> newsCategory = bizService.queryList("gNewsCategory.queryNewsCategory",obj);
        if(newsCategory==null){
            result.setMsg("调用失败");
        }else{
            result.setData(newsCategory);
            result.setCode("0000");
            result.setMsg("调用成功");
        }
        return  result;
    }
}
