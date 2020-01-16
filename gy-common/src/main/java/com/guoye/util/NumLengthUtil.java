package com.guoye.util;

/**
 * json操作工具类
 * 
 * @author fs
 *
 */
public final class NumLengthUtil {
	private NumLengthUtil() {
	}
	public static String getString(String str, int strLength){
		int strLen = str.length();
		StringBuffer sb = null;
		while (strLen < strLength) {
			sb = new StringBuffer();
			sb.append(str).append("0");//右补0
			str = sb.toString();
			strLen = str.length();
		}
		return str;
	}
}
