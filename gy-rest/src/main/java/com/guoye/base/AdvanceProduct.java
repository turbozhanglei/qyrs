package com.guoye.base;

import java.util.List;

public class AdvanceProduct {

    private String id;

    private String time;

    private String time_status;

    private String pre_id;

    private List<AdvanceProductList> productList;

    private String q_starttime;

    private String q_endtime;

    private String ids;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getQ_starttime() {
        return q_starttime;
    }

    public void setQ_starttime(String q_starttime) {
        this.q_starttime = q_starttime;
    }

    public String getQ_endtime() {
        return q_endtime;
    }

    public void setQ_endtime(String q_endtime) {
        this.q_endtime = q_endtime;
    }

    public String getPre_id() {
        return pre_id;
    }

    public void setPre_id(String pre_id) {
        this.pre_id = pre_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime_status() {
        return time_status;
    }

    public void setTime_status(String time_status) {
        this.time_status = time_status;
    }

    public List<AdvanceProductList> getProductList() {
        return productList;
    }

    public void setProductList(List<AdvanceProductList> productList) {
        this.productList = productList;
    }
}
