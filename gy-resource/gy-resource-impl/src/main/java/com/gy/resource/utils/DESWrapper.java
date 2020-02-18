package com.gy.resource.utils;


import org.apache.commons.codec.binary.Base64;

/**
 * @author tanxiongying
 */
public class DESWrapper {

    public static String encrypt(String src, String key) {
        String result;
        try {
            byte[] data = DesUtil.encrypt(src.getBytes("UTF-8"), Base64.decodeBase64(key));
            result = Base64.encodeBase64String(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String decrypt(String src, String key) {
        String result;
        try {
            byte[] data = DesUtil.decrypt(Base64.decodeBase64(src), Base64.decodeBase64(key));
            result = new String(data, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String genKey() {
        String result;
        try {
            byte[] data = DesUtil.initKey();
            result = Base64.encodeBase64String(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
