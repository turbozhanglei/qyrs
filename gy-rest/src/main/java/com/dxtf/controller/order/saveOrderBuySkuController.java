package com.dxtf.controller.order;


import com.dxtf.base.BizAction;
import com.dxtf.util.BaseResult;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 订单SKU保存
 *
 * @author Wang Junwei
 * @see
 * @since 22点21分
 */
@RestController
@RequestMapping("/orderSku")
public class saveOrderBuySkuController extends BizAction {

    /**
     * 添加订货单信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveOrderBuySku")
    public BaseResult saveOrderBuySku(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        dto.put("tableName", dto.getAsString("t"));
        try {
            Dto sku = (Dto) bizService.queryForDto("wOrderAsnSku" + ".querySkuByName", new BaseDto("name", dto.getAsString("name")));
            if (null == sku) {
                sku = new BaseDto();
                sku.put("name", dto.getAsString("name"));
                sku.put("number", dto.getAsString("number"));
                sku.put("price", dto.getAsString("price"));
                sku.put("introduction", dto.getAsString("introduction"));
                sku.put("typed", dto.getAsString("typed"));
                sku.put("level", dto.getAsString("level"));
                sku.put("sort", dto.getAsString("sort"));
                sku.put("type_id", dto.getAsString("type_id"));
                sku.put("describes", dto.getAsString("describes"));
                sku.put("status", dto.getAsString("status"));
                sku.put("tableName", "wOrderAsnSku");
                sku.put("method", "saveSkuInfo");
                bizService.save(sku);
            }
            Dto paramData = new BaseDto();
            paramData.put("name", dto.getAsString("name"));
            paramData.put("sku_id", sku.getAsString("id"));
            paramData.put("order_id", dto.getAsString("order_id"));
            paramData.put("batch_number", dto.getAsString("batch_number"));
            paramData.put("package", dto.getAsString("package"));
            paramData.put("measure_unit", dto.getAsString("measure_unit"));
            paramData.put("num", dto.getAsString("num"));
            paramData.put("ea_num", dto.getAsString("ea_num"));
            paramData.put("rough_weight", dto.getAsString("rough_weight"));
            paramData.put("net_weight", dto.getAsString("net_weight"));
            paramData.put("volume", dto.getAsString("volume"));
            paramData.put("worth", dto.getAsString("worth"));
            paramData.put("trace_number", dto.getAsString("trace_number"));
            paramData.put("status", dto.getAsString("status"));
            paramData.put("receiving_status", dto.getAsString("receiving_status"));
            paramData.put("stock", dto.getAsString("stock"));
            paramData.put("lock_stock", dto.getAsString("lock_stock"));
            paramData.put("rejection", dto.getAsString("rejection"));
            paramData.put("rejection_remark", dto.getAsString("rejection_remark"));
            paramData.put("setting_id", dto.getAsString("setting_id"));
            paramData.put("facility_id", dto.getAsString("facility_id"));
            paramData.put("remark", dto.getAsString("remark"));
            paramData.put("period", dto.getAsString("period"));
            paramData.put("period_time", dto.getAsString("period_time"));
            paramData.put("tableName", "wOrderAsnSku");
            bizService.saveInfo(paramData);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
