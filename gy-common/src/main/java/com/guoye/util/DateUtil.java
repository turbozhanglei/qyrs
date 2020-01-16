package com.guoye.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public  class DateUtil {
	
	public static String getStringFromDate(Date date,String pattern){
		return new SimpleDateFormat(pattern).format(date);
	}
	
	public static String getSfmFromDate(Date date){
		
		return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
	}
	
	public static String getTodayDate(){
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}
	
	public static Date getDate(String date) throws ParseException{
		return new SimpleDateFormat("yyyy-MM-dd").parse(date);
	}
	
	public static Date getDateFromMin(String date) throws ParseException{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
	}
	
	public static String getDateStr(Date date)
	{
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	/** 
     * 两个时间之间相差距离多少天 
     * @param one 时间参数 1： 
     * @param two 时间参数 2： 
     * @return 相差天数 
     */  
    public static Integer getDistanceDays(Date str1, Date str2){  
        long days=0;  
        long time1 = str1.getTime();  
        long time2 = str2.getTime();  
        long diff ;  
        if(time1<time2) {
            diff = time2 - time1;  
        } else {  
            diff = time1 - time2;  
        }  
        days = diff / (1000 * 60 * 60 * 24);  
        return Integer.parseInt(days + "");
    }  
      
    /** 
     * 两个时间相差距离多少天多少小时多少分多少秒 
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00 
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00 
     * @return long[] 返回值为：{天, 时, 分, 秒} 
     */  
    public static long[] getDistanceTimes(String str1, String str2) {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date one;  
        Date two;  
        long day = 0;  
        long hour = 0;  
        long min = 0;  
        long sec = 0;  
        try {  
            one = df.parse(str1);  
            two = df.parse(str2);  
            long time1 = one.getTime();  
            long time2 = two.getTime();  
            long diff ;  
            if(time1<time2) {  
                diff = time2 - time1;  
            } else {  
                diff = time1 - time2;  
            }  
            day = diff / (24 * 60 * 60 * 1000);  
            hour = (diff / (60 * 60 * 1000) - day * 24);  
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
            sec = (diff/1000-day*24*60*60-hour*60*60-min*60);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        long[] times = {day, hour, min, sec};  
        return times;  
    }  
    /** 
     * 两个时间相差距离多少天多少小时多少分多少秒 
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00 
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00 
     * @return String 返回值为：xx天xx小时xx分xx秒 
     */  
    public static String getDistanceTime(Date str1, Date str2) {  
        long day = 0;  
        long hour = 0;  
        long min = 0;  
        long sec = 0;
        long time1 = str1.getTime();  
        long time2 = str2.getTime();  
        long diff ;  
        if(time1<time2) {  
            diff = time2 - time1;  
        } else {  
            diff = time1 - time2;  
        }  
        day = diff / (24 * 60 * 60 * 1000);  
        hour = (diff / (60 * 60 * 1000) - day * 24);  
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
        sec = (diff/1000-day*24*60*60-hour*60*60-min*60);  
        return day + "天" + hour + "小时" + min + "分" + sec + "秒";  
    }
    
    /**
     * 计算相差天数
     * 		(前一天,前一周,前三十天及当前日期)
     * @param i(当前时间的前i天)
     * @return list{前i天,当前时间}
     */
    public static Map<String,String> getTime(int i) {
		Date dNow = new Date(); // 当前时间
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(dNow);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -i); // 设置为当前时间的前i天
		dBefore = calendar.getTime(); // 得到前i天的时间

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置时间格式
		String startDate = sdf.format(dBefore); // 格式化前i天
		String endDate = sdf.format(dNow); // 格式化当前时间
		Map<String,String> map = new HashMap<String,String>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;
	}
    
    /** 
    * 获得指定日期的前N天 
    * @param specifiedDay 
    * @return 
    *  today 前N天
     * @throws ParseException 
    * @throws Exception 
    */ 
    public static String getSpecifiedDayBefore(int today) {
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        Date date=new Date();  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -today);  
        date = calendar.getTime();  
		return sdf.format(date);
    }
    
    /** 
     * 获取给定日期后N天日期
     * @param specifiedDay 
     * @return 
      * @throws ParseException 
     * @throws Exception 
     */ 
     public static String getDateAddDays(Date today,Integer days) {
 	    Calendar c = Calendar.getInstance(); 
 	    c.setTime(today);
 	    c.add(Calendar.DATE,days);
 	    return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()); 
     }
     
     /** 
      * 获取给定日期后N天日期
      * @param specifiedDay 
      * @return 
       * @throws ParseException 
      * @throws Exception 
      */ 
      public static String getDateSubDays(Date today,Integer days) {
  	    Calendar c = Calendar.getInstance(); 
  	    c.setTime(today);
  	    c.add(Calendar.DATE,-days);
  	    return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()); 
      }
     
     public static int getDayofweek(Date date){  
    	 Calendar cal = Calendar.getInstance();  
		 cal.setTime(date);
		 return cal.get(Calendar.DAY_OF_WEEK);  
	 }
     
     public static int getDayofMonth(Date date){  
    	 Calendar cal = Calendar.getInstance();  
		 cal.setTime(date);
		 return cal.get(Calendar.DAY_OF_MONTH);  
	 }
     
     public static int getDayofYear(Date date){  
    	 Calendar cal = Calendar.getInstance();  
		 cal.setTime(date);
		 return cal.get(Calendar.DAY_OF_YEAR);  
	 }
     
     /**
      * 获取当月第一天
      * @param date
      * @return
      */
     public static String getFirstMonthDay(Date date) {  
         Calendar cal = Calendar.getInstance();  
         cal.set(Calendar.DAY_OF_MONTH, 1);
         return getDateStr(cal.getTime());  
     }

    /**
      * 获取当年第一天
      * @param date
      * @return
      */
     public static String getFirstYearDay(Date date) {  
         Calendar cal = Calendar.getInstance();  
         cal.set(Calendar.DAY_OF_YEAR, 1);
         return getDateStr(cal.getTime());  
     }
     /**
      * 获取前一年第一天
      * @param date
      * @return
      */
     public static String getFirstYearBeforeDay(Date date) {  
         Calendar cal = Calendar.getInstance();  
         cal.set(Calendar.DAY_OF_YEAR, 1);
         cal.add(1,-1);
         return getDateStr(cal.getTime());  
     }

    public static String getFirstWeekDay(Date date) {
         Calendar cal = Calendar.getInstance();  
         cal.setTime(date);  
         // 获得当前日期是一个星期的第几天  
         int dayWeek = cal.get(Calendar.DAY_OF_WEEK);  
         if (1 == dayWeek) {  
             cal.add(Calendar.DAY_OF_MONTH, -1);  
         }  
         // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
         cal.setFirstDayOfWeek(Calendar.MONDAY);  
         // 获得当前日期是一个星期的第几天  
         int day = cal.get(Calendar.DAY_OF_WEEK);  
         // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
         cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);  
         return getDateStr(cal.getTime());  
     }  
     
}
