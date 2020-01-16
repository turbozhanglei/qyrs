package com.guoye.util;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.io.*;
import java.util.*;

/**
 * 
 * 工具类
 * 
 * @author maochunlei
 * @version 1.0 
 * @date 2016年9月19日14:29:44
 * @since 1.0
 */
public class PropertyHolderUtil extends PropertyPlaceholderConfigurer {  
	
	private static final Log logger = LogFactory.getLog(PropertyHolderUtil.class.getName());
	public static List<String> paths = new ArrayList<String>();
    public PropertyHolderUtil() {
    	
        Properties properties = new Properties();
        getFiles("/env/config/guoye");
        try {
        	for(String path:paths){
        		InputStream in = new BufferedInputStream (new FileInputStream(path));
        		BufferedReader reader = new BufferedReader (new InputStreamReader(in,"UTF-8"));
    	        properties.load(reader);
        	}
        } catch (Exception e) {
        	logger.error("----------load properties error----------" + e);
        }
        logger.info("-----------properties value------------" + JSONArray.toJSONString(properties));
        this.setProperties(properties);
    }
    
    private static Map<String, Object> ctxPropertiesMap;  
    
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,Properties props) throws BeansException {  
        super.processProperties(beanFactoryToProcess, props);
        ctxPropertiesMap = new HashMap<String, Object>();  
        for (Object key : props.keySet()) {  
            String keyStr = key.toString();  
            String value = props.getProperty(keyStr);  
            ctxPropertiesMap.put(keyStr, value);  
        }
    }
    
    /*
     * 通过文件名读取配置文件（需按照标准目录放置配置文件）
     * @param key   key
     * @return
     * @throws Exception
     */
    public static String getPropertiesByKey(String key){
        return ctxPropertiesMap.get(key) + "";
    }
    
    
    /**
     * 通过文件名读取配置文件（需按照标准目录放置配置文件）
     * @param key   key
     * @return
     * @throws Exception
     */
    public static Integer getIntValue(String key){
        return ctxPropertiesMap.get(key)==null?0:
        	Integer.parseInt(ctxPropertiesMap.get(key)+"");
    }
   
   public static void getFiles(String path) {
	   File file=new File(path);
	   File[] tempList = file.listFiles();
	   for (int i = 0; i < tempList.length; i++) {
		   if (tempList[i].isFile()) {
    	    paths.add(tempList[i].getPath());
    	   }
    	   if (tempList[i].isDirectory()) {
    	    getFiles(tempList[i].getPath());
    	   }
	   }
   }
   
   public static void main(String[] args) {
	   
	   System.out.println(paths);
   }
   
}
