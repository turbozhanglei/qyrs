package com.guoye.controller.order;

import com.guoye.base.BizAction;
import com.guoye.service.OrderSaleService;
import com.guoye.util.BaseResult;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 订货单业务处理
 *
 * @author Wang Junwei
 * @see
 * @since 22点21分
 */
@RestController
@RequestMapping("/sale")
public class OrderSaleController extends BizAction {

    @Autowired
    private OrderSaleService orderSaleService;

    /**
     * 向预期收货单里导入订货单
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveSaleOrder")
    public BaseResult saveSaleOrder(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode("4000");
                result.setMsg("请先登录");
                result.setData("请先登录");
                return result;
            }
            orderSaleService.saveOrder(inDto, member.getAsLong("id"));
            result.setData("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 向预期收货单里导入订货单
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveSaleOutOrder")
    public BaseResult saveSaleOutOrder(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode("4000");
                result.setMsg("请先登录");
                result.setData("请先登录");
                return result;
            }
            orderSaleService.saveSaleOutOrder(inDto, member.getAsLong("id"));
            result.setData("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 库存分配
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveSaleDistribution")
    public BaseResult saveSaleDistribution(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode("4000");
                result.setMsg("请先登录");
                return result;
            }
            orderSaleService.saveSaleDistribution(inDto, member.getAsLong("id"));
            result.setData("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 出库下单删除
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteOutOrderInfo")
    public BaseResult deleteOutOrderInfo(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode("4000");
                result.setMsg("请先登录");
                result.setData("请先登录");
                return result;
            }
            result = orderSaleService.deleteOutOrderInfo(inDto, member.getAsLong("id"));
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
