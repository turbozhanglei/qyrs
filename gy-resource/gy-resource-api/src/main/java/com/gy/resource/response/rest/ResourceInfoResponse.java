package com.gy.resource.response.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 资源信息
 * @version : v1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceInfoResponse implements Serializable {

    private static final long serialVersionUID = -8062239912163326960L;
    //id
    private long id;
    //发布用户id
    private long userId;
    //手机号
    private String mobileStr;
    //资源标题
    private String title;
    //昵称
    private String nickname;
    //资源id
    private Long resourceId;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //创建用户
    private long creator;
    //更新用户
    private long updator;
    
}