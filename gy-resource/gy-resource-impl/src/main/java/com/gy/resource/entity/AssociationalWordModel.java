package com.gy.resource.entity;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 资源标签联想词
 *
 * @author xuyongliang
 * @since 2020-02-14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssociationalWordModel implements Serializable{

    /**
     * id
     */
    private Long id;
    /**
     * 联想词，根据code区分
     */
    private String word;
    /**
     * 字典值
     */
    private Integer code;
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
