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
 * 请求参数:1.发布类型 2.资源标签 3.资源区域 4.内贸外贸（名称待定）
 * 返回参数: 1.发布人Id 2.发布人头像 3.发布人昵称 4.资源信息 标题 5.发布时间 6.浏览量 7.发布人电话 8.资源id
 * 逻辑:首页资源信息读取后台审核通过的十条资源信息，优先读取置顶的资源信息，不够十条则补充发布时间最近的资源信息，排序根据发布时间由近到远排序。展示元素包括发布人头像和昵称、资源信息标题、发布时间、浏览量、拨打电话按钮。无头像展示默认头像；昵称需要脱敏，脱敏规则为：显示首字母（中文字）和尾字母（中文字），其余用“”代替，例如“王心苑”，输出“王苑”、例如“happy”，输出“h*y”；若用户名仅为2个字母（中文字），则末尾用“”代替，例如“桂鹏”，输出“桂”；若用户名仅为1个字母（中文字），则全部展示；资源信息标题一行展示不下打省略号；发布时间取该信息最新的审核通过时间，只展示年月日；浏览量取用户进入信息详情页的次数，浏览量每个用户ID只记一次；点击拨打电话按钮跳转至拨打手机页面，号码为用户登录的手机号码，使用该功能需提前登录。点击资源信息左侧区域跳转至对应资源信息详情页，需提前登录。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryResourceByConditionResponse implements Serializable {
    @ApiModelProperty(notes = "发布人Id")
    private String issureUserId;
    @ApiModelProperty(notes = "发布人头像")
    private String issureHeadImage;

    @ApiModelProperty(notes = "发布人昵称")
    private String issureNickName;

    @ApiModelProperty(notes = "资源标题")
    private String resourceTitle;

    @ApiModelProperty(notes = "发布时间 yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date issureDate;

    @ApiModelProperty(notes = "浏览量")
    private String browseNum;

    @ApiModelProperty(notes = "发布人电话")
    private String issurePhone;

    @ApiModelProperty(notes = "资源Id")
    private String resourceId;
}
