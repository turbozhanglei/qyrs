package com.guoye.controller.goodsLocation;

import com.guoye.util.BaseResult;
import com.guoye.base.BizAction;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/goodsLocation")
public class GoodsLocationController extends BizAction {

    /**
     * 保存信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/editInfo")
    public BaseResult editInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (inDto.getAsLong("id") != null) {
                // 修改
                //查询库房编码
                BaseDto wDto = new BaseDto();
                wDto.put("id", inDto.getAsString("warehouse_id"));
                wDto = (BaseDto) bizService.queryForDto("warehouse.getInfo", wDto);
                //查询库区编码
                BaseDto aDto = new BaseDto();
                aDto.put("id", inDto.getAsString("warehouse_area_id"));
                aDto = (BaseDto) bizService.queryForDto("warehouseArea.getInfo", aDto);
                String number = "";
                if (aDto != null && wDto != null) {
                    number = wDto.getAsString("number") + aDto.getAsString("number") + inDto.get("warehouse_area")
                            + inDto.getAsString("row") + "L" + inDto.getAsString("stratum") + "S" + inDto.getAsString("columnss");
                    inDto.put("number", number);
                }

                inDto.put("updator", member == null ? "" : member.get("id"));
                bizService.updateInfo(inDto);
            } else {
                // 新增
                //查询库房编码
                BaseDto wDto = new BaseDto();
                wDto.put("id", inDto.getAsString("warehouse_id"));
                wDto = (BaseDto) bizService.queryForDto("warehouse.getInfo", wDto);
                //查询库区编码
                BaseDto aDto = new BaseDto();
                aDto.put("id", inDto.getAsString("warehouse_area_id"));
                aDto = (BaseDto) bizService.queryForDto("warehouseArea.getInfo", aDto);
                String number = "";
                if (aDto != null && wDto != null) {
                    number = wDto.getAsString("number") + aDto.getAsString("number") + inDto.get("warehouse_area")
                            + inDto.getAsString("row") + "L" + inDto.getAsString("stratum") + "S" + inDto.getAsString("columnss");
                    inDto.put("number", number);
                }
                inDto.put("creator", member == null ? "" : member.get("id"));
                inDto.put("updator", member == null ? "" : member.get("id"));
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
