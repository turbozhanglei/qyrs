package com.gy.resource.response.manager;

import com.alibaba.excel.annotation.ExcelProperty;
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
    @ExcelProperty("资源Id")
    private String resourceId;

    @ApiModelProperty(notes = "资源标题")
    @ExcelProperty("资源标题")
    private String resourceTitle;

    @ApiModelProperty(notes = "资源发布类型 1求购 0出售")
    @ExcelProperty("资源发布类型")
    private String resourceType;

    @ApiModelProperty(notes = "资源标签 1乙二醇 2PTA 3成品油 4PVC 5甲醇 6塑料 0其他", required = true)
    @ExcelProperty("资源标签")
    private String resourceLabel;

    @ApiModelProperty(notes = "资源区域 1东北 2华东 3华北 4华中 5华南 6西南 7西北 8境外")
    @ExcelProperty("资源区域")
    private String resourceArea;

    @ApiModelProperty(notes = "内贸外贸 1内贸 2进口 3出口")
    @ExcelProperty("内贸外贸")
    private String tradeType;

    @ExcelProperty("发布状态")
    @ApiModelProperty(notes = "发布状态 0待审核 1系统审核通过 2待人工审核 3人工审核通过 4人工审核不通过")
    private String issureStatus;

    @ApiModelProperty(notes = "置顶状态 0普通 1置顶")
    @ExcelProperty("置顶状态")
    private String topStatus;

    @ApiModelProperty(notes = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty(notes = "发布人Id")
    @ExcelProperty("发布人Id")
    private String issureUserId;

    @ExcelProperty("发布人电话")
    @ApiModelProperty(notes = "发布人电话")
    private String issurePhone;

    @ApiModelProperty(notes = "浏览量")
    @ExcelProperty("浏览量")
    private String browseNum;

    @ExcelProperty("分享数")
    @ApiModelProperty(notes = "分享数")
    private String shareNum;

    @ExcelProperty("拨打电话数")
    @ApiModelProperty(notes = "拨打电话数")
    private String phoneNum;

}
