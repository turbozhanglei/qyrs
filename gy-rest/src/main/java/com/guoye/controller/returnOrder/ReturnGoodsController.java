package com.guoye.controller.returnOrder;

import com.guoye.base.BaseMapper;
import com.guoye.base.BizAction;
import com.guoye.service.ReturnGoodsService;
import com.guoye.util.BaseResult;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 商品
 * Created by wj on 2018/8/21.
 */

@SuppressWarnings("all")
@RestController
@RequestMapping("/return")
public class ReturnGoodsController extends BizAction {

    @Autowired
    private BaseMapper g4Dao;
    @Autowired
    ReturnGoodsService returnGoodsService;

    /**
     * 退货单信息保存
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/editUserReturnInfo")
    public BaseResult editUserReturnInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            returnGoodsService.saveReturnOrder(inDto, member);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 删除信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteReturnInfo")
    public BaseResult deleteReturnInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            returnGoodsService.deleteReturnInfo(inDto, member);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 货位转移信息save
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveLocationTransfer")
    public BaseResult saveLocationTransfer(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            returnGoodsService.saveLocationTransfer(inDto, member);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 货位转移信息save
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateLocationTransfer")
    public BaseResult updateLocationTransfer(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto update = new BaseDto();
            update.put("tableName", "locationTransfer");
            update.put("method", "updateLocationTransferStatus");
            update.put("status", "1");
            update.put("ids", inDto.getAsString("ids"));

            g4Dao.updateInfo("locationTransfer.updateLocationTransferStatus", update);


        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 非订单出库信息保存
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveNotorderout")
    public BaseResult saveNotorderout(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            returnGoodsService.saveNotorderout(inDto, member);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


}