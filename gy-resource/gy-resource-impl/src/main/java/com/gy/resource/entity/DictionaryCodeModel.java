package com.gy.resource.entity;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字典表
 *
 * @author xuyongliang
 * @since 2020-02-14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryCodeModel implements Serializable{

    /**
     * id
     */
    private Long id;
    /**
     * 字典分类，资源标签：resource_label、资源区域：resource_area、贸易类别：resource_trade、发布类型：release_type
     */
    private String category;
    /**
     * 字典值
     */
    private Integer code;
    /**
     * 字典描述
     */
    private String desc;
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
