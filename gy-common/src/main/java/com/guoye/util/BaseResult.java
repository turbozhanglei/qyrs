package com.guoye.util;

public class BaseResult {

    /**
     * 接口返回时的code , 默认是成功0000
     */
    private String code = "0000";

    /**
     * 接口返回msg描述  一般接口异常时此字段会有对应描述  成功时多数都默认空
     */
    private String msg = "";

    /**
     * 接口返回data  任意数据类型
     */
    private Object data;

    public BaseResult(){

    }

    public BaseResult(String code){
        this.code = code;
    }

    public BaseResult(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public BaseResult(String code,String msg,Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
