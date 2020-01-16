package com.guoye.controller.supplier;

import com.guoye.base.BizAction;
import com.guoye.util.BaseResult;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: Wang Junwei
 * @create: 2018-10-23
 * @Description: 供应商
 */
@RestController
@RequestMapping("/supplier")
public class supplierController extends BizAction {
    /**
     * 新增供应商
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sddSupplier")
    public BaseResult sddSupplier(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (inDto.getAsLong("id") != null) {
                // 修改
                inDto.put("updator", member == null ? "" : member.get("id"));
                bizService.updateInfo(inDto);
            } else {
                // 新增
                inDto.put("creator", member == null ? "" : member.get("id"));
                inDto.put("updator", member == null ? "" : member.get("id"));
                if (StringUtils.isNotEmpty(redisService.getValue("number"))) {
                    String code = String.valueOf(Integer.parseInt(redisService.getValue("number")) + 1);
                    redisService.setValue("number", code);
                    inDto.put("number", System.currentTimeMillis() + String.format("%0" + 8 + "d", Integer.parseInt(code)));
                } else {
                    redisService.setValue("number", "1");
                    inDto.put("number", System.currentTimeMillis() + String.format("%0" + 8 + "d", 1));
                }
                bizService.saveInfo(inDto);
            }
            result.setData(inDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}