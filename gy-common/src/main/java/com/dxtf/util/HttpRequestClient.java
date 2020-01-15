package com.dxtf.util;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.g4studio.core.json.JsonHelper;
import org.g4studio.core.metatype.Dto;

import javax.ws.rs.core.MediaType;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 
 * http模拟请求客户端
 * 
 */
public class HttpRequestClient {
	
	
	/**
	 * 
	 * 
	 * 通过post方式上送数据返回结果
	 * 
	 * 
	 * @param map
	 *            存放到request中的参数
	 * @param url
	 *            请求地址
	 * @return String 返回的内容
	 * @throws FileNotFoundException
	 * 
	 * @throws URIException
	 *             URl不存在的时候
	 */
	public static String postRequest(String url, Map paramMap)
			throws Exception {
		PostMethod postMethod = new PostMethod();
		postMethod.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		postMethod.setURI(new URI(url));
		// 判断是否含有参数，如果含有参数，则增加到httpmethod的param中去
		if (paramMap != null && paramMap.size() > 0) {
			Set<String> bkeys = paramMap.keySet();
			NameValuePair[] dataList = new NameValuePair[paramMap.size()];
			int i = 0;
			for (Iterator<String> itr = bkeys.iterator(); itr.hasNext();) {
				String key = (String) itr.next();
				Object value = (Object) paramMap.get(key);
				dataList[i] = new NameValuePair(key,
						value != null ? (String) value : "");
				i++;
				postMethod.addParameter(new NameValuePair(key,
						value != null ? (String) value : ""));
			}
		}
		HttpClient httpClient = new HttpClient();
		try {
			int i = httpClient.executeMethod(postMethod);
			String result = postMethod.getResponseBodyAsString();
			if (i == 200 && "".equals(result)) {
				result = "";
			}
			return result;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String postRequestByUrlEncode(String url, Map paramMap)
			throws Exception {
		PostMethod postMethod = new PostMethod();
		postMethod.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		postMethod.setURI(new URI(url));
		// 判断是否含有参数，如果含有参数，则增加到httpmethod的param中去
		if (paramMap != null && paramMap.size() > 0) {
			Set<String> bkeys = paramMap.keySet();
			NameValuePair[] dataList = new NameValuePair[paramMap.size()];
			int i = 0;
			for (Iterator<String> itr = bkeys.iterator(); itr.hasNext();) {
				String key = (String) itr.next();
				Object value = (Object) paramMap.get(key);
				dataList[i] = new NameValuePair(key,
						value != null ? (String) URLEncoder.encode(value.toString(),"UTF-8") : "");
				i++;
				postMethod.addParameter(new NameValuePair(key,
						value != null ? (String) URLEncoder.encode(value.toString(),"UTF-8") : ""));
			}
		}
		HttpClient httpClient = new HttpClient();
		try {
			int i = httpClient.executeMethod(postMethod);
			String result = postMethod.getResponseBodyAsString();
			if (i == 200 && "".equals(result)) {
				result = "";
			}
			return result;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 发送短信公用方法
	 * @param mobile
	 * @param msg
	 * @return
	 */
    public static Dto sendMsg(String mobile,String msg){
    	Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(
            "api","key-17380124c969d1051822ede3ee8ab06a"));
        WebResource webResource = client.resource(
            "http://sms-api.luosimao.com/v1/send.json");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("mobile", mobile);
        formData.add("message", "尊敬的用户，您的校验码为" + msg + ",工作人员不会索取，请勿泄露。【浙里商城】");
        ClientResponse response =  webResource.type( MediaType.APPLICATION_FORM_URLENCODED ).
        post(ClientResponse.class, formData);
        String textEntity = response.getEntity(String.class);
        int status = response.getStatus();
        System.out.println("--------return status------------" + status);
		Dto retDto = JsonHelper.parseSingleJson2Dto(textEntity);
		return retDto;
	}

}
