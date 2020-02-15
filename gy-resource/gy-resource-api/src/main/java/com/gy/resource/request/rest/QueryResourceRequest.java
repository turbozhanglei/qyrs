package com.gy.resource.request.rest;

import com.gy.resource.request.TokenRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author: gaolanyu
 * @date: 2020-02-13
 * @remark: 请求参数: 1.资源id 2.当前用户id 3.0审核通过 1全部 返回参数: 1.发布人id 2.发布人头像 3.发布人昵称 4.所属公司名称 5.资源信息标题 6.发布时间
 * 7.资源发布类型 8.资源标签 9.资源区域 10.内贸外贸 11.内容 12.浏览量 13.分享数 14.发布人电话 15.分享链接 16.创建时间 17.资源信息类型 18.发布状态
 * 19.置顶状态 20.审核人账号 21.审核时间 22.图片(暂无)
 *
 * 逻辑:用户无头像展示默认头像；昵称需要脱敏展示，脱敏规则同首页；所属公司名称如用户未填写则为空；资源信息标题全文展示；发布时间取该信息最新的审核通过时间，只展示年月日；浏览量取用户进入信息详情页的次数，浏览量每个用户ID只记一次；分享数取分享成功的次数，分享数每个用户ID只记一次，同一用户重复分享成功不增加分享数
 *
 * 记录浏览记录
 */
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QueryResourceRequest extends TokenRequest {
    private static final long serialVersionUID = 7376076744927305501L;

    @ApiModelProperty(notes = "资源Id", required = true)
    private String resourceId;

    @ApiModelProperty(notes = "查看资源人Id,后台管理页面不传，时间不够为了复用，后期再说")
    private String loginUserId;

//    @ApiModelProperty(notes = "查询条件,0审核通过 1审核不通过 2全部")
//    private String condition;
}
