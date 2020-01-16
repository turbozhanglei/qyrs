package com.guoye.util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import net.sf.json.JsonConfig;
import org.apache.commons.collections.CollectionUtils;
import org.g4studio.core.json.JsonValueProcessorImpl;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;

import java.sql.Timestamp;
import java.util.*;

/**
 * json操作工具类
 * 
 * @author fs
 *
 */
public final class JSONUtil {


	private JSONUtil() {
	}

	/**
	 * 用自定义模板从json字符串中取值
	 * 
	 * @param json
	 * @param template demo:items.items.x_item.0[].open_iid---->0[]：[]表示数组，0表示取第几个
	 * @return
	 * @author yangtao
	 */
	public static String getJSONValueByTemplate(String json, String template) {
		String keys[] = template.split("\\.");
		for (String key : keys) {
			if (json == null) {
				return null;
			}
			if (key.indexOf("[]") > 0) {
				json = JSON.parseObject(json, new TypeReference<List<String>>(){}).get(Integer.parseInt(key.replace("[]", "")));
			} else{
				json = JSON.parseObject(json).getString(key);
			}
		}
		return json;
	}
	
	/**
	 * JSON转Map
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> parseJSON2Map(String jsonStr){ 
        Map<String, Object> map = new HashMap<String, Object>();  
        //最外层解析  
        JSONObject json = JSONObject.parseObject(jsonStr);
        for(Object k : json.keySet()){ 
            Object v = json.get(k);   
            //如果内层还是数组的话，继续解析  
            if(v instanceof JSONArray){
            	//数组对象又分为两种（简单数组或键值对）
                List list = parseJSON2List(json.get(k).toString());
				map.put((String)k, list);  
            } else {  
                map.put((String)k, v);  
            }  
        }  
        return map;  
    }
	
	/**
	 * JSON转Map
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Dto parseJSON2Dto(String jsonStr){
		Dto map = new BaseDto();
        //最外层解析  
        JSONObject json = JSONObject.parseObject(jsonStr);
        for(Object k : json.keySet()){ 
            Object v = json.get(k);   
            //如果内层还是数组的话，继续解析  
            if(v instanceof JSONArray){
            	//数组对象又分为两种（简单数组或键值对）
                List list = parseJSON2List(json.get(k).toString());
				map.put((String)k, list);  
            } else {  
                map.put((String)k, v);  
            }  
        }  
        return map;  
    }
	public static void main(String[] args) {
		String jsonStr = "{\"loginChannel\":\"pc\",\"token\":\"123\",\"mid\":\"123\"}";
		System.out.println(JSONUtil.parseJSON2Dto(jsonStr));
	}
	
	/**
	 * JSON转List<Map<String, Object>>
	 * @param json
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Map<String, Object>> parseJSON2List(String json){
    	JSONArray jsonArray = JSONArray.parseArray(json);
        List list = new ArrayList();
        for (Object object : jsonArray) {
        	try {
        		JSONObject jsonObject = (JSONObject) object;
                HashMap<String, Object> map = new HashMap<String, Object>();
                for (Map.Entry entry : jsonObject.entrySet()) {
                    if(entry.getValue() instanceof JSONArray){
                        map.put((String)entry.getKey(), parseJSON2List(entry.getValue().toString()));
                    }else{
                        map.put((String)entry.getKey(), entry.getValue());
                    }
                }
                list.add(map);
			} catch (Exception e) {
				list.add(object);
			}
        }
        return list;
    }

	public static final List formatDateList(List dtos, String pFormatString) {
		String jsonString = "[]";
		if(CollectionUtils.isNotEmpty(dtos)) {
			JsonConfig cfg = new JsonConfig();
			cfg.registerJsonValueProcessor(Timestamp.class, new JsonValueProcessorImpl(pFormatString));
			cfg.registerJsonValueProcessor(Date.class, new JsonValueProcessorImpl(pFormatString));
			cfg.registerJsonValueProcessor(java.sql.Date.class, new JsonValueProcessorImpl(pFormatString));
			net.sf.json.JSONArray jsonObject = net.sf.json.JSONArray.fromObject(dtos, cfg);
			jsonString = jsonObject.toString();
			net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(jsonString);

			List<?> tlist = JSON.parseArray(jsonString, BaseDto.class);
			return tlist;
		}else {
			return new ArrayList();
		}
	}

	public static final Dto formatDateObject(Object dto, String pFormatString) {
		String jsonString = "[]";
		if(dto != null) {
			JsonConfig cfg = new JsonConfig();
			cfg.registerJsonValueProcessor(Timestamp.class, new JsonValueProcessorImpl(pFormatString));
			cfg.registerJsonValueProcessor(Date.class, new JsonValueProcessorImpl(pFormatString));
			cfg.registerJsonValueProcessor(java.sql.Date.class, new JsonValueProcessorImpl(pFormatString));
			net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(dto, cfg);
			jsonString = jsonObject.toString();
			Dto retDto = JSON.parseObject(jsonString, BaseDto.class);
			return retDto;
		}else {
			return null;
		}
	}
	public static Object[] toArrays(JSONArray ja){
		Object[] objs = new Object[ja.size()];
		for (int i = 0; i < ja.size(); i++) {
			objs[i] = ja.get(i);
		}
		return objs;
	}

	/**
	 * JSON转List<Map<String, Object>>
	 * @param json
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Dto> parseJSON2ListDto(String json){
		JSONArray jsonArray = JSONArray.parseArray(json);
		List list = new ArrayList();
		for (Object object : jsonArray) {
			try {
				JSONObject jsonObject = (JSONObject) object;
				Dto map = new BaseDto();
				for (Map.Entry entry : jsonObject.entrySet()) {
					if(entry.getValue() instanceof JSONArray){
						map.put((String)entry.getKey(), parseJSON2List(entry.getValue().toString()));
					}else{
						map.put((String)entry.getKey(), entry.getValue());
					}
				}
				list.add(map);
			} catch (Exception e) {
				list.add(object);
			}
		}
		return list;
	}
}
