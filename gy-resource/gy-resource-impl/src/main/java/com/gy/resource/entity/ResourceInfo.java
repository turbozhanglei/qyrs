package com.gy.resource.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 资源信息
 * @author : gaoly
 * @email : 774329481@qq.com
 * @since : 2020-02-14 11:08:42
 * @version : v1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceInfo implements Serializable {

    private static final long serialVersionUID = 5699318323159663984L;
    //id
    private Long id;
//发布用户id
    private Long userId;
    //手机号，加密后
    private String mobile;
    //资源标题
    private String title;
    //平台 1：小程序
    private Integer platform;
    //发布类型，见字典表release_type
    private Integer releaseType;
    //资源标签，见字典表resource_label
    private Integer resourceLabel;
    //资源区域，见字典表resource_area
    private Integer resourceArea;
    //内贸外贸，见字典表resource_trade
    private Integer resourceTrade;
    //发布状态，0、待审核，1、系统审核通过，2、待人工审核，3、人工审核通过，4、人工审核不通过
    private Integer status;
    //是否置顶，0：未置顶、1：已置顶
    private Integer sticky;
    //是否包含敏感词 0：不包含、1：包含
    private Integer sensitiveFlag;
    //是否有图 0：否、1：是
    private Integer imageFlag;
    //资源内容
    private String content;
    //审核账号名
    private String auditor;
    //审核时间
    private Date auditTime;
    //是否删除：0不删除， 1删除 
    private Integer deleteFlag;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //创建用户
    private Long creator;
    //更新用户
    private Long updator;

    private Date createStartTime;
    private Date createEndTime;

    
}