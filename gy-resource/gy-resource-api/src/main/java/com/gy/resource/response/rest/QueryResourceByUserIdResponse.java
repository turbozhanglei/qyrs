package com.gy.resource.response.rest;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: gaolanyu
 * @date: 2020-02-14
 * @remark:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryResourceByUserIdResponse implements Serializable {

    private static final long serialVersionUID = -7794769182973090392L;

    @ApiModelProperty(notes = "资源标题")
    private String resourceTitle;
    @ApiModelProperty(notes = "发布时间 yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date issureDate;
    @ApiModelProperty(notes = "发布状态 0待审核 1系统审核通过 2待人工审核 3人工审核通过 4人工审核不通过")
    private String issureStatus;

    @ApiModelProperty(notes = "浏览量")
    private String browseNum;

    @ApiModelProperty(notes = "分享数")
    private String shareNum;

    @ApiModelProperty(notes = "资源Id")
    private String resourceId;
}
