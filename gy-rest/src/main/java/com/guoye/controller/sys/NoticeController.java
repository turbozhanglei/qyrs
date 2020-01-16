package com.guoye.controller.sys;

import com.guoye.base.BizAction;
import com.guoye.service.BizService;
import com.guoye.util.BaseResult;
import com.guoye.util.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 公告
 *
 * @see NoticeController
 * @since 2017年6月20日15:49:06
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {


    /**
     * 发布公告
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/editNoticeStatus")
    public BaseResult editNoticeStatus(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {

        } catch (Exception e) {
            e.printStackTrace();
            // result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

}