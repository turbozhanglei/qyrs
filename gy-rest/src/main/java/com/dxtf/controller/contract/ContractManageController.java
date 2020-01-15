package com.dxtf.controller.contract;

import com.dxtf.base.BizAction;
import com.dxtf.util.BaseResult;
import org.apache.commons.lang.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 合同业务处理
 *
 * @author Wang Junwei
 * @see
 * @since 22点21分
 */
@RestController
@RequestMapping("/contract")
public class ContractManageController extends BizAction {
    /**
     * 保存信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/contractManage")
    public BaseResult contractManage(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (member != null) {
                if (inDto.getAsLong("id") != null) {
                    inDto.put("updator", member == null ? "" : member.get("id"));
                    bizService.updateInfo(inDto);
                } else {
                    // 创建时间戳
                    long millis = System.currentTimeMillis();
                    // 得到redis中的编号
                    String value = redisService.getValue("number");
                    if (StringUtils.isNotEmpty(value)) {
                        int parseInt = Integer.parseInt(value);
                        // 编号加1重新赋值
                        redisService.setValue("number", String.valueOf(parseInt + 1));
                        inDto.put("contract_number", millis + String.format("%0" + 5 + "d", parseInt + 1));
                    } else {
                        redisService.setValue("number", "1");
                        inDto.put("number", millis + String.format("%0" + 5 + "d", 1));
                    }
                    inDto.put("creator", member == null ? "" : member.get("id"));
                    inDto.put("updator", member == null ? "" : member.get("id"));
                    bizService.saveInfo(inDto);
                }
            } else {
                result.setCode("9999");
                result.setMsg("服务异常,请稍后重试！");
                return result;
            }
            result.setData(inDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
