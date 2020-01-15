package com.dxtf.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dxtf.base.BaseMapper;
import com.dxtf.enums.SkuLogTypeEnum;
import com.dxtf.service.BizService;
import com.dxtf.service.OrderStoreSkuLogService;
import com.dxtf.service.ReturnGoodsService;
import com.dxtf.util.BaseResult;
import com.dxtf.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service("returnGoodsServiceImpl")
public class ReturnGoodsServiceImpl implements ReturnGoodsService {

    @Autowired
    public BizService bizService;
    @Autowired
    protected BaseMapper g4Dao;

    @Autowired
    private OrderStoreSkuLogService orderStoreSkuLogService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult saveReturnOrder(Dto dto, Dto member) throws Exception {
        BaseResult result = new BaseResult();
        Dto odto = new BaseDto();

        try {
            //保存信息
            if (StringUtils.isNotEmpty(dto.getAsString("id")) && dto.getAsString("id") != "") {

            } else {
                //生成退货单号
                String returnCode = "DXINSRETURN" + DateUtil.getStringFromDate(new Date(), "yyyyMMddHHmmss");
                dto.put("order_return", returnCode);
                dto.put("tableName", dto.getAsString("t"));
                dto.put("creator", member.get("id") == null ? "" : member.getAsString("id"));
                bizService.saveInfo(dto);

                //关联表插入 且 修改订货单批次表状态 将其改为“退货”status=4
                odto.put("creator", member.get("id") == null ? "" : member.getAsString("id"));
                odto.put("return_goods_id", dto.getAsString("id"));
                odto.put("tableName", dto.getAsString("t"));
                odto.put("method", "saveGoodsSkuInfo");

                Dto sdto = new BaseDto();
                sdto.put("tableName", "order_buy");
                sdto.put("method", "updateSkuStatus");
                sdto.put("status", 4);
                sdto.put("updator", member.get("id") == null ? "" : member.getAsString("id"));
                String[] skuids = dto.getAsString("sku_id").split(",");
                for (int i = 0; i < skuids.length; i++) {
                    odto.put("order_buy_sku_id", skuids[i]);
                    sdto.put("id", skuids[i]);
                    bizService.save(odto);
                    bizService.update(sdto);
                }
            }
            result.setData(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult deleteReturnInfo(Dto dto, Dto member) throws Exception {
        BaseResult result = new BaseResult();
        try {
            String[] arrChecked = dto.getAsString("ids").split(",");
            for (int i = 0; i < arrChecked.length; i++) {
                dto.put("id", arrChecked[i]);
                g4Dao.delete(dto.getAsString("tableName") + ".deleteInfo", dto);
            }
            Dto sdto = new BaseDto();
            sdto.put("tableName", "returnGoods");
            sdto.put("method", "deleteForReturnSku");
            //删除关联表信息
            List<Dto> list = bizService.queryForList("returnGoods.queryForSku", new BaseDto("return_goods_id", dto.getAsString("id")));
            for (Dto slist : list) {
                sdto.put("id", slist.getAsString("id"));
                bizService.delete(sdto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLocationTransfer(Dto dto, Dto member) throws Exception {

        dto.put("creator", member.get("id") == null ? "" : member.getAsString("id"));
        dto.put("tableName", "locationTransfer");
        dto.put("method", "saveInfo");
        dto.put("try_number", dto.getAsString("try_number"));
        dto.put("move_location", dto.getAsString("storage_location"));//移至货位
        dto.put("storage_location", dto.getAsString("setting_id"));//原货位
        Dto sdto = new BaseDto();
        sdto.put("tray_id", dto.getAsString("tray_id"));
        sdto.put("setting_id", dto.getAsString("setting_id"));

        String move_tray = dto.getAsString("move_tray");

        if (StringUtils.isNotEmpty(move_tray)) {
            sdto.put("in_batch_numbers", dto.getAsString("sections").split(","));
        }

        //查询出商品信息
        List<Dto> list = bizService.queryForList("wtray.queryOrderListForLocation", sdto);

        if (list != null && list.size() != 0) {
            for (Dto slist : list) {
                dto.put("product_name", slist.getAsString("productname"));
                dto.put("product_specifications", slist.getAsString("level"));
                dto.put("type_num", slist.getAsString("typenum"));
                dto.put("type_name", slist.getAsString("typename"));
                dto.put("num", slist.getAsString("numtotal"));//库存数量
                dto.put("distance_day", slist.getAsString("distance_day"));
                dto.put("production_time", slist.getAsString("roduction_date"));
                dto.put("expiration_time", slist.getAsString("expiration_date"));
                dto.put("product_code", slist.getAsString("number"));
                dto.put("client_name", slist.getAsString("provider_id"));
                dto.put("storage_time", slist.getAsString("storage_time"));//入库时间
                dto.put("order_num", slist.getAsString("bill"));//单据号
                dto.put("unit_volume", slist.getAsString("unit_volume"));//单位体积
                dto.put("unit_weight", slist.getAsString("unit_weight"));//单位重量
                dto.put("status", 0);

                if (slist.getAsInteger("is_expired_days") >= 0) {
                    dto.put("isexpired", 0);
                } else {
                    dto.put("isexpired", 1);
                }

                bizService.save(dto);
            }


            if (StringUtils.isEmpty(move_tray)) {

                //更新托盘表信息
                Dto bdto = new BaseDto();
                bdto.put("tableName", "wtray");
                bdto.put("method", "updateInfo");
                bdto.put("savelocation", dto.getAsString("move_location"));//存放货位
                bdto.put("id", dto.getAsString("tray_id"));
                bdto.put("updator", member.get("id") == null ? "" : member.getAsString("id"));
                bizService.update(bdto);

                //修改库存表 货位信息
                //查询出商品信息
                Dto adto = new BaseDto();
                adto.put("tableName", "locationTransfer");
                adto.put("method", "updateStoreInfo");
                adto.put("storage_location", dto.getAsString("move_location"));
                adto.put("tray_id", dto.getAsString("tray_id"));
                adto.put("setting_id", dto.getAsString("setting_id"));
                adto.put("updator", member.get("id") == null ? "" : member.getAsString("id"));
                bizService.update(adto);


            } else {

                String sections = dto.getAsString("sections");
                if (StringUtils.isNotEmpty(sections)) {
                    //修改库存表 货位信息
                    //查询出商品信息
                    Dto adto = new BaseDto();
                    adto.put("tableName", "locationTransfer");
                    adto.put("method", "updateStoreTray");
                    adto.put("storage_location", dto.getAsString("move_location"));
                    adto.put("tray_id", dto.getAsString("tray_id"));
                    adto.put("move_tray", move_tray);
                    adto.put("setting_id", dto.getAsString("setting_id"));
                    adto.put("in_batch_numbers", dto.getAsString("sections").split(","));
                    adto.put("updator", member.get("id") == null ? "" : member.getAsString("id"));
                    bizService.update(adto);

                }


            }


        } else {
            throw new Exception("暂无商品可转移货位！，请重新选择");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNotorderout(Dto dto, Dto member) throws Exception {

        dto.put("creator", member.get("id") == null ? "" : member.getAsString("id"));
        dto.put("tableName", "wOrderNoSaleSku");
        dto.put("method", "saveInfo");
        dto.put("tray_number", dto.getAsString("try_number"));

        //产品信息
        String productData = dto.getAsString("productData");
        List<Dto> list = new ArrayList<>();

        if (StringUtils.isNotEmpty(productData)) {
            JSONArray productDataList = JSONArray.parseArray(productData);

            for (int i = 0; i < productDataList.size(); i++) {

                JSONObject siteSettin = productDataList.getJSONObject(i);

                Map map = JSON.parseObject(siteSettin.toJSONString(), Map.class);

                list.add(getParamAsDto(map));

            }

        }
        if (list != null && list.size() != 0) {
            for (Dto slist : list) {
                dto.put("sku_name", slist.getAsString("productname"));
                dto.put("sku_level", slist.getAsString("level"));
                dto.put("sku_type_number", slist.getAsString("typenum"));
                dto.put("sku_type_text", slist.getAsString("typename"));
                dto.put("num", slist.getAsString("numtotal"));
                dto.put("distance_day", slist.getAsString("distance_day"));
                dto.put("roduction_date", slist.getAsString("roduction_date"));
                dto.put("expiration_date", slist.getAsString("expiration_date"));
                dto.put("sku_number", slist.getAsString("number"));
                dto.put("in_batch_number", slist.getAsString("in_batch_number"));
                dto.put("client_name", slist.getAsString("client_name"));
                dto.put("customer_id", slist.getAsString("provider_id"));
                dto.put("instock_time", slist.getAsString("storage_time"));//入库时间
                dto.put("bill", slist.getAsString("bill"));//单据号
                dto.put("setting_id", slist.getAsString("setting_id"));

                bizService.save(dto);
                //修改库存信息
                Dto adto = new BaseDto();
                adto.put("tableName", "wOrderNoSaleSku");
                adto.put("method", "updateStoreInfo");
                adto.put("storage_location", dto.getAsString("storage_location"));
                adto.put("status", 2);
                adto.put("tray_id", dto.getAsString("tray_id"));
                adto.put("setting_id", dto.getAsString("setting_id"));
                adto.put("in_batch_number", slist.getAsString("in_batch_number"));
                adto.put("updator", member.get("id") == null ? "" : member.getAsString("id"));
                bizService.update(adto);

                orderStoreSkuLogService.insertLog(member.getAsLong("id"), slist.getAsLong("sku_id"), SkuLogTypeEnum.NO_ORDER_OUTSTORE.getCode(), slist.getAsInteger("numtotal"),
                        slist.getAsString("productname"), slist.getAsString("in_batch_number"), dto.getAsString("number"));

            }


            //托盘发生变化
            //托盘，库位都发生变化
            //根据tray_id和setting_id查询该托盘是否空余
            Dto trayDto = new BaseDto();
            trayDto.put("tray_id", dto.getAsString("tray_id"));
            trayDto.put("setting_id", dto.getAsString("setting_id"));
            BaseDto countDto = (BaseDto) bizService.queryForObject("outSku.queryOrderStoreSku", trayDto);
            //托盘空余更新 savelocation
            if (countDto.getAsLong("total") == 0) {
                Dto update = new BaseDto();
                update.put("tableName", "wtray");
                update.put("method", "updateSavelocation");
                update.put("savelocation", null);
                update.put("updator", member.get("id") == null ? "" : member.getAsString("id"));
                update.put("id", dto.getAsString("tray_id"));
                bizService.update(update);
            }


        }

    }

    private Dto getParamAsDto(Map map) {
        Dto dto = new BaseDto();
        Iterator keyIterator = map.keySet().iterator();

        while (keyIterator.hasNext()) {
            String key = (String) keyIterator.next();
            String value = String.valueOf(map.get(key));

            dto.put(key.trim(), value.trim());
        }

        return dto;
    }


}
