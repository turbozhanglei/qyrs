package com.dxtf.util;

import com.alibaba.fastjson.JSONArray;
import com.dxtf.base.BaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.model.SpringBeanLoader;
import org.g4studio.core.resource.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author admin
 * @date 2017年7月16日23:40:24
 */
@Component
@Slf4j
public class CommonUtil {
    private static Log log = LogFactory.getLog(CommonUtil.class);
    protected static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    public static RedisService redisService;

    public static BaseMapper reader;

    @Autowired
    public CommonUtil(RedisService redisService, BaseMapper reader) {
        CommonUtil.redisService = redisService;
        CommonUtil.reader = reader;
    }

    /**
     * 返回系统参数统一方法
     *
     * @return Dto
     */
    public static Dto getSysConfig() {
        Dto configDto = redisService.getObject("THMJ_" + StatusConstant.CONFIG_SYSTEM, BaseDto.class);
        if (configDto != null && configDto.size() > 2) {
            return configDto;
        } else {
            Dto config = new BaseDto();
            List<Dto> paramList = reader.queryForList("sysConfig.queryConfigList");
            for (Dto dto : paramList) {
                config.put(dto.get("key"), dto.get("val"));
            }
            redisService.setValue("THMJ_" + StatusConstant.CONFIG_SYSTEM, JSONArray.toJSONString(config), 180l);
            return config;
        }
    }

    /**
     * 去除小数点后的0
     */
    public static String rvZeroAndDot(String s) {
        if (s.isEmpty()) {
            return null;
        }
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 返回当前时间与月底相差的毫秒
     */

    public static Long millisecondDifference() {
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        Date lastDay = cale.getTime();
        int i = differentDays(new Date(), lastDay);
        if (i == 0) {
            i = i + 1;
        }
//        Long  l = Long.valueOf(i);
        BigDecimal multiply = new BigDecimal(i).multiply(new BigDecimal(86400));
        return Long.valueOf(String.valueOf(multiply));
//        Long y = l*86400000L;
//        return y;
    }

    /**
     * 获取当前时间的时间戳
     */
    public static String getNowTimeStamp() {
        long time = System.currentTimeMillis();
        return String.valueOf(time);
    }

    /**
     * Order订单号生成
     *
     * @return Dto
     */
    public static String getOrderNo() {
        /*if(redisService == null){
            redisService = (RedisService)SpringBeanLoader.getSpringBean("redisService");
		}
		String no = redisService.getValue("ORDER_NO_REDIS_KEY_ZL");
		if(StringUtils.isNotEmpty(no)){
			return "O" + sdf.format(new Date()) + "00000" + (Integer.parseInt(no) + 1);
		}else{
			redisService.setValue("ORDER_NO_REDIS_KEY_Zl","1",millisecondDifference());
			return "O" + sdf.format(new Date()) + "000001";
		}*/
        return "O" + sdf.format(new Date()) + (int) ((Math.random() * 9 + 1) * 10000000);
    }

    /**
     * Order订单号生成
     *
     * @return Dto
     */
    public static String getOrderSubNo() {
//		if(redisService == null){
//			redisService = (RedisService)SpringBeanLoader.getSpringBean("redisService");
//		}
//		String no = redisService.getValue("ORDER_SUB_NO_REDIS_KEY_ZL");
//		if(StringUtils.isNotEmpty(no)){
//			return "OS" + sdf.format(new Date()) + "00000" + (Integer.parseInt(no) + 1);
//		}else{
//			redisService.setValue("ORDER_SUB_NO_REDIS_KEY_ZL","1",millisecondDifference());
//			return "OS" + sdf.format(new Date()) + "000001";
//		}
        return "OS" + sdf.format(new Date()) + (int) ((Math.random() * 9 + 1) * 10000000);
    }

    /**
     * date2比date1多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2)   //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    //闰年
                    timeDistance += 366;
                } else {
                    //不是闰年
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else    //不同年
        {
            System.out.println("判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        }
    }

    public static String getCarrierNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(new Date());
        return "CY" + date + (int) ((Math.random() * 9.0D + 1.0D) * 10000.0D) + "0001";
    }

    public static Dto ThousandBit(Dto info) {
        String format;
        String string;
        for (Iterator var5 = (new ArrayList(info.keySet())).iterator(); var5.hasNext(); info.put(string, format)) {
            string = (String) var5.next();
            String values = info.getAsString(string);
            Pattern pattern = Pattern.compile("^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$");
            Matcher isNum = pattern.matcher(values);
            if (isNum.matches()) {
                DecimalFormat df = new DecimalFormat("##,##0.00");
                format = "" != values ? df.format((double) Float.parseFloat(values)) : "";
            } else {
                format = values;
            }
        }

        return info;
    }

    // map生成xml
    public static String mapToxml(Map<String, String> map) {
        String xml = "<xml>";
        for (String item : map.keySet()) {
            xml += "<" + item + ">";
            xml += map.get(item);
            xml += "</" + item + ">";
        }
        xml += "</xml>";
        return xml;
    }

    /**
     * 统一下单设置签名的方式
     */
    public static Map<String, Object> setSign(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        // 参数名ASCII码从小到大排序（字典序）；
        if (null != map.get("appid")) {
            sb.append("appid=");
            sb.append(map.get("appid"));
        }
        if (null != map.get("attach")) {
            sb.append("&attach=");
            sb.append(map.get("attach"));
        }
        if (null != map.get("body")) {
            sb.append("&body=");
            sb.append(map.get("body"));
        }
        if (null != map.get("detail")) {
            sb.append("&detail=");
            sb.append(map.get("detail"));
        }
        if (null != map.get("device_info")) {
            sb.append("&device_info=");
            sb.append(map.get("device_info"));
        }
        if (null != map.get("mch_id")) {
            sb.append("&mch_id=");
            sb.append(map.get("mch_id"));
        }
        if (null != map.get("nonce_str")) {
            sb.append("&nonce_str=");
            sb.append(map.get("nonce_str"));
        }
        if (null != map.get("notify_url")) {
            sb.append("&notify_url=");
            sb.append(map.get("notify_url"));
        }
        if (null != map.get("openid")) {
            sb.append("&openid=");
            sb.append(map.get("openid"));
        }
        if (null != map.get("out_trade_no")) {
            sb.append("&out_trade_no=");
            sb.append(map.get("out_trade_no"));
        }
        if (null != map.get("spbill_create_ip")) {
            sb.append("&spbill_create_ip=");
            sb.append(map.get("spbill_create_ip"));
        }
        if (null != map.get("total_fee")) {
            sb.append("&total_fee=");
            sb.append(map.get("total_fee"));
        }
        if (null != map.get("trade_type")) {
            sb.append("&trade_type=");
            sb.append(map.get("trade_type"));
        }
        if (null != map.get("sub_mch_id")) {
            sb.append("&sub_mch_id=");
            sb.append(map.get("sub_mch_id"));
        }
        if (null != map.get("sub_openid")) {
            sb.append("&sub_openid=");
            sb.append(map.get("sub_openid"));
        }
        System.out.println("begin......" + sb.toString());
        String sign = Md5Util.parseStrToMd5U32(sb.toString()).toUpperCase();
        map.put("sign", sign);
        System.out.println("end........" + sign);
        return map;
    }

    /**
     * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<
     *
     * @param paramsMap  要排序的Map对象
     * @param urlEncode  是否需要URLENCODE
     * @param keyToLower 是否需要将Key转换为全小写 true:key转化成小写，false:不转化
     * @return
     */
    public static Map<String, String> formatUrlMap(Map<String, String> paramsMap, boolean urlEncode, boolean keyToLower) {

        String buff = "";
        Map<String, String> tmpMap = paramsMap;

        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());

            //对所有传入参数按照字段名的ASCII码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });

            //构造URL 键值对的格式
            StringBuffer buf = new StringBuffer();
            for (Map.Entry<String, String> item : infoIds) {
                if (StringUtils.isNotEmpty(item.getKey())) {
                    String key = item.getKey();
                    String value = item.getValue();
                    buf.append(key + "=" + value);
                    buf.append("&");
                }
            }
            buff = buf.toString() + "key=addsflijtssaerexwersdscfsewsdssx";
            System.out.println(buff);
            String sign = Md5Util.parseStrToMd5U32(buff.toString()).toUpperCase();
            tmpMap.put("sign", sign);
            System.out.println("end........" + sign);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return tmpMap;
    }

    public static Map<String, String> formatUrlMap2(Map<String, String> paramsMap, boolean urlEncode, boolean keyToLower) {

        String buff = "";
        Map<String, String> tmpMap = paramsMap;

        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());

            //对所有传入参数按照字段名的ASCII码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });

            //构造URL 键值对的格式
            StringBuffer buf = new StringBuffer();
            for (Map.Entry<String, String> item : infoIds) {
                if (StringUtils.isNotEmpty(item.getKey())) {
                    String key = item.getKey();
                    String value = item.getValue();
                    buf.append(key + "=" + value);
                    buf.append("&");
                }
            }
            buff = buf.toString() + "key=addsflijtssaerexwersdscfsewsdssx";
            System.out.println(buff);
            String sign = Md5Util.parseStrToMd5U32(buff.toString()).toUpperCase();
            tmpMap.put("paySign", sign);
            System.out.println("end........" + sign);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return tmpMap;
    }


    /**
     * 统一下单设置签名的方式
     */
    public static Map<String, Object> OpenSign(Map<String, Object> reqBean) {
        StringBuilder sb = new StringBuilder();
        // 参数名ASCII码从小到大排序（字典序）；
        if (null != reqBean.get("appid")) {
            sb.append("appid=");
            sb.append(reqBean.get("appid"));
        }
        if (null != reqBean.get("timeStamp")) {
            sb.append("timeStamp=");
            sb.append(reqBean.get("timeStamp"));
        }
        if (null != reqBean.get("nonceStr")) {
            sb.append("nonceStr=");
            sb.append(reqBean.get("nonceStr"));
        }
        if (null != reqBean.get("package")) {
            sb.append("package=");
            sb.append(reqBean.get("package"));
        }
        if (null != reqBean.get("signType")) {
            sb.append("signType=");
            sb.append(reqBean.get("signType"));
        }
        System.out.println("begin......" + sb.toString());
        String sign = Md5Util.parseStrToMd5U32(sb.toString()).toUpperCase();
        reqBean.put("paySign", sign);
        System.out.println("end........" + sign);
        return reqBean;
    }

    public static Map<String, Object> xmlFormatStringToMap(String str) {
        Map<String, Object> result = null;
        if (StringUtils.isNotEmpty(str)) {
            try {
                Element root = DocumentHelper.parseText(str).getRootElement();
                // 获取所有子节点
                List<Element> elements = root.elements();
                for (Element element : elements) {
                    // 第一次进入初始化
                    if (result == null) {
                        result = new HashMap<>();
                    }
                    result.put(element.getName(), element.getText());
                }
                return result;
            } catch (Exception e) {
                log.error("Xml转换异常 >>> 被转换字符串 >>> " + str);
            }
        } else {
            return new HashMap<>();
        }
        return result;
    }


    /**
     * Order取餐码生成
     *
     * @return Dto
     */
    public static synchronized String getDiningCodeNo() {
        if (redisService == null) {
            redisService = (RedisService) SpringBeanLoader.getSpringBean("redisService");
        }
        String key = DateUtil.getStringFromDate(new Date(), "yyyyMMdd");
        String no = redisService.getValue(key + "_din_code");
        if (StringUtils.isNotEmpty(no)) {
            redisService.setValue(key + "_din_code", String.valueOf(Integer.parseInt(no) + 1));
            String code = String.format("%0" + 5 + "d", (Integer.parseInt(no) + 1));
            return "Z" + code;
        } else {
            redisService.setValue(key + "_din_code", "1");
            return "Z" + "00001";
        }
    }

    /**
     * 两个时间之间相差距离多少天
     *
     * @param str1 时间参数 1：
     * @param str2 时间参数 2：
     * @return 相差天数
     */
    public static long getDistanceDays(String str1, String str2) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date one;
        Date two;
        long days = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }


    /**
     * 加密微信手机号
     *
     * @param sSrc
     * @param encodingFormat
     * @param sKey
     * @param ivParameter
     * @return
     * @throws Exception
     */
    public static String decryptS5(String sSrc, String encodingFormat, String sKey, String ivParameter) throws Exception {
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] raw = decoder.decodeBuffer(sKey);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec(decoder.decodeBuffer(ivParameter));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] myendicod = decoder.decodeBuffer(sSrc);
            byte[] original = cipher.doFinal(myendicod);
            String originalString = new String(original, encodingFormat);
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Order退款单号生成
     *
     * @return Dto
     */
    public static String getReturnOrderNo() {
        if (redisService == null) {
            redisService = (RedisService) SpringBeanLoader.getSpringBean("redisService");
        }
        String no = redisService.getValue("RETURN_ORDER_NO_REDIS_KEY_ZL");
        if (StringUtils.isNotEmpty(no)) {
            return "T" + sdf.format(new Date()) + "00000" + (Integer.parseInt(no) + 1);
        } else {
            redisService.setValue("RETURN_ORDER_NO_REDIS_KEY_ZL", "1", millisecondDifference());
            return "T" + sdf.format(new Date()) + "000001";
        }
    }

    /**
     * 根据手机号获取归属地
     *
     * @return
     */
    public static String getPhoneAddress(String phone) {
        String url = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=" + phone;
        Map<String, String> header = new HashMap();
        header.put("charset", "utf-8");
        String mobileAddress = "";
        try {
            String result = HttpClientUtil.httpGet(url);
            if (StringUtils.isNotEmpty(result)) {
                System.out.println(result.substring(result.indexOf("{"), result.indexOf("}") + 1));
                Dto dto = JSONUtil.parseJSON2Dto(result.substring(result.indexOf("{"), result.indexOf("}") + 1));
                mobileAddress = dto.getAsString("province");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mobileAddress;
    }
}
