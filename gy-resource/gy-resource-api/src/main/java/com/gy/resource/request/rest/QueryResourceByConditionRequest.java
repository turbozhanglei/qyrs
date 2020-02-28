package com.gy.resource.request.rest;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: gaolanyu
 * @date: 2020-02-14
 * @remark:
 * 请求参数:1.发布类型 2.资源标签 3.资源区域 4.内贸外贸（名称待定）5 资源标题（模糊） 6浏览量升序 7浏览量降序 7分享数升序 8分享数降序
 * 返回参数: 1.发布人Id 2.发布人头像 3.发布人昵称 4.资源信息 标题 5.发布时间 6.浏览量 7.发布人电话 8.资源id
 * 逻辑:首页资源信息读取后台审核通过的十条资源信息，优先读取置顶的资源信息，不够十条则补充发布时间最近的资源信息，排序根据发布时间由近到远排序。展示元素包括发布人头像和昵称、资源信息标题、发布时间、浏览量、拨打电话按钮。无头像展示默认头像；昵称需要脱敏，脱敏规则为：显示首字母（中文字）和尾字母（中文字），其余用“”代替，例如“王心苑”，输出“王苑”、例如“happy”，输出“h*y”；若用户名仅为2个字母（中文字），则末尾用“”代替，例如“桂鹏”，输出“桂”；若用户名仅为1个字母（中文字），则全部展示；资源信息标题一行展示不下打省略号；发布时间取该信息最新的审核通过时间，只展示年月日；浏览量取用户进入信息详情页的次数，浏览量每个用户ID只记一次；点击拨打电话按钮跳转至拨打手机页面，号码为用户登录的手机号码，使用该功能需提前登录。点击资源信息左侧区域跳转至对应资源信息详情页，需提前登录。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryResourceByConditionRequest implements Serializable {
    private static final long serialVersionUID = 5349641787908107232L;

    @ApiModelProperty(notes = "资源发布类型 -1全部 0出售 1求购")
    private String resourceType;

    @ApiModelProperty(notes = "资源标签 -1全部 1乙二醇 2PTA 3成品油 4PVC 5甲醇 6塑料 0其他", required = true)
    private String resourceLabel;

    @ApiModelProperty(notes = "资源区域 -1全部 1东北 2华东 3华北 4华中 5华南 6西南 7西北 8境外")
    private String resourceArea;

    @ApiModelProperty(notes = "内贸外贸 -1全部 1内贸 2进口 3出口")
    private String tradeType;

    @ApiModelProperty(notes = "资源标题模糊搜索")
    private String resourceTitle;

    @ApiModelProperty(notes = "资源内容模糊搜索")
    private String resourceContent;

    @ApiModelProperty(notes = "浏览量排序 0升序 1降序 不需要排序不传")
    private String browseUpNum;

//    @ApiModelProperty(notes = "浏览量降序 是降序则传1 否则不传")
//    private String browseDownNum;

    @ApiModelProperty(notes = "分享数排序 0升序 1降序 不需要排序不传")
    private String shareUpNum;

//    @ApiModelProperty(notes = "分享数降序")
//    private String shareDownNum;

    @ApiModelProperty(notes = "发布人用户Id")
    private String issureUserId;

    private Integer start = 1;
    private Integer limit = 10;

}
