package com.dxtf.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dxtf.enums.SkuLogTypeEnum;
import com.dxtf.service.BizService;
import com.dxtf.service.InStockService;
import com.dxtf.service.OrderStoreSkuLogService;
import com.dxtf.util.*;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("inStockServiceImpl")
public class InStockServiceImpl implements InStockService {

    //处理入库数据
    public static String DEAL_WITH_W_ORDER_BUY_CHECKED = "deal_with_w_order_buy_checked_";


    @Autowired
    public RedisService redisService;

    @Autowired
    public BizService bizService;

    @Autowired
    private OrderStoreSkuLogService orderStoreSkuLogService;

    @Autowired
    public OrderNumberUtils orderNumberUtils;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public BaseResult saveOrder(Dto dto, Long memberId) throws Exception {
        BaseResult result = new BaseResult();
        try {

            String tableData = dto.getAsString("tableData");
            if (StringUtils.isEmpty(tableData)) {
                result.setCode(StatusConstant.CODE_9999);
                result.setMsg("产品信息不能为空");
                return result;
            }

            String id = dto.getAsString("id");
            if (StringUtils.isEmpty(id)) {
                //第一次入库
                //当填写具体的入库产品明细后，自动生库单号，同一个批次的产品生成一个同一个出库单号。DXINSTOCK2019110401
                String order_no = orderNumberUtils.getIntoOrderNo();
                dto.put("creator", memberId);
                dto.put("updator", memberId);
                dto.put("order_no", order_no);
                dto.put("tableName", "order_buy");
                bizService.saveInfo(dto);
                //订货单批次表
                String orderId = dto.getAsString("id");

                JSONArray objects = JSONArray.parseArray(tableData);
                for (int i = 0; i < objects.size(); i++) {
                    JSONObject jsonObject = objects.getJSONObject(i);
                    Dto update = new BaseDto();
                    update.put("name", jsonObject.getString("name"));
                    update.put("level", jsonObject.getString("level"));
                    update.put("num", jsonObject.getString("num"));
                    update.put("roduction_date", jsonObject.getString("roduction_date"));
                    update.put("expiration_date", jsonObject.getString("expiration_date"));
                    update.put("order_id", orderId);
                    update.put("sku_id", jsonObject.getString("sku_id"));
                    update.put("shelflife", jsonObject.getIntValue("shelflife"));

                    Boolean isTransfer = jsonObject.get("isTransfer") == null ? false : jsonObject.getBoolean("isTransfer");
                    if (isTransfer) {
                        update.put("transfer_store", jsonObject.getString("transfer_store"));
                        update.put("transfer_store_address", jsonObject.getString("transfer_store_address"));
                        update.put("transfer_people", jsonObject.getString("transfer_people"));
                        update.put("transfer_people_mobile", jsonObject.getString("transfer_people_mobile"));
                        update.put("transfer_expect_time", jsonObject.getString("transfer_expect_time"));
                        update.put("transfer_end_time", jsonObject.getString("transfer_end_time"));
                        update.put("is_transfer", 0);
                    } else {
                        update.put("is_transfer", 1);
                    }


                    //通过保质期计算过期日期
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date rdt = sdf.parse(jsonObject.getString("roduction_date"));
                        Calendar ca = Calendar.getInstance();
                        ca.setTime(rdt);
                        ca.add(Calendar.DATE, jsonObject.getIntValue("shelflife"));
                        String expiration_date = sdf.format(ca.getTime());
                        update.put("expiration_date", expiration_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //生产日期
                    String roduction_date = jsonObject.getString("roduction_date");
                    update.put("number", roduction_date.replaceAll("-", "").replaceAll(":", "").replaceAll("\\s*", ""));
                    update.put("tableName", "orderBuySku");
                    update.put("creator", memberId);

                    bizService.saveInfo(update);

                    //入库商品id
                    String buy_sku_id = update.getAsString("id");


                    Object driverData = jsonObject.get("driverData");

                    dealwithDriver(driverData, buy_sku_id, memberId);
                }
//                //保存收货人信息
                BaseDto recipient = (BaseDto) bizService.queryForObject("recipient.getInfoByRecipientStore", new BaseDto("recipient_store", dto.getAsString("recipient_store")));

                BaseDto sDto = new BaseDto();
                sDto.put("recipient_store", dto.getAsString("recipient_store"));
                sDto.put("recipient_store_address", dto.getAsString("recipient_store_address"));
                sDto.put("recipient_people", dto.getAsString("recipient_people"));
                sDto.put("recipient_people_mobile", dto.getAsString("recipient_people_mobile"));
                sDto.put("creator", memberId);
                sDto.put("tableName", "recipient");

                if (recipient != null) {
                    sDto.put("id", recipient.getAsString("id"));
                    bizService.updateInfo(sDto);
                } else {
                    bizService.saveInfo(sDto);
                }

                BaseDto supplier = (BaseDto) bizService.queryForObject("supplier.getInfoBySupplierCompany", new BaseDto("supplier_company", dto.getAsString("supplier_company")));

                BaseDto rDto = new BaseDto();
                rDto.put("supplier_company", dto.getAsString("supplier_company"));
                rDto.put("supplier_people", dto.getAsString("supplier_people"));
                rDto.put("supplier_people_mobile", dto.getAsString("supplier_people_mobile"));
                rDto.put("carrier_company", dto.getAsString("carrier_company"));
                rDto.put("creator", memberId);
                rDto.put("tableName", "supplier");

                //保存供应商信息
                if (supplier != null) {
                    rDto.put("id", supplier.getAsString("id"));
                    bizService.updateInfo(rDto);
                } else {
                    bizService.saveInfo(rDto);
                }


            } else {

                if (dto.getAsString("status").equals("2")) {
                    dto.put("status", 0);
                }
                //修改
                dto.put("updator", memberId);
                dto.put("tableName", "order_buy");
                bizService.updateInfo(dto);

                //订货单批次表
                String orderId = dto.getAsString("id");

                JSONArray objects = JSONArray.parseArray(tableData);
                for (int i = 0; i < objects.size(); i++) {
                    JSONObject jsonObject = objects.getJSONObject(i);
                    Dto update = new BaseDto();
                    update.put("name", jsonObject.getString("name"));
                    update.put("level", jsonObject.getString("level"));
                    update.put("num", jsonObject.getString("num"));
                    update.put("roduction_date", jsonObject.getString("roduction_date"));
                    update.put("expiration_date", jsonObject.getString("expiration_date"));
                    update.put("order_id", orderId);
                    update.put("sku_id", jsonObject.getString("sku_id"));
                    update.put("shelflife", jsonObject.getIntValue("shelflife"));

                    //通过保质期计算过期日期
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date rdt = sdf.parse(jsonObject.getString("roduction_date"));
                        Calendar ca = Calendar.getInstance();
                        ca.setTime(rdt);
                        ca.add(Calendar.DATE, jsonObject.getIntValue("shelflife"));
                        String expiration_date = sdf.format(ca.getTime());
                        update.put("expiration_date", expiration_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Boolean isTransfer = jsonObject.get("isTransfer") == null ? false : jsonObject.getBoolean("isTransfer");
                    if (isTransfer) {
                        update.put("transfer_store", jsonObject.getString("transfer_store"));
                        update.put("transfer_store_address", jsonObject.getString("transfer_store_address"));
                        update.put("transfer_people", jsonObject.getString("transfer_people"));
                        update.put("transfer_people_mobile", jsonObject.getString("transfer_people_mobile"));
                        update.put("transfer_expect_time", jsonObject.getString("transfer_expect_time"));
                        update.put("transfer_end_time", jsonObject.getString("transfer_end_time"));
                        update.put("is_transfer", 0);
                    } else {
                        update.put("is_transfer", 1);
                    }

                    //生产日期
                    String roduction_date = jsonObject.getString("roduction_date");
                    update.put("number", roduction_date.replaceAll("-", "").replaceAll(":", "").replaceAll("\\s*", ""));
                    update.put("tableName", "orderBuySku");
                    update.put("creator", memberId);

                    //入库商品id
                    String buy_sku_id = "";
                    Object skuID = jsonObject.get("id");

                    if (skuID == null || StringUtils.isEmpty(ObjectUtils.toString(skuID, ""))) {
                        bizService.saveInfo(update);
                        buy_sku_id = update.getAsString("id");
                    } else {
                        update.put("id", skuID + "");

                        bizService.updateInfo(update);
                        buy_sku_id = ObjectUtils.toString(skuID, "");
                    }


                    Dto delete = new BaseDto();
                    delete.put("buy_sku_id", buy_sku_id);
                    delete.put("tableName", "orderBuySku");
                    delete.put("method", "deleteOrderBuySkuDriver");
                    bizService.update(delete);

                    Object driverData = jsonObject.get("driverData");
                    dealwithDriver(driverData, buy_sku_id, memberId);

                }
                //保存收货人信息
                BaseDto recipient = (BaseDto) bizService.queryForObject("recipient.getInfoByRecipientStore", new BaseDto("recipient_store", dto.getAsString("recipient_store")));
                if (recipient != null) {
                    BaseDto sDto = new BaseDto();
                    sDto.put("id", recipient.getAsString("id"));
                    sDto.put("recipient_store", dto.getAsString("recipient_store"));
                    sDto.put("recipient_store_address", dto.getAsString("recipient_store_address"));
                    sDto.put("recipient_people_mobile", dto.getAsString("recipient_people_mobile"));
                    sDto.put("recipient_people", dto.getAsString("recipient_people"));
                    sDto.put("updator", memberId);
                    sDto.put("tableName", "recipient");
                    bizService.updateInfo(sDto);
                } else {
                    BaseDto sDto = new BaseDto();
                    sDto.put("recipient_store", dto.getAsString("recipient_store"));
                    sDto.put("recipient_store_address", dto.getAsString("recipient_store_address"));
                    sDto.put("recipient_people", dto.getAsString("recipient_people"));
                    sDto.put("recipient_people_mobile", dto.getAsString("recipient_people_mobile"));
                    sDto.put("creator", memberId);
                    sDto.put("tableName", "recipient");
                    bizService.saveInfo(sDto);
                }

                BaseDto supplier = (BaseDto) bizService.queryForObject("supplier.getInfoBySupplierCompany", new BaseDto("supplier_company", dto.getAsString("supplier_company")));
                //保存供应商信息
                if (supplier != null) {
                    BaseDto rDto = new BaseDto();
                    rDto.put("id", supplier.getAsString("id"));
                    rDto.put("supplier_company", dto.getAsString("supplier_company"));
                    rDto.put("supplier_people", dto.getAsString("supplier_people"));
                    rDto.put("supplier_people_mobile", dto.getAsString("supplier_people_mobile"));
                    rDto.put("carrier_company", dto.getAsString("carrier_company"));
                    rDto.put("updator", memberId);
                    rDto.put("tableName", "supplier");
                    bizService.updateInfo(rDto);
                } else {
                    BaseDto rDto = new BaseDto();
                    rDto.put("supplier_company", dto.getAsString("supplier_company"));
                    rDto.put("supplier_people", dto.getAsString("supplier_people"));
                    rDto.put("supplier_people_mobile", dto.getAsString("supplier_people_mobile"));
                    rDto.put("carrier_company", dto.getAsString("carrier_company"));
                    rDto.put("creator", memberId);
                    rDto.put("tableName", "supplier");
                    bizService.saveInfo(rDto);
                }
            }


        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public BaseResult doQualityProduct(Dto dto, Long memberId) throws Exception {
        BaseResult result = new BaseResult();
        try {

            //待检卡集合
            String ids = dto.getAsString("ids");
            if (StringUtils.isEmpty(ids)) {
                throw new Exception("请选择产品");
            }


            String status = dto.getAsString("status");
            if (StringUtils.isEmpty(status)) {
                throw new Exception("请选择质检状态");
            }

            String[] idList = ids.split(",");

            for (String id : idList) {
                if (StringUtils.isEmpty(id)) {
                    continue;
                }
                //查询订货单批次表数据
                Dto orderBuySku = (Dto) bizService.queryForObject("orderBuySku.getInfo", new BaseDto("id", id));
                if (orderBuySku != null && orderBuySku.getAsInteger("status") == 1) {
                    //质检中
                    //修改状态
                    //状态 0 待质检 1 质检中 2 质检通过  3 质检驳回
                    if (status.equals("3")) {
                        orderBuySku.put("status", 3);
                        orderBuySku.put("tableName", "orderBuySku");
                        orderBuySku.put("updator", memberId);
                        bizService.updateInfo(orderBuySku);
                    } else if (status.equals("2")) {
                        orderBuySku.put("status", 2);
                        orderBuySku.put("tableName", "orderBuySku");
                        orderBuySku.put("updator", memberId);
                        bizService.updateInfo(orderBuySku);
                    }
                }

            }

        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public BaseResult printQualityCard(Dto dto, Long memberId) throws Exception {
        BaseResult result = new BaseResult();
        try {

            //待检卡集合
            String ids = dto.getAsString("ids");
            if (StringUtils.isEmpty(ids)) {
                throw new Exception("请选择产品");
            }


            String[] idList = ids.split(",");

            for (String id : idList) {
                if (StringUtils.isEmpty(id)) {
                    continue;
                }
                //查询订货单批次表数据
                Dto orderBuySku = (Dto) bizService.queryForObject("orderBuySku.getInfo", new BaseDto("id", id));
                if (orderBuySku != null && orderBuySku.getAsInteger("status") == 0) {
                    //质检中
                    //修改状态
                    //状态 0 待质检 1 质检中 2 质检通过  3 质检驳回

                    orderBuySku.put("status", 1);
                    orderBuySku.put("tableName", "orderBuySku");
                    orderBuySku.put("updator", memberId);
                    bizService.updateInfo(orderBuySku);

                }
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }


    /**
     * 处理司机相关
     *
     * @param driverData
     * @param buy_sku_id
     * @param memberId
     */

    public void dealwithDriver(Object driverData, String buy_sku_id, Long memberId) {
        if (driverData != null) {
            List<Map> driverList = JSON.parseObject(JSON.toJSONString(driverData), ArrayList.class);
            for (Map driver : driverList) {

                String driver_number = ObjectUtils.toString(driver.get("driver_number"), "");
                String mobile = ObjectUtils.toString(driver.get("mobile"), "");
                String driver_type = ObjectUtils.toString(driver.get("driver_type"), "");
                String drivername = ObjectUtils.toString(driver.get("drivername"), "");
                String driverId = ObjectUtils.toString(driver.get("id"), "");

                boolean isUpdate = true;
                if (StringUtils.isNotEmpty(driverId)) {
                    Dto driverDto = (Dto) bizService.queryForObject("wdriver.getInfo", new BaseDto("driver_number", driver_number));
                    if (driverDto != null) {
                        driverId = driverDto.getAsString("id");

                        //判断司机信息是否更改
                        if (StringUtils.equals(mobile, driverDto.getAsString("mobile"))
                                && StringUtils.equals(driver_type, driverDto.getAsString("driver_type"))
                                && StringUtils.equals(drivername, driverDto.getAsString("drivername"))) {

                            //不需要更新
                            isUpdate = false;
                        }
                    }
                }


                if (StringUtils.isEmpty(driverId)) {
                    //插入
                    Dto driverinfo = new BaseDto();
                    driverinfo.put("drivername", drivername);
                    driverinfo.put("mobile", mobile);
                    driverinfo.put("driver_type", driver_type);
                    driverinfo.put("driver_number", driver_number);
                    driverinfo.put("status", 0);
                    driverinfo.put("creator", memberId);
                    driverinfo.put("tableName", "wdriver");

                    bizService.saveInfo(driverinfo);
                    driverId = driverinfo.getAsString("id");

                } else {

                    if (isUpdate) {
                        //更新
                        Dto driverinfo = new BaseDto();
                        driverinfo.put("drivername", drivername);
                        driverinfo.put("mobile", mobile);
                        driverinfo.put("driver_type", driver_type);
                        driverinfo.put("driver_number", driver_number);
                        driverinfo.put("updator", memberId);
                        driverinfo.put("tableName", "wdriver");
                        driverinfo.put("id", driverId);
                        bizService.updateInfo(driverinfo);
                    }

                }

                Dto updatebuyskudriver = new BaseDto();
                updatebuyskudriver.put("buy_sku_id", buy_sku_id);
                updatebuyskudriver.put("driver_id", driverId);
                updatebuyskudriver.put("creator", memberId);
                updatebuyskudriver.put("tableName", "orderBuySku");
                updatebuyskudriver.put("method", "saveOrderBuySkuDriver");
                bizService.save(updatebuyskudriver);

            }
        }
    }


    /**
     * Order订单号生成
     *
     * @return Dto
     */
    private synchronized String getOrderNo() throws Exception {
        String no = redisService.getValue("DXINSTOCK_ORDER_BUY");
        if (StringUtils.isNotEmpty(no)) {
            redisService.setValue("DXINSTOCK_ORDER_BUY", String.valueOf(Integer.parseInt(no) + 1));
            return "DXINSTOCK" + DateUtil.getTodayDate() + intToString((Integer.parseInt(no) + 1), 4);
        } else {
            //获取当天时间差
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat parseTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long seconds = parseTime.parse(df.format(new Date()) + " 23:59:59").getTime() - new Date().getTime();
            redisService.setValue("DXINSTOCK_ORDER_BUY", "1", seconds);
            return "DXINSTOCK" + DateUtil.getTodayDate() + "0001";
        }
    }

    private String intToString(int num, int length) {
        String str = String.valueOf(num);
        int a = length - str.length();
        String result = "";
        if (a > 0) {
            for (int i = 0; i < a; i++) {
                result += "0";
            }
            result += num;
        } else {
            result = str;
        }
        return result;
    }

    public BaseResult deleteOrderBuySkuDriver(Dto dto) throws Exception {
        BaseResult result = new BaseResult();
        try {
            BaseDto sDto = new BaseDto();
            sDto.put("id", dto.getAsString("id"));
            sDto.put("method", "deleteOrderBuySku");
            sDto.put("tableName", "orderBuySku");
            bizService.update(sDto);
            BaseDto dDto = new BaseDto();
            dDto.put("buy_sku_id", dto.getAsString("id"));
            dDto.put("method", "deleteOrderBuySkuDriver");
            dDto.put("tableName", "orderBuySku");
            bizService.update(dDto);
            result.setCode("0000");
            result.setMsg("删除成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }


    @Override
    public BaseResult intoStoreBuySku(Dto dto, Long memberId) throws Exception {
        BaseResult result = new BaseResult();
        try {

            //待检信息
            String id = dto.getAsString("id");
            dto.put("status", "2");

            //查询当前入库总条数
            Dto countdto = (Dto) bizService.queryForObject("worderinstore.queryBatchCount", new BaseDto("buy_checked_id", dto.getAsString("buy_checked_id")));

            Integer total = countdto.getAsInteger("total");
            total++;


            //入库
            dealWithInStoreOrder(dto, memberId, total);

            //查询批次号
            Dto worderinstore = (Dto) bizService.queryForObject("worderinstore.getInfo", new BaseDto("id", dto.getAsString("id")));
            if (worderinstore == null) {
                throw new Exception("入库失败，请重试");
            }

            if (StringUtils.isEmpty(dto.getAsString("inStockFlag"))) {
                //批量更新数据入库存
                batchIntoOrderStoreSku(worderinstore, memberId);
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }


    @Override
    public BaseResult saveStoreSku(Dto dto, Long memberId) throws Exception {
        BaseResult result = new BaseResult();
        try {

            //待检信息
            String id = dto.getAsString("id");
            if (StringUtils.isEmpty(id)) {
                throw new Exception("请选择待检信息");
            }

            //状态 0：待入库  1：已入库
            // dto.put("status", 1);

            //入库时间
            dto.put("instock_time", DateUtil.getStringFromDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            //到货日期
            dto.put("arrival_date", DateUtil.getStringFromDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            dto.put("checked_warehouse_id", null);
            dto.put("warehouse_site_setting", null);
            dto.put("tableName", "order_buy_checked");
            dto.put("updator", memberId);

            bizService.updateInfo(dto);

            //货位信息
            String siteSettingData = dto.getAsString("siteSettingData");

            List<String> errorlist = new ArrayList<>();

            if (StringUtils.isNotEmpty(siteSettingData)) {

                JSONArray siteSettingList = JSONArray.parseArray(siteSettingData);

                //查询当前入库总条数
                Dto countdto = (Dto) bizService.queryForObject("worderinstore.queryBatchCount", new BaseDto("buy_checked_id", dto.getAsString("buy_checked_id")));

                Integer total = countdto.getAsInteger("total");

                for (int i = 0; i < siteSettingList.size(); i++) {

                    JSONObject siteSettin = siteSettingList.getJSONObject(i);


                    Map instore = JSON.parseObject(siteSettin.toJSONString(), Map.class);

                    //小批次号+1
                    total++;
                    String errorMsg = dealWithInStoreOrder(getParamAsDto(instore), memberId, total);

                    if (StringUtils.isNotEmpty(errorMsg)) {
                        errorlist.add(errorMsg);
                    }

                }
                //获取合格品数量
                Dto stock = (Dto) bizService.queryForObject("worderinstore.queryStockCount", new BaseDto("buy_checked_id", dto.getAsString("id")));
                Dto sdto = new BaseDto();
                sdto.put("id", dto.getAsString("id"));
                sdto.put("tableName", "order_buy_checked");
                if (stock != null) {
                    if (dto.getAsInteger("qualified_num") <= (stock.getAsInteger("total"))) {
                        sdto.put("status", 1);
                        bizService.updateInfo(sdto);
                    }
                }

            }

            if (!errorlist.isEmpty()) {
                result.setData(String.join(",", errorlist));
            }


        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void batchIntoOrderStoreSku(Dto next, Long memberId) {


        //待检统计表id
        String id = next.getAsString("id");


        try {

            String value = redisService.getValue(DEAL_WITH_W_ORDER_BUY_CHECKED + id);

            if (StringUtils.isNotEmpty(value)) {
                //已经在处理
                return;
            }

            //设置处理标识
            redisService.setValue(DEAL_WITH_W_ORDER_BUY_CHECKED + id, id, 120l);


            //查询批次号
            Dto worderinstore = (Dto) bizService.queryForObject("worderinstore.getInfo", new BaseDto("id", id));
            if (worderinstore.getAsInteger("status") == 2) {
                //已经入库
                return;
            }


            //批次号
            String batchNumber = next.getAsString("batch_number");

            Integer num = next.getAsInteger("stock");

            List<Dto> insertList = new ArrayList<>();

            for (int j = 0; j < num; j++) {
                //插入库存表
                Dto insert = new BaseDto();
                insert.put("buy_sku_id", next.getAsString("buy_sku_id"));
                insert.put("buy_checked_id", next.getAsString("buy_checked_id"));
                insert.put("sku_id", next.getAsString("sku_id"));
                insert.put("tray_id", next.getAsString("tray_id"));
                insert.put("provider_id", next.getAsString("provider_id"));
                insert.put("setting_id", next.getAsString("setting_id"));
                insert.put("status", "0");
                insert.put("stock", "1");
                insert.put("creator", memberId);
                insert.put("updator", memberId);
                insert.put("in_batch_number", batchNumber);
                insert.put("store_sku_number", batchNumber + "@" + (j + 1) + "/" + num);
                insert.put("tableName", "norderStoreSku");
                insert.put("method", "saveInfo");


                insertList.add(insert);
            }

            //更新订单状态为2：已入库
            //插入库存表
            Dto insert = new BaseDto();
            insert.put("updator", memberId);
            insert.put("status", "2");
            insert.put("tableName", "worderinstore");
            insert.put("id", id);
            bizService.updateInfo(insert);

            //插入入库数据
            bizService.insertBatch(insertList);

            orderStoreSkuLogService.insertLog(memberId, next.getAsLong("sku_id"), SkuLogTypeEnum.INSTORE.getCode(), num, worderinstore.getAsString("sku_name"), batchNumber, worderinstore.getAsString("order_no"));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;

        } finally {
            //删除缓存数据
            redisService.delete(DEAL_WITH_W_ORDER_BUY_CHECKED + id);
        }


    }


    /**
     * 处理入库订单表
     *
     * @param dto
     * @param memberId
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public String dealWithInStoreOrder(Dto dto, Long memberId, int i) {


        if (StringUtils.isEmpty(dto.getAsString("tray_id"))) {
            return "";
        }

        String setting_id = dto.getAsString("setting_id");

        //查询货位信息
        Dto query = new BaseDto();
        query.put("tableName", "warehouseSiteSetting");
        query.put("id", setting_id);

        List<Dto> settingList = bizService.queryForList("warehouseSiteSetting.queryList", query);
        if (settingList.isEmpty()) {
            return dto.getAsString("setting_number") + "不存在";
        }
        //占用托盘id
        String w_tray_id = settingList.get(0).getAsString("w_tray_id");
        int num = dto.getAsInteger("stock");

        if (num <= 0) {
            return "";
        }

        String tray_id = dto.getAsString("tray_id");

        if (StringUtils.isNotEmpty(w_tray_id) && !StringUtils.equals(w_tray_id, tray_id)) {
            return dto.getAsString("setting_number") + "已经被占用";
        }

        String batch_number = dto.getAsString("buy_checked_id") + "_" + dto.getAsString("order_buy_sku_number") + "_" + (i);

        String instockId = dto.getAsString("id");


        String value = redisService.getValue("DEAL_WITH_INSTORE_ORDER" + batch_number);

        if (StringUtils.isNotEmpty(value)) {
            //已经在处理
            return "";
        }

        //设置处理标识
        redisService.setValue("DEAL_WITH_INSTORE_ORDER" + batch_number, batch_number, 120l);


        try {
            if (StringUtils.isEmpty(instockId)) {
                //插入库存表
                Dto insert = new BaseDto();
                insert.put("buy_sku_id", dto.getAsString("buy_sku_id"));
                insert.put("buy_checked_id", dto.getAsString("buy_checked_id"));
                insert.put("sku_id", dto.getAsString("sku_id"));
                insert.put("tray_id", tray_id);
                insert.put("provider_id", dto.getAsString("provider_id"));
                insert.put("setting_id", setting_id);
                insert.put("status", "0");
                insert.put("stock", num);
                insert.put("creator", memberId);
                insert.put("updator", memberId);
                insert.put("batch_number", batch_number);
                insert.put("tableName", "worderinstore");
                bizService.saveInfo(insert);

                dto.put("id", insert.getAsString("id"));

                //更新托盘表
                Dto update = new BaseDto();
                update.put("tableName", "wtray");
                update.put("savelocation", setting_id);
                update.put("updator", memberId);
                update.put("id", tray_id);
                bizService.updateInfo(update);

            } else {
                //查询入库信息
                Dto instock = (Dto) bizService.queryForObject("worderinstore.getInfo", new BaseDto("id", instockId));

                if (instock == null) {
                    return "";
                }

                //现有托盘id
                String trayid = instock.getAsString("tray_id");

                //数量
                Integer stock = instock.getAsInteger("stock");
                //货位
                String settingId = instock.getAsString("setting_id");

                boolean isUpdate = false;

                if (StringUtils.equals(tray_id, trayid) && StringUtils.equals(settingId, setting_id)) {
                    //托盘信息,库位未发生改变
                    if (stock == dto.getAsInteger("stock") && instock.getAsInteger("status") == dto.getAsInteger("status")) {
                        //库存未发生变化

                    } else {
                        isUpdate = true;
                    }

                } else {
                    isUpdate = true;
                    if (StringUtils.equals(tray_id, trayid) && !StringUtils.equals(settingId, setting_id)) {
                        //货位发送变换

                        //更新托盘表
                        Dto update = new BaseDto();
                        update.put("tableName", "wtray");
                        update.put("savelocation", setting_id);
                        update.put("updator", memberId);
                        update.put("id", tray_id);
                        bizService.updateInfo(update);

                    } else {
                        //托盘发生变化
                        //托盘，库位都发生变化
                        Dto update = new BaseDto();
                        update.put("tableName", "wtray");
                        update.put("method", "updateSavelocation");
                        update.put("savelocation", null);
                        update.put("updator", memberId);
                        update.put("id", trayid);
                        bizService.update(update);

                        //更新新托盘信息
                        update.clear();

                        update.put("tableName", "wtray");
                        update.put("savelocation", setting_id);
                        update.put("updator", memberId);
                        update.put("id", tray_id);
                        bizService.updateInfo(update);
                    }
                }

                if (isUpdate) {
                    //插入库存表
                    Dto insert = new BaseDto();
                    insert.put("setting_id", setting_id);
                    insert.put("stock", num);
                    insert.put("updator", memberId);
                    insert.put("tray_id", tray_id);
                    insert.put("status", dto.getAsString("status"));
                    insert.put("tableName", "worderinstore");
                    insert.put("id", instockId);
                    bizService.updateInfo(insert);
                }

            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        } finally {
            //删除缓存数据
            redisService.delete("DEAL_WITH_INSTORE_ORDER" + batch_number);
        }
        return "";

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


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Dto upSkuStatus(Dto dto) throws Exception {
        Dto returnDto = new BaseDto();
        //查询当前sale_sku信息
        Dto saleSku = (Dto) bizService.queryForObject("outSku.getInfo", dto);
        if (null != saleSku && saleSku.getAsInteger("status") == 49) {
            //查询w_order_sale_export_sku发货单是否已全部分配完成
            BaseDto baseDto = new BaseDto("sale_id", saleSku.getAsString("sale_id"));
            baseDto.put("status", 0);
            List<Dto> listExport = bizService.queryForList("orderSaleExportSku.queryList", baseDto);
            //未分配完，只更新子订单
            if (listExport.size() != 0) {
                //更新子订单
                BaseDto sonDto = new BaseDto();
                sonDto.put("tableName", "outSku");
                sonDto.put("method", "updateSkuStatus");
                sonDto.put("id", dto.getAsString("id"));
                bizService.update(sonDto);
                //根据批次号和出库数量更新库存表
                Dto storeDto = new BaseDto();
                storeDto.put("num", saleSku.getAsString("out_stock_num"));
                storeDto.put("in_batch_number", saleSku.getAsString("in_batch_number"));
                storeDto.put("tableName", "outSku");
                storeDto.put("method", "updateBuyStatus");
                bizService.update(storeDto);
            } else {//全部分配完成
                //查询w_order_sale_sku自订单是否全部出库
                List<Dto> list = bizService.queryForList("outSku.queryListById", new BaseDto("sale_id", saleSku.getAsString("sale_id")));
                if (list.size() == 1) {
                    //更新子订单
                    BaseDto sonDto = new BaseDto();
                    sonDto.put("tableName", "outSku");
                    sonDto.put("method", "updateSkuStatus");
                    sonDto.put("id", dto.getAsString("id"));
                    bizService.update(sonDto);
                    //更新主订单
                    Dto fdto = new BaseDto();
                    fdto.put("tableName", "outSku");
                    fdto.put("method", "updateParentSkuStatus");
                    fdto.put("sale_id", saleSku.getAsString("sale_id"));
                    bizService.update(fdto);
                    //根据批次号和出库数量更新库存表
                    Dto storeDto = new BaseDto();
                    storeDto.put("num", saleSku.getAsString("out_stock_num"));
                    storeDto.put("in_batch_number", saleSku.getAsString("in_batch_number"));
                    storeDto.put("tableName", "outSku");
                    storeDto.put("method", "updateBuyStatus");
                    bizService.update(storeDto);
                    returnDto.put("in_batch_number", saleSku.getAsString("in_batch_number"));
                    //根据批次号查询tray_id和setting_id
                    Dto trayDto = new BaseDto();
                    trayDto.put("in_batch_number", saleSku.getAsString("in_batch_number"));
                    trayDto = (BaseDto) bizService.queryForObject("outSku.queryTraidAndSettingid", trayDto);
                    //根据tray_id和setting_id查询该托盘是否空余
                    BaseDto countDto = (BaseDto) bizService.queryForObject("outSku.queryOrderStoreSku", trayDto);
                    //托盘空余更新 savelocation
                    if (countDto.getAsLong("total") == 0) {
                        BaseDto wTrayDto = new BaseDto();
                        wTrayDto.put("tableName", "wtray");
                        wTrayDto.put("method", "updateSavelocation");
                        wTrayDto.put("id", trayDto.getAsString("tray_id"));
                        wTrayDto.put("savelocation", null);
                        bizService.update(wTrayDto);
                    }
                } else {
                    //更新子订单
                    BaseDto sonDto = new BaseDto();
                    sonDto.put("tableName", "outSku");
                    sonDto.put("method", "updateSkuStatus");
                    sonDto.put("id", dto.getAsString("id"));
                    bizService.update(sonDto);
                    //根据批次号和出库数量更新库存表
                    Dto storeDto = new BaseDto();
                    storeDto.put("num", saleSku.getAsString("out_stock_num"));
                    storeDto.put("in_batch_number", saleSku.getAsString("in_batch_number"));
                    storeDto.put("tableName", "outSku");
                    storeDto.put("method", "updateBuyStatus");
                    bizService.update(storeDto);
                }
            }

            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);

            //更新库存记录
            orderStoreSkuLogService.insertLog(member.getAsLong("id"), saleSku.getAsLong("sku_id"), SkuLogTypeEnum.OUTSTORE.getCode(), saleSku.getAsInteger("out_stock_num"),
                    saleSku.getAsString("sku_name"), saleSku.getAsString("in_batch_number"), saleSku.getAsString("order_no"));


        }
        return returnDto;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteSkuStore(String in_batch_number, String memberid) {
        if (StringUtils.isNotEmpty(in_batch_number)) {

            try {
                Dto query = new BaseDto();
                query.put("in_batch_number", in_batch_number);
                List<Dto> skuList = bizService.queryForList("norderStoreSku.queryList", query);
                if (!skuList.isEmpty()) {

                    Dto skuInfo = skuList.get(0);
                    if (skuInfo.getAsInteger("is_expired_days") < 0) {
                        //过期产品可以删除

                        Dto updateStockInfo = new BaseDto();
                        updateStockInfo.put("in_batch_number", in_batch_number);
                        updateStockInfo.put("updator", memberid);
                        updateStockInfo.put("tableName", "norderStoreSku");
                        updateStockInfo.put("method", "deleteStock");

                        bizService.update(updateStockInfo);

                    }

                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                throw e;
            }


        }
    }

    @Override
    public void allotStock(Dto dto, Long memberId) {
        //待检表id,查询待检表数据
        String buyCheckedId = dto.getAsString("buy_checked_id");
        if (StringUtils.isEmpty(buyCheckedId)) {
            return;
        }


        List<Dto> orderBuyCheckedList = bizService.queryForList("order_buy_checked.queryList", new BaseDto("id", buyCheckedId));
        if (!orderBuyCheckedList.isEmpty()) {
            //待检信息不为空
            Dto orderBuyChecked = orderBuyCheckedList.get(0);

            String buySkuNum = orderBuyChecked.getAsString("buy_sku_num");

            String arrivalNum = orderBuyChecked.getAsString("arrival_num");

            Dto updateDto = new BaseDto();
            if (StringUtils.isEmpty(arrivalNum)) {
                //到货数量
                updateDto.put("arrival_num", buySkuNum);
            }

            Dto worderinstore = null;
            if (StringUtils.isNotEmpty(dto.getAsString("id"))) {
                //id不为空是修改
                //获取原库存数量
                worderinstore = (Dto) bizService.queryForObject("worderinstore.getInfo", new BaseDto("id", dto.getAsString("id")));
            }


            //合格数量
            String qualifiedNum = orderBuyChecked.getAsString("qualified_num");

            //入库数量
            String stock = dto.getAsString("stock");
            if (StringUtils.isEmpty(qualifiedNum)) {
                updateDto.put("qualified_num", stock);
            } else {

                if (worderinstore == null) {
                    updateDto.put("qualified_num", Integer.parseInt(qualifiedNum) + Integer.parseInt(stock));
                } else {
                    //移除原有库存数据
                    updateDto.put("qualified_num", Integer.parseInt(qualifiedNum) - Integer.parseInt(worderinstore.getAsString("stock")) + Integer.parseInt(stock));
                }

            }

            updateDto.put("unqualified_num", Integer.parseInt(buySkuNum) - updateDto.getAsInteger("qualified_num"));

            if (updateDto.getAsInteger("unqualified_num") == 0) {
                updateDto.put("inspecter_result", 0);
            } else {
                updateDto.put("inspecter_result", 2);
            }

            if (StringUtils.isEmpty(orderBuyChecked.getAsString("checked_time"))) {
                //入库时间
                updateDto.put("instock_time", DateUtil.getStringFromDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
                //到货日期
                updateDto.put("arrival_date", DateUtil.getStringFromDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
                updateDto.put("checked_time", DateUtil.getStringFromDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
                updateDto.put("isexpired", 0);

            }


            try {

                dto.put("buy_sku_id", orderBuyChecked.getAsString("buy_sku_id"));
                dto.put("buy_checked_id", orderBuyChecked.getAsString("id"));
                dto.put("sku_id", orderBuyChecked.getAsString("sku_id"));
                dto.put("provider_id", orderBuyChecked.getAsString("customer_id"));
                dto.put("order_buy_sku_number", orderBuyChecked.getAsString("order_buy_sku_number"));

                intoStoreBuySku(dto, memberId);

                if (updateDto.getAsInteger("unqualified_num") == 0) {
                    Dto sdto = new BaseDto();
                    sdto.put("id", dto.getAsString("buy_checked_id"));
                    sdto.put("tableName", "order_buy_checked");
                    //状态  0 待分配 1 已分配
                    sdto.put("status", 1);
                    bizService.updateInfo(sdto);

                    Dto orderBuySku = new BaseDto("id", dto.getAsString("buy_sku_id"));
                    //状态 0 待质检 1 质检中 2 质检通过  3 质检驳回 4 退货
                    orderBuySku.put("status", 2);
                    orderBuySku.put("tableName", "orderBuySku");
                    orderBuySku.put("updator", memberId);
                    bizService.updateInfo(orderBuySku);

                }


            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }

            updateDto.put("id", orderBuyChecked.getAsString("id"));
            updateDto.put("tableName", "order_buy_checked");
            updateDto.put("updator", memberId);
            bizService.updateInfo(updateDto);

        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doOrderCheck(Dto dto, Long memberId, String id) {


        String value = redisService.getValue("DO_ORDER_CHECK" + id);

        if (StringUtils.isNotEmpty(value)) {
            //已经在处理
            return;
        }

        //设置处理标识
        redisService.setValue("DO_ORDER_CHECK" + id, id, 120l);
        try {

            List<Dto> orderBuySkuList = bizService.queryForList("orderBuySku.getList", new BaseDto("order_id", id));
            for (Dto orderBuySku : orderBuySkuList) {


                if (orderBuySku != null && orderBuySku.getAsInteger("order_buy_status") == 0) {

                    dto.put("id", orderBuySku.getAsString("order_id"));

                    if (dto.getAsInteger("status") == 1) {
                        //质检中，插入待检产品信息统计表
                        Dto updateCheckrd = new BaseDto();
                        updateCheckrd.put("sku_id", orderBuySku.getAsString("sku_id"));
                        updateCheckrd.put("sku_name", orderBuySku.getAsString("name"));
                        updateCheckrd.put("sku_number", orderBuySku.getAsString("sku_number"));
                        updateCheckrd.put("sku_level", orderBuySku.getAsString("level"));
                        updateCheckrd.put("roduction_date", orderBuySku.getAsString("roduction_date"));
                        updateCheckrd.put("expiration_date", orderBuySku.getAsString("expiration_date"));
                        updateCheckrd.put("sku_type_id", orderBuySku.getAsString("type_id"));
                        updateCheckrd.put("food_add_type", orderBuySku.getAsString("food_add_type"));
                        updateCheckrd.put("sku_type_text", orderBuySku.getAsString("text"));
                        updateCheckrd.put("sku_type_number", orderBuySku.getAsString("type_number"));

                        if (StringUtils.isNotEmpty(orderBuySku.getAsString("unit_volume"))) {
                            updateCheckrd.put("unit_volume", orderBuySku.getAsString("unit_volume"));
                        }
                        if (StringUtils.isNotEmpty(orderBuySku.getAsString("unit_weight"))) {
                            updateCheckrd.put("unit_weight", orderBuySku.getAsString("unit_weight"));
                        }
                        updateCheckrd.put("customer_id", orderBuySku.getAsString("customer_id"));
                        updateCheckrd.put("bill", orderBuySku.getAsString("bill"));
                        updateCheckrd.put("bill_photo", orderBuySku.getAsString("bill_photo"));
                        updateCheckrd.put("bill_zip", orderBuySku.getAsString("bill_zip"));
                        updateCheckrd.put("order_id", orderBuySku.getAsString("order_id"));
                        updateCheckrd.put("buy_sku_id", orderBuySku.getAsString("id"));
                        //状态  0 待分配 1 已分配
                        updateCheckrd.put("status", 0);

                        updateCheckrd.put("instock_time", null);
                        updateCheckrd.put("arrival_date", null);
                        updateCheckrd.put("checked_warehouse_id", null);
                        updateCheckrd.put("warehouse_site_setting", null);
                        updateCheckrd.put("arrival_num", null);
                        updateCheckrd.put("qualified_num", null);
                        updateCheckrd.put("unqualified_num", null);
                        updateCheckrd.put("inspecter_result", null);
                        updateCheckrd.put("isexpired", null);
                        updateCheckrd.put("remark", null);
                        updateCheckrd.put("creator", memberId);
                        updateCheckrd.put("updator", memberId);
                        updateCheckrd.put("is_delete", "N");
                        updateCheckrd.put("tableName", "order_buy_checked");
                        updateCheckrd.put("checked_time", null);
                        bizService.saveInfo(updateCheckrd);

                    }

                }
            }
            bizService.update(dto);


        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            //删除缓存数据
            redisService.delete("DO_ORDER_CHECK" + id);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doCloseCheck(Dto dto, Long memberId) {
        String orderId = dto.getAsString("id");
        if (StringUtils.isEmpty(orderId)) {
            return;
        }


        try {
            Dto sdto = new BaseDto();
            sdto.put("order_id", orderId);
            sdto.put("tableName", "orderBuySku");
            sdto.put("method", "updateOrderBuyCheckedStatus");
            sdto.put("updator", memberId);
            bizService.update(sdto);

            Dto orderBuySku = new BaseDto("order_id", orderId);
            orderBuySku.put("tableName", "orderBuySku");
            orderBuySku.put("method", "updateOrderBuySkuStatus");
            orderBuySku.put("updator", memberId);
            bizService.update(orderBuySku);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


    }

}
