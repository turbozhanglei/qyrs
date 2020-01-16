package com.guoye.controller.order;

import com.guoye.base.BizAction;
import com.guoye.service.InStockService;
import com.guoye.util.BaseResult;
import com.guoye.util.StatusConstant;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 质检控制器
 */
@RestController
@RequestMapping("/quality")
public class QualityInspectionController extends BizAction {

    @Autowired
    InStockService inStockService;


    /**
     * 打印待检卡
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/printQualityCard")
    public BaseResult printQualityCard(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }


            return inStockService.printQualityCard(dto, member.getAsLong("id"));

        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 进行质检
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/doQualityProduct")
    public BaseResult doQualityProduct(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }

            return inStockService.doQualityProduct(dto, member.getAsLong("id"));


        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 手持版本质检
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/allotStock")
    public BaseResult allotStock(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }

            inStockService.allotStock(dto, member.getAsLong("id"));


        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 手持版本质检完成
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/doCloseCheck")
    public BaseResult doCloseCheck(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }

            inStockService.doCloseCheck(dto, member.getAsLong("id"));

        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
