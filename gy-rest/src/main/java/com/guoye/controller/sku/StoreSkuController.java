package com.guoye.controller.sku;

import com.guoye.util.BaseResult;
import com.guoye.base.BizAction;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by MJ on 2019-02-25.
 */

@RestController
@RequestMapping("/storeSku")
public class StoreSkuController extends BizAction {


    /**
     * 库存更改并保存调整记录
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/editStoreSku")
    public BaseResult editStoreSku(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            dto.put("sku_id", dto.getAsString("skuId"));
            dto.put("store_id", dto.getAsString("storeId"));
            dto.put("stock", dto.getAsString("num"));
            dto.put("tableName", "orderStoreSku");
            bizService.updateInfo(dto);
            dto.put("creator", member == null ? "" : member.get("id"));
            dto.put("tableName", "orderStoreSkuLog");
            bizService.saveInfo(dto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 库存新增
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addStoreSku")
    @Transactional
    public BaseResult addStoreSku(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            if (StringUtils.isNotEmpty(dto.getAsString("sku_id"))) {
                for (String param : dto.getAsString("sku_id").split(",")) {
                    //新增库存，然后更新ASN收货表
                    Dto stockParam = new BaseDto();
                    stockParam.put("deptid", dto.getAsString("deptid"));
                    stockParam.put("order_id", dto.getAsString("id"));
                    stockParam.put("order_type", "1");
                    stockParam.put("sku_id", param);
                    stockParam.put("stock", dto.getAsString("stock"));
                    stockParam.put("provider_id", dto.getAsString("provider_id"));
                    stockParam.put("setting_id", dto.getAsString("setting_id"));
                    stockParam.put("status", dto.getAsString("status"));
                    stockParam.put("create_time", dto.getAsString("create_time"));

                    stockParam.put("overcharge", dto.getAsString("overcharge"));
                    stockParam.put("owing", dto.getAsString("owing"));
                    stockParam.put("productstatus", dto.getAsString("productstatus"));
                    stockParam.put("tableName", "orderStoreSku");
                    bizService.saveInfo(stockParam);
                    //更新ASN收货表
                    Dto asnSkuParam = new BaseDto();
                    asnSkuParam.put("num", dto.getAsString("stock"));
                    asnSkuParam.put("ea_num", dto.getAsString("stock"));
                    asnSkuParam.put("stock", dto.getAsString("stock"));
                    asnSkuParam.put("worth", dto.getAsString("worth"));
                    asnSkuParam.put("status", dto.getAsString("status"));
                    asnSkuParam.put("remark", dto.getAsString("remark"));
                    asnSkuParam.put("setting_id", dto.getAsString("setting_id"));
                    asnSkuParam.put("facility_id", dto.getAsString("facility_id"));
                    asnSkuParam.put("trace_number", dto.getAsString("trace_number"));
                    asnSkuParam.put("receiving_status", "0");
                    asnSkuParam.put("id", dto.getAsString("id"));
                    asnSkuParam.put("tableName", "wOrderAsnSku");
                    bizService.updateInfo(asnSkuParam);
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 库存新增
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateDownUpperStatus")
    public BaseResult updateDownUpperStatus(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            dto.put("tableName", "orderStoreSku");
            dto.put("method", "updateDownUpperStatus");
            bizService.update(dto);
        } catch (RuntimeException e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
