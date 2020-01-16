package com.guoye.controller.sys;

import com.guoye.base.BizAction;
import com.guoye.service.BizService;
import com.guoye.service.GuestService;
import com.guoye.util.BaseResult;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/guest")
public class SysGuestController extends BizAction {

    @Autowired
    GuestService guestService;

    @Autowired
    public BizService bizService;

    @ResponseBody
    @RequestMapping(value = "/editInfo")
    public BaseResult editInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        //获取warningday (1/3、1/2)
        String warningday = inDto.getAsString("warningday");
        //判断warningday存在则计算warningday_num
        if (StringUtils.isNotEmpty(warningday)) {
            double warningday_num = guestService.getWarningdayNum(warningday);
            inDto.put("warningday_num", 1 - warningday_num);
        }
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (inDto.getAsLong("id") != null) {

                if (inDto.getAsString("status").equals("3")) {
                    inDto.put("status", 0);
                }

                if (member != null) {
                    // 修改
                    inDto.put("updator", member == null ? "" : member.get("id"));
                }

                bizService.updateInfo(inDto);
            } else {
                if (member != null) {
                    // 新增
                    inDto.put("deptid ", member.get("deptid"));
                    inDto.put("creator", member == null ? "" : member.get("id"));
                    inDto.put("updator", member == null ? "" : member.get("id"));
                }
                guestService.initCustomer(inDto);
            }
            result.setData(inDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    //审核
    @ResponseBody
    @RequestMapping(value = "/doAudits")
    public BaseResult doAudits(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            result = guestService.passAudit(dto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


}
