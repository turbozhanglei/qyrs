package com.gy.resource.request.manager;

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
 * 请求参数: 1.开始时间 2.结束时间 3.资源信息排序(0 浏览量从大到小 1分享数从大到小 2拨打电话数从大到小) 4.发布人手机号
 *
 * 返回参数:1.资源信息编码 2.资源信息标题名称 3资源信息类型
 * 4资源信息标签
 * 5资源区域
 * 6内贸外贸
 * 7发布状态
 * 8置顶状态
 * 9创建时间
 * 10.发布人用户ID
 * 11.发布人手机号
 * 12.浏览量
 * 13.分享数
 * 14.拨打电话数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest implements Serializable {

    private static final long serialVersionUID = 5051037640304872881L;

    @ApiModelProperty(notes = "创建开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createStartTime;

    @ApiModelProperty(notes = "创建结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createEndTime;

    @ApiModelProperty(notes = "0 浏览量从大到小 1分享数从大到小 2拨打电话数从大到小",required = true)
    private String resourceSort;

    @ApiModelProperty(notes = "发布人手机号")
    private String issurePhone;

    private int start=1;
    private int limit=10;


}
