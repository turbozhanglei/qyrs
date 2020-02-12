package com.guoye.controller.Advert;

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

/**
* @Autor:zhaowei
* @Date: 2020/2/11 16:12
* @Description:
*/
@RestController
@RequestMapping("/advert")
public class AvertController extends BizAction {

    @ResponseBody
    @RequestMapping(value = "/saveInfo")
    public BaseResult saveInfo(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);

            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }
            dto.put("tableName", "adCode");
            //id 不为空则修改
            if (StringUtils.isNotEmpty(dto.getAsString("id"))){
                dto.put("update_time",new Date());
                dto.put("updator", member == null ? "" : member.get("id"));
                bizService.updateInfo(dto);
            }else {
                //插入
                dto.put("creator", member == null ? "" : member.get("id"));
                dto.put("updator", member == null ? "" : member.get("id"));
                bizService.saveInfo(dto);
            }
        }catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
    * @Autor:zhaowei
    * @Date: 2020/2/11 17:32
    * @Description: 查询广告位详情
    */
    @ResponseBody
    @RequestMapping(value = "/getInfo")
    public BaseResult getInfo(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);

            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }

            Dto words = (BaseDto) bizService.queryForObject("adCode.getInfo", dto);
            if (words != null && words.getAsLong("id") != null) {
                result.setData(words);
            }
        }catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
