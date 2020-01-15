package com.dxtf.controller.order;

import com.dxtf.base.BizAction;
import com.dxtf.service.InStockService;
import com.dxtf.util.BaseResult;
import com.dxtf.util.JSONUtil;
import com.dxtf.util.StatusConstant;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.util.G4Constants;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 入库业务管理
 */
@RestController
@RequestMapping("/instock")
public class InStockManageController extends BizAction {

    @Autowired
    InStockService inStockService;


    /**
     * 查询订单中的产品信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryOrderBuySku")
    public BaseResult queryOrderBuySku(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }

            String orderId = dto.getAsString("id");

            if (StringUtils.isNotEmpty(orderId)) {
                //查询订单信息
                List<Dto> list = bizService.queryList("orderBuySku.queryList", new BaseDto("order_id", orderId));
                if (!list.isEmpty()) {
                    Iterator<Dto> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Dto next = iterator.next();
                        //查询司机信息
                        List driverList = bizService.queryList("wdriver.queryOrderBuySkuDriver", new BaseDto("buy_sku_id", next.getAsString("id")));
                        next.put("driverData", driverList);
                    }

                }

                result.setData(JSONUtil.formatDateList(list, G4Constants.FORMAT_DateTime));
            }


        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 下单
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveBuyOrder")
    public BaseResult saveBuyOrder(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }

            return inStockService.saveOrder(dto, member.getAsLong("id"));

        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 入库
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveStoreSku")
    public BaseResult saveStoreSku(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }

            return inStockService.saveStoreSku(dto, member.getAsLong("id"));

        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 更新库存
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/intoStoreBuySku")
    public BaseResult intoStoreBuySku(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }

            return inStockService.intoStoreBuySku(dto, member.getAsLong("id"));
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 批量入库
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/batchIntoStoreBuySku")
    public BaseResult batchIntoStoreBuySku(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }

            //入库订单表id集合
            String ids = dto.getAsString("ids");

            //待检卡id集合
            String buy_checked_ids = dto.getAsString("buy_checked_ids");

            List<String> error = new ArrayList<>();

            List<Dto> orderList = new ArrayList<>();

            if (StringUtils.isNotEmpty(buy_checked_ids)) {
                //根据父订单更新所有子订单状态
                //查询子订单状态为0的所有数据
                Dto search = new BaseDto();
                search.put("tableName", "worderinstore");
                search.put("buy_checked_ids", buy_checked_ids);
                search.put("status", "2");

                orderList = bizService.queryForList("worderinstore.queryList", dto);

            } else if (StringUtils.isNotEmpty(ids)) {
                //根据id更新所有子订单状态
                //查询订单状态为0的所有数据
                Dto search = new BaseDto();
                search.put("tableName", "worderinstore");
                search.put("ids", ids);
                search.put("status", "2");

                orderList = bizService.queryForList("worderinstore.queryList", dto);
            }


            Iterator<Dto> iterator = orderList.iterator();
            while (iterator.hasNext()) {

                Dto next = iterator.next();

                //批量更新数据入库存
                try {
                    inStockService.batchIntoOrderStoreSku(next, member.getAsLong("id"));
                } catch (Exception e) {
                    //记录异常批次号
                    error.add(next.getAsString("batch_number"));
                }
            }

            if (!error.isEmpty()) {
                result.setCode("9999");
                result.setMsg(String.join(",", error) + "批次货物入库失败");
            }


        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 删除订单产品和司机的关联关系
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteOrderBuySkuDriver")
    public BaseResult deleteOrderBuySkuDriver(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }

            return inStockService.deleteOrderBuySkuDriver(dto);

        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 查询订单中的产品信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/doOrderCheck")
    public BaseResult doOrderCheck(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }
            dto.put("tableName", "order_buy");
            dto.put("method", "updateStatus");
            dto.put("updator", member.getAsString("id"));
            if (dto.getAsString("ids") != null) {

                String ids[] = dto.getAsString("ids").split(",");
                for (String id : ids) {
                    if (StringUtils.isNotEmpty(id)) {
                        try {
                            inStockService.doOrderCheck(dto, member.getAsLong("id"), id);
                        } catch (Exception e) {
                            Dto update = new BaseDto();
                            update.put("tableName", "order_buy");
                            update.put("method", "updateIsChecked");
                            update.put("updator", 1);
                            update.put("is_checked", "1");
                            update.put("id", dto.getAsString("id"));
                            bizService.update(update);
                            e.printStackTrace();

                        }
                    }
                }

                result.setCode("0000");
                result.setMsg("操作成功");
            }


        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 出库
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/upSkuStatus")
    public BaseResult upSkuStatus(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            String ids = dto.getAsString("ids");
            for (String id : ids.split(",")) {
                if (StringUtils.isNotEmpty(id)) {
                    dto.put("id", id);
                    try {
                        Dto returnDto = inStockService.upSkuStatus(dto);
                        if (returnDto.getAsString("in_batch_number") != null && returnDto.getAsString("in_batch_number") != "") {
                            //根据批次号查询tray_id和setting_id
                            Dto trayDto = new BaseDto();
                            trayDto.put("in_batch_number", returnDto.getAsString("in_batch_number"));
                            trayDto = (BaseDto) bizService.queryForDto("outSku.queryTraidAndSettingid", trayDto);
                            //根据tray_id和setting_id查询该托盘是否空余
                            BaseDto countDto = (BaseDto) bizService.queryForDto("outSku.queryOrderStoreSku", trayDto);
                            //托盘空余更新 savelocation
                            if (countDto.getAsString("total").isEmpty()) {
                                BaseDto wTrayDto = new BaseDto();
                                wTrayDto.put("tableName", "wtray");
                                wTrayDto.put("method", "updateSavelocation");
                                wTrayDto.put("id", trayDto.getAsString("tray_id"));
                                wTrayDto.put("savelocation", null);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


        } catch (RuntimeException e) {
            e.printStackTrace();

        }

        return result;
    }


}