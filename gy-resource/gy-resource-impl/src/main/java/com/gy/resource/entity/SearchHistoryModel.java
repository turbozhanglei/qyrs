package com.gy.resource.entity;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 搜索历史
 *
 * @author xuyongliang
 * @since 2020-02-14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchHistoryModel implements Serializable{

    /**
     * id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 搜索词
     */
    private String word;
    /**
     * 有效时间
     */
    private Date validTime;
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
