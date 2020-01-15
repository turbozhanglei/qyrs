package com.dxtf.util;

import com.dxtf.service.BizService;
import org.apache.commons.lang.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class OrderNumberUtils {

    @Autowired
    BizService bizService;

    /**
     * 入库key
     */
    private final static String DXINSTOCK = "DXINSTOCK";
    /**
     * 入库redis key
     */
    private final static String DXINSTOCK_REDIS = "DXINSTOCK_ORDER_BUY";

    /**
     * 出库key
     */
    private final static String DXOUTSTOCK = "DXOUTSTOCK";
    /**
     * 出库redis key
     */
    private final static String DXOUTSTOCK_REDIS = "DXOUTSTOCK_ORDER_SALE";

    @Autowired
    public RedisService redisService;

    /**
     * 客户注册key
     */
    private final static String DX_CUSTOMER_NUM = "DX_CUSTOMER_NUM";
    /**
     * 客户注册保存key
     */
    private final static String DX_CUSTOMER_NUM_REDIS = "DX_CUSTOMER_NUM_REDIS";

    /**
     * Order入库订单号生成
     *
     * @return Dto
     */
    public synchronized String getIntoOrderNo() throws Exception {
        String no = redisService.getValue(DXINSTOCK_REDIS);
        if (StringUtils.isNotEmpty(no)) {
            //获取当天时间差
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat parseTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long seconds = parseTime.parse(df.format(new Date()) + " 23:59:59").getTime() - new Date().getTime();
            if (seconds <= 0) {
                seconds = 1;
            } else {
                seconds = seconds / 1000;
            }
            redisService.setValue(DXINSTOCK_REDIS, String.valueOf(Integer.parseInt(no) + 1), seconds);
            return DXINSTOCK + DateUtil.getTodayDate() + intToString((Integer.parseInt(no) + 1), 4);
        } else {
            //获取当天时间差
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat parseTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long seconds = parseTime.parse(df.format(new Date()) + " 23:59:59").getTime() - new Date().getTime();
            if (seconds <= 0) {
                seconds = 1;
            } else {
                seconds = seconds / 1000;
            }
            redisService.setValue(DXINSTOCK_REDIS, "1", seconds);
            return DXINSTOCK + DateUtil.getTodayDate() + "0001";
        }
    }

    /**
     * Order出库订单号生成
     *
     * @return Dto
     */
    public synchronized String getOutOrderNo() throws Exception {
        String no = redisService.getValue(DXOUTSTOCK_REDIS);
        if (StringUtils.isNotEmpty(no)) {
            //获取当天时间差
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat parseTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long seconds = parseTime.parse(df.format(new Date()) + " 23:59:59").getTime() - new Date().getTime();
            if (seconds <= 0) {
                seconds = 1;
            } else {
                seconds = seconds / 1000;
            }
            redisService.setValue(DXOUTSTOCK_REDIS, String.valueOf(Integer.parseInt(no) + 1), seconds);
            return DXOUTSTOCK + DateUtil.getTodayDate() + intToString((Integer.parseInt(no) + 1), 4);
        } else {
            //获取当天时间差
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat parseTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long seconds = parseTime.parse(df.format(new Date()) + " 23:59:59").getTime() - new Date().getTime();
            if (seconds <= 0) {
                seconds = 1;
            } else {
                seconds = seconds / 1000;
            }
            redisService.setValue(DXOUTSTOCK_REDIS, "1", seconds);
            return DXOUTSTOCK + DateUtil.getTodayDate() + "0001";
        }
    }


    /**
     * 客户编号生成
     *
     * @return Dto
     */
    public synchronized String getCuntomerNo() {
        Long no = redisService.increment(DX_CUSTOMER_NUM_REDIS, 1);
        if (no == 1) {
            Dto dto = (Dto) bizService.queryForDto("wcustomer.queryListCount", new BaseDto());
            Integer total = dto.getAsInteger("total");
            if (total > 0) {
                no = total.longValue() + 1;
                redisService.setValue(DX_CUSTOMER_NUM_REDIS, String.valueOf(no));
            }
        }
        return "KF" + intToString(no.intValue(), 6);
    }


    /**
     * 客户编号减一
     *
     * @return Dto
     */
    public synchronized void subCuntomerNo() throws Exception {
        String no = redisService.getValue(DX_CUSTOMER_NUM);
        if (StringUtils.isNotEmpty(no)) {
            redisService.increment(DX_CUSTOMER_NUM_REDIS, -1);
        }
    }

    public static String intToString(int num, int length) {
        String params = String.valueOf(num);
        int leng = length - params.length();
        String result = "";
        if (leng > 0) {
            for (int i = 0; i < leng; i++) {
                result = result + "0";
            }
            result += num;
        } else {
            result = params;
        }
        return result;
    }
}
