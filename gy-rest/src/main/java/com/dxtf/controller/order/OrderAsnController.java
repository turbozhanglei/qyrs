package com.dxtf.controller.order;

import com.dxtf.util.BaseResult;
import com.dxtf.util.StatusConstant;
import com.dxtf.base.BizAction;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订货单业务处理
 *
 * @author Wang Junwei
 * @see
 * @since 22点21分
 */
@RestController
@RequestMapping("/asn")
public class OrderAsnController extends BizAction {

    /**
     * 向预期收货单里导入订货单
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/importBuyAsnOrder")
    public BaseResult importBuyAsnOrder(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            String[] arrChecked = inDto.getAsString("ids").split(",");
            for (int i = 0; i < arrChecked.length; i++) {
                // 保存asn表
                Dto buy = (Dto) bizService.queryForDto("orderBuy.getInfo", new BaseDto("id", arrChecked[i]));
                buy.put("type", "0");
                buy.put("tableName", "wOrderAsn");
                bizService.saveInfo(buy);
                // 保存asn-order表
                List<Dto> buyOrders = bizService.queryForList("orderBuySku.queryList", new BaseDto("order_id", arrChecked[i]));
                for (Dto buyOrder : buyOrders) {
                    buyOrder.put("order_id", buy.get("id"));
                    buyOrder.put("tableName", "wOrderAsnSku");
                    bizService.saveInfo(buyOrder);
                }
            }
            result.setData(inDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 确认出库
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkOrderOutStock")
    public BaseResult checkOrderOutStock(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }
            //查询订单下面的商品信息，是否全部打包
            Dto param = (Dto) bizService.queryForDto("wOrderAsn.getOrderProductPackInfo", new BaseDto("order_id", inDto.getAsString("id")));
            if (null != param && param.getAsString("total_count").equals(param.getAsString("pack_count"))) {
                //更新状态为已出库
                Dto paramUpdate = new BaseDto();
                paramUpdate.put("status", "731");
                paramUpdate.put("id", inDto.getAsString("id"));
                paramUpdate.put("updator", member.getAsString("id"));
                paramUpdate.put("tableName", "wOrderSale");
                paramUpdate.put("method", "updateInfo");
                bizService.update(paramUpdate);
                //更新tms订单为出库
                Dto orderUpdate = new BaseDto();
                if (param.getAsString("t_status").equals("244")) {
                    orderUpdate.put("status", "245");
                } else if (param.getAsString("t_status").equals("249")) {
                    orderUpdate.put("status", "250");
                }
                orderUpdate.put("tableName", "wOrderAsn");
                orderUpdate.put("method", "updateTmsOrderStatus");
                orderUpdate.put("order_no", param.getAsString("order_no"));
                bizService.updateInfo(orderUpdate);
            } else {
                result.setMsg("该订单商品未全部打包，请检查");
                result.setData("该订单商品未全部打包，请检查");
                result.setCode("9999");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
