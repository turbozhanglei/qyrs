/*
package com.guoye.controller.sys;

import com.guoye.base.BizAction;
import com.guoye.controller.utils.DisplaySalesActivity;
import com.sap.conn.jco.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

*/
/**
 * 后台用户业务处理
 *
 * @author mcl
 * @see RfcController
 * @since 2017年6月20日15:49:06
 *//*

@RestController
@RequestMapping("/rfc")
public class RfcController extends BizAction {
   */
/* public static void main(String[] args) throws JCoException {
        *//*
*/
/*JCoDestination jCoDestination = null;
        JCoFunction function = null;
        jCoDestination = ConnectSAPServer.Connect();
        function = jCoDestination.getRepository().getFunction("ZRFC _DELIVERY_INFO_GET");
        JCoParameterList parameterList = function.getImportParameterList();
        parameterList.setValue("VBELN", "803234847");
        function.execute(jCoDestination);
        JCoParameterList resultList = function.getExportParameterList();
        System.out.println(resultList.getString("BUYER"));
        System.out.println(resultList.getString("RECEIVER"));
        System.out.println(resultList.getString("RECEIVER_TEL"));
        System.out.println(resultList.getString("RECEIVER_ADR"));
        System.out.println(resultList.getString("E_TYPE"));
        System.out.println(resultList.getString("E_MESSAGE"));*//*
*/
/*


       *//*
*/
/* //成功接口 获取信息
        JCoDestination destination = DisplaySalesActivity.getSAPDestination();//获取连接
        //返回一个JCoFunction初始参数的传递函数名。获取接口方法
        JCoFunction function = destination.getRepository().getFunction("ZRFC_DELIVERY_INFO_GET");
        //1.单独的参数，不在表结构下
        function.getImportParameterList().setValue("VBELN", "4500692499");// 参数
        //执行接口
        function.execute(destination);
        JCoParameterList resultList = function.getExportParameterList();
        System.out.println(resultList.getString("BUYER"));
        System.out.println(resultList.getString("RECEIVER"));
        System.out.println(resultList.getString("RECEIVER_TEL"));
        System.out.println(resultList.getString("RECEIVER_ADR"));
        System.out.println(resultList.getString("E_TYPE"));
        System.out.println(resultList.getString("E_MESSAGE"));*//*
*/
/*
        JCoDestination destination = DisplaySalesActivity.getSAPDestination();//获取连接
        //返回一个JCoFunction初始参数的传递函数名。获取接口方法
        JCoFunction function = destination.getRepository().getFunction("ZRFC_DELIVERED_SET");
        //3.JCoTable 主体参数，可为多个主体参数。。。
        JCoTable headerImportParam = function.getTableParameterList().getTable("CT_DELIVERED");//返回的值i个字段作为一个表
        //如果为参数集合，在外层加for循环即可
        headerImportParam.appendRow();//附加表的最后一个新行,行指针,它指向新添加的行。
        headerImportParam.setValue("VBELN", "4500692499");//参数
        headerImportParam.setValue("FN_USER", "");//参数
        headerImportParam.setValue("FN_DATE", "");//参数
        headerImportParam.setValue("FN_TIME", "");//参数
        //执行接口
        function.execute(destination);
        JCoParameterList resultList = function.getExportParameterList();
        System.out.println(resultList.getString("E_TYPE"));
        System.out.println(resultList.getString("E_MESSAGE"));
    }
   *//*
*/
/* public static void main(String[] args) throws Exception {
        Map<String,String> param=new HashMap<>();
        param.put("EBELN","4500692493");
        param.put("MIGO","migo");
        param.put("FLAG_PERSON","");
        Map<String,String> body=new HashMap<>();
        JSONObject jsonObject = JSONObject.fromObject(param);
        body.put("JSON_DATA",jsonObject.toString());
        System.out.println(body);
        Map<String, String> mapHeads = new HashMap<>();
        mapHeads.put("Content-type", "application/json; charset=UTF-8");
        Map<String, String> stringStringMap = HttpClientUtil.doRequestPostJson("http://10.10.5.223:9999/ServiceCenter-VPServer/orders/PoDataCtr/WeChatToVPIsPO", JSONObject.fromObject(body).toString(), mapHeads);
        System.out.println(stringStringMap);
    }*//*

}
*/
