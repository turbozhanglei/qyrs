package com.gy.resource.entity;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 全局关联信息表
 *
 * @author xuyongliang
 * @since 2020-02-14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlobalCorrelationModel implements Serializable{

    /**
     * id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 关联id与ref_type一起使用，资源id、文章id、用户id
     */
    private Long refId;
    /**
     * 关联类型，0、关注用户，1、资源浏览数，2、资源分享数，3、资源拨打电话数，4、资讯文章分享数，5、资讯文章点赞数，6、资讯文章浏览数
     */
    private Integer refType;
    /**
     * 是否删除：0不删除， 1删除 
     */
    private Integer deleteFlag;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建用户
     */
    private Long creator;
    /**
     * 更新用户
     */
    private Long updator;



}
