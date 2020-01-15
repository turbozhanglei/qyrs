package com.dxtf.service.impl;

import com.alibaba.fastjson.JSON;
import com.dxtf.service.BizService;
import com.dxtf.service.OrderSaleService;
import com.dxtf.util.*;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("orderSaleServiceImpl")
public class OrderSaleServiceImpl implements OrderSaleService {

    @Autowired
    public RedisService redisService;

    @Autowired
    public BizService bizService;

    @Autowired
    public OrderNumberUtils orderNumberUtils;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public BaseResult saveOrder(Dto dto, Long memberId) {
        try {
            dto.put("order_no", UUID.randomUUID().toString().replaceAll("-", ""));
            dto.put("tableName", "norderSale");
            dto.put("creator", memberId);
            dto.put("storage_time", DateUtil.getStringFromDate(dto.getAsDate("storage_time"), "yyyy-MM-dd HH:mm:ss"));
            dto.put("out_stock", DateUtil.getStringFromDate(dto.getAsDate("out_stock"), "yyyy-MM-dd HH:mm:ss"));
            bizService.saveInfo(dto);
            if (StringUtils.isNotEmpty(dto.getAsString("products"))) {
                List<Map<String, Object>> products = JSONUtil.parseJSON2List(dto.getAsString("products"));
                Map<String, Object> productsStock = JSONUtil.parseJSON2Map(dto.getAsString("stock_num"));
                Map<String, Object> driverList = JSONUtil.parseJSON2Map(dto.getAsString("driver_list"));
                for (Map map : products) {
                    Dto product = new BaseDto();
                    product.putAll(map);
                    String productId = product.getAsString("id");
                    product.put("sale_id", dto.getAsString("id"));
                    product.put("batch_num", UUID.randomUUID().toString().replaceAll("-", ""));
                    product.put("buy_sku_id", product.getAsString("buy_sku_id"));
                    product.put("store_id", product.getAsString("id"));
                    product.put("out_stock_num", productsStock.get("stock_" + product.getAsString("id")));
                    product.put("creator", memberId);
                    product.put("tableName", "nwOrderSaleSku");
                    bizService.saveInfo(product);
                    List<Map<String, Object>> productsDrivers = JSONUtil.parseJSON2List(JSON.toJSONString(driverList.get("keys_" + productId)));
                    if (null != productsDrivers && productsDrivers.size() > 0) {
                        for (Map mapDriver : productsDrivers) {
                            Dto paramDriver = new BaseDto();
                            paramDriver.put("tableName", "nwOrderSaleSku");
                            paramDriver.put("method", "saveDriverInfo");
                            paramDriver.put("sale_sku_id", productId);
                            paramDriver.put("driver_id", mapDriver.get("id"));
                            paramDriver.put("creator", memberId);
                            bizService.save(paramDriver);
                        }
                    }
                    ;
                }
            }
        } catch (RuntimeException e) {
            throw e;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public BaseResult saveSaleOutOrder(Dto dto, Long memberId) {
        try {
            if (dto.getAsLong("id") != null) {
//                dto.put("order_no", orderNumberUtils.getOutOrderNo());
                dto.put("tableName", "norderSale");
                dto.put("method", "updateInfo");
                dto.put("creator", memberId);
                dto.put("storage_time", DateUtil.getStringFromDate(dto.getAsDate("storage_time"), "yyyy-MM-dd HH:mm:ss"));
                dto.put("out_stock", DateUtil.getStringFromDate(dto.getAsDate("out_stock"), "yyyy-MM-dd HH:mm:ss"));
                bizService.update(dto);
                //删除配好的库存
                Dto tDto = new BaseDto();
                tDto.put("tableName", "nwOrderSaleSku");
                tDto.put("method", "deleteOutOrderInfo");
                tDto.put("sale_id", dto.getAsString("id"));
                bizService.update(tDto);
                if (StringUtils.isNotEmpty(dto.getAsString("products"))) {
                    List<Map<String, Object>> products = JSONUtil.parseJSON2List(dto.getAsString("products"));
                    Map<String, Object> productsStock = JSONUtil.parseJSON2Map(dto.getAsString("stock_num"));
                    for (Map map : products) {
                        Dto product = new BaseDto();
                        product.putAll(map);
                        String productId = product.getAsString("id");
                        product.put("sale_id", dto.getAsString("id"));
                        product.put("buy_sku_id", product.getAsString("buy_sku_id"));
                        product.put("store_id", product.getAsString("id"));
                        product.put("roduction_date", product.getAsString("roduction_date"));
                        product.put("export_stock_num", productsStock.get("stock_" + product.getAsString("id")));
                        product.put("creator", memberId);
                        product.put("tableName", "nwOrderSaleSku");
                        product.put("method", "saveOutOrderInfo");
                        bizService.update(product);
                    }
                }
            } else {
                String order_no = "";
                try {
                    order_no = orderNumberUtils.getOutOrderNo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dto.put("order_no", order_no);
                dto.put("tableName", "norderSale");
                dto.put("creator", memberId);
                dto.put("storage_time", DateUtil.getStringFromDate(dto.getAsDate("storage_time"), "yyyy-MM-dd HH:mm:ss"));
                dto.put("out_stock", DateUtil.getStringFromDate(dto.getAsDate("out_stock"), "yyyy-MM-dd HH:mm:ss"));
                bizService.saveInfo(dto);
                if (StringUtils.isNotEmpty(dto.getAsString("products"))) {
                    List<Map<String, Object>> products = JSONUtil.parseJSON2List(dto.getAsString("products"));
                    Map<String, Object> productsStock = JSONUtil.parseJSON2Map(dto.getAsString("stock_num"));
                    for (Map map : products) {
                        Dto product = new BaseDto();
                        product.putAll(map);
                        String productId = product.getAsString("id");
                        product.put("sale_id", dto.getAsString("id"));
                        product.put("buy_sku_id", product.getAsString("buy_sku_id"));
                        product.put("store_id", product.getAsString("id"));
                        product.put("export_stock_num", productsStock.get("stock_" + product.getAsString("id")));
                        product.put("creator", memberId);
                        product.put("tableName", "nwOrderSaleSku");
                        product.put("method", "saveOutOrderInfo");
                        bizService.update(product);
                    }
                }
            }

        } catch (RuntimeException e) {
            throw e;
        }
        return null;
    }

    //保存库存分配
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public BaseResult saveSaleDistribution(Dto dto, Long memberId) {
        try {
            //如果分配完更新中间表状态
            if (dto.getAsLong("exportStatus") != null) {
                BaseDto eDto = new BaseDto();
                eDto.put("tableName", "orderSaleExportSku");
                eDto.put("method", "updateStatus");
                eDto.put("id", dto.getAsString("id"));
                bizService.update(eDto);
            }

            if (StringUtils.isNotEmpty(dto.getAsString("products"))) {
                List<Map<String, Object>> products = JSONUtil.parseJSON2List(dto.getAsString("products"));
                Map<String, Object> driverList = new HashMap<>();
                if (StringUtils.isNotEmpty(dto.getAsString("driver_list"))) {
                    driverList = JSONUtil.parseJSON2Map(dto.getAsString("driver_list"));
                }

                //查询当前出库产品对应的出库单id
                Dto resData = (Dto) bizService.queryForDto("orderSaleExportSku.getInfo", new BaseDto("id", dto.getAsString("id")));
                if (null != resData) {
                    for (Map map : products) {
                        Dto product = new BaseDto();
                        product.putAll(map);
                        String productId = product.getAsString("id");
                        product.put("sale_id", resData.getAsString("sale_id"));
                        product.put("batch_num", UUID.randomUUID().toString().replaceAll("-", ""));
                        product.put("buy_sku_id", product.getAsString("buy_sku_id"));
                        product.put("in_batch_number", product.getAsString("in_batch_number"));
                        product.put("store_id", product.getAsString("id"));
                        product.put("sku_id", product.getAsString("sku_id"));
                        product.put("out_stock_num", product.getAsString("out_stock_num"));
                        product.put("creator", memberId);
                        product.put("tableName", "nwOrderSaleSku");
                        bizService.saveInfo(product);
                        //修改库存
                        BaseDto sDto = new BaseDto();
                        sDto.put("in_batch_number", product.getAsString("in_batch_number"));
                        sDto.put("sku_id", product.getAsString("sku_id"));
                        sDto.put("provider_id", product.getAsString("provider_id"));
                        sDto.put("tableName", "orderStoreSku");
                        sDto.put("out_stock_num", product.getAsString("out_stock_num"));
                        sDto.put("method", "updateStatus");
                        bizService.update(sDto);
//                        if(product.getAsLong("out_stock_num")!=null){
//                            sDto.put("out_stock_num",product.getAsString("out_stock_num"));
//                            List<Dto> list=bizService.queryForList("orderStoreSku.queryByOutStockNum",sDto);
//                            if(list!=null){
//                                for (Dto lDto:list){
//                                    sDto.put("id",lDto.getAsString("id"));
//                                    bizService.update(sDto);
//                                }
//                            }
//                        }
                        if (driverList.get("keys_" + productId) != null) {
                            List<Map<String, Object>> productsDrivers = JSONUtil.parseJSON2List(JSON.toJSONString(driverList.get("keys_" + productId)));
                            if (null != productsDrivers && productsDrivers.size() > 0) {

                                for (Map mapDriver : productsDrivers) {
                                    Dto paramDriver = new BaseDto();
                                    paramDriver.put("tableName", "nwOrderSaleSku");
                                    paramDriver.put("method", "saveDriverInfo");
                                    paramDriver.put("sale_sku_id", product.getAsString("id"));
                                    paramDriver.put("driver_id", mapDriver.get("id"));
                                    paramDriver.put("creator", memberId);
                                    bizService.save(paramDriver);
                                }
                            }
                        }

                        ;
                    }
                }
            }
        } catch (RuntimeException e) {
            throw e;
        }
        return null;
    }

    //保存库存分配
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public BaseResult deleteOutOrderInfo(Dto dto, Long memberId) {
        BaseResult result = new BaseResult();
        try {
            BaseDto oDto = new BaseDto();
            oDto.put("tableName", "norderSale");
            oDto.put("method", "deleteInfo");
            oDto.put("id", dto.getAsString("id"));
            bizService.update(oDto);
            BaseDto eDto = new BaseDto();
            eDto.put("tableName", "nwOrderSaleSku");
            eDto.put("method", "deleteOutOrderInfo");
            eDto.put("sale_id", dto.getAsString("id"));
            bizService.update(eDto);
            result.setCode("0000");
            result.setMsg("操作成功");
        } catch (RuntimeException e) {
            throw e;
        }
        return result;
    }
}
