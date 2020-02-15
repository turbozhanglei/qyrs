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
 * @date: 2020-02-13
 * @remark:
 * 请求参数: 1.资源id 2.当前用户id 3.0审核通过 1全部
 * 返回参数: 1.发布人id 2.发布人头像 3.发布人昵称 4.所属公司名称 5.资源信息标题 6.发布时间 7.资源发布类型 8.资源标签 9.资源区域 10.内贸外贸 11.内容
 * 12.浏览量 13.分享数 14.发布人电话 15.分享链接 16.创建时间 17.资源信息类型
 * 18.发布状态 19.置顶状态 20.审核人账号 21.审核时间 22.图片(暂无)
 *
 * 逻辑:用户无头像展示默认头像；昵称需要脱敏展示，脱敏规则同首页；所属公司名称如用户未填写则为空；资源信息标题全文展示；发布时间取该信息最新的审核通过时间，只展示年月日；浏览量取用户进入信息详情页的次数，浏览量每个用户ID只记一次；分享数取分享成功的次数，分享数每个用户ID只记一次，同一用户重复分享成功不增加分享数
 *
 * 记录浏览记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryResourceResponse implements Serializable {
    private static final long serialVersionUID = 4783249151534663019L;

    @ApiModelProperty(notes = "发布人Id")
    private String issureUserId;

    @ApiModelProperty(notes = "发布人头像")
    private String issureHeadImage;

    @ApiModelProperty(notes = "发布人昵称")
    private String issureNickName;

    @ApiModelProperty(notes = "公司名称")
    private String companyName;

    @ApiModelProperty(notes = "资源标题")
    private String resourceTitle;

    @ApiModelProperty(notes = "发布时间 yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date issureDate;

    @ApiModelProperty(notes = "资源发布类型 0求购 1出售")
    private String resourceType;

    @ApiModelProperty(notes = "资源标签 1乙二醇 2PTA 3成品油 4PVC 5甲醇 6塑料 0其他", required = true)
    private String resourceLabel;

    @ApiModelProperty(notes = "资源区域 1东北 2华东 3华北 4华中 5华南 6西南 7西北 8境外", required = true)
    private String resourceArea;

    @ApiModelProperty(notes = "内贸外贸 0内贸 1进口 2出口")
    private String tradeType;

    @ApiModelProperty(notes = "资源信息内容")
    private String resourceContent;

    @ApiModelProperty(notes = "浏览量")
    private String browseNum;

    @ApiModelProperty(notes = "分享数")
    private String shareNum;

    @ApiModelProperty(notes = "发布人电话")
    private String issurePhone;

    @ApiModelProperty(notes = "分享链接")
    private String shareLink;

    @ApiModelProperty(notes = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    @ApiModelProperty(notes = "发布状态 0待审核 1系统审核通过 2待人工审核 3人工审核通过 4人工审核不通过")
    private String issureStatus;

    @ApiModelProperty(notes = "置顶状态 0普通 1置顶")
    private String topStatus;

    @ApiModelProperty(notes = "审核人账号")
    private String checkAccount;

    @ApiModelProperty(notes = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkDate;
}
