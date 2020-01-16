package com.guoye.controller.special;

import com.guoye.base.BizAction;
import com.guoye.util.BaseResult;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 特殊业务处理
 *
 * @author Wang Junwei
 * @see
 * @since 16点36分
 */
@RestController
@RequestMapping("/special")
public class SpecialController extends BizAction {
    /**
     * 通用config初始化
     */
    @ResponseBody
    @RequestMapping(value = "/initList")
    public BaseResult initList(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            String member = "123";
            //Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (member != null) {
                List<Dto> paramLis = bizService.queryForList("sysConfig.queryInitList", inDto);
                result.setData(paramLis);
            }
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 合同结算记录查询
     */
    @ResponseBody
    @RequestMapping(value = "/queryFeeList")
    public BaseResult queryFeeList(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (member != null) {
                List<Dto> paramLis = bizService.queryForList("contractBalance.queryFeeList", inDto);
                result.setData(paramLis);
            }
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 供应商k>v
     */
    @ResponseBody
    @RequestMapping(value = "/querySupplierType")
    public BaseResult querySupplierType(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (member != null) {
                List<Dto> paramLis = bizService.queryForList("sysConfig.querySupplierType", inDto);
                result.setData(paramLis);
            }
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 商品联动查询
     */

    @ResponseBody
    @RequestMapping(value = "/querySkuTypeList")
    public BaseResult querySkuTypeList(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (member != null) {
                List<Dto> paramLis = bizService.queryForList("sku.querySkuTypeList", inDto);
                result.setData(paramLis);
            }
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/querySkuList")
    public BaseResult querySkuList(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (member != null) {
                List<Dto> paramLis = bizService.queryForList("sku.querySkuList", inDto);
                result.setData(paramLis);
            }
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}