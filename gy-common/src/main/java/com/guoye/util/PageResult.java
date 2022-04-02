package com.guoye.util;

public class PageResult {
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
    private Object rows;

    public PageResult(){

    }

    public PageResult(String code,String msg){
        this.code = code;
        this.msg = msg;
    }
    public PageResult(String code,String msg,Object rows,int total){
        this.code = code;
        this.msg = msg;
        this.rows = rows;
        this.total = total;
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

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * total  总条数
     */
    private int total;
}
