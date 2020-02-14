package com.gy.resource.response.manager;

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
public class ReportResponse implements Serializable {
    private static final long serialVersionUID = -5431924102183977828L;

    @ApiModelProperty(notes = "资源Id")
    private String resourceId;

    @ApiModelProperty(notes = "资源标题")
    private String resourceTitle;

    @ApiModelProperty(notes = "资源发布类型 0求购 1出售")
    private String resourceType;

    @ApiModelProperty(notes = "资源标签 0乙二醇 1PTA 2成品油 3PVC 4甲醇 5塑料 6其他")
    private String resourceLabel;

    @ApiModelProperty(notes = "资源区域 0东北 1华东 2华北 3华中 4华南 5西南 6西北 7境外")
    private String resourceArea;

    @ApiModelProperty(notes = "内贸外贸 0内贸 1进口 2出口")
    private String tradeType;

    @ApiModelProperty(notes = "发布状态 0待审核 1系统审核通过 2待人工审核 3人工审核通过 4人工审核不通过")
    private String issureStatus;

    @ApiModelProperty(notes = "置顶状态 0普通 1置顶")
    private String topStatus;

    @ApiModelProperty(notes = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    @ApiModelProperty(notes = "发布人Id")
    private String issureUserId;

    @ApiModelProperty(notes = "发布人电话")
    private String issurePhone;

    @ApiModelProperty(notes = "浏览量")
    private String browseNum;

    @ApiModelProperty(notes = "分享数")
    private String shareNum;

    @ApiModelProperty(notes = "拨打电话数")
    private String phoneNum;

}
