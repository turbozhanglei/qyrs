package com.guoye.controller.word;

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
* @Autor: zhaowei
* @Date: 2020/2/11 13:25
* @Description:
*/
@RestController
@RequestMapping("/word")
public class SensitiveWord extends BizAction {
    
    /**
    * @Autor:zhaowei
    * @Date: 2020/2/11 13:26
    * @Description: 获取敏感词信息
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

            Dto words = (BaseDto) bizService.queryForObject("sensitiveWord.getInfo", dto);
            if (words != null && words.getAsLong("id") != null) {
                result.setData(words);
            }
        }catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
    * @Autor:zhaowei
    * @Date: 2020/2/11 14:18
    * @Description: 保存或修改敏感词
    */
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
            dto.put("tableName", "sensitiveWord");
            //id 不为空则修改
            if (StringUtils.isNotEmpty(dto.getAsString("id"))){
                dto.put("update_time",new Date());
                dto.put("updator", member == null ? "" : member.get("id"));
                bizService.updateInfo(dto);
            }else {
                //插入
                dto.put("create_time",new Date());
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
}
