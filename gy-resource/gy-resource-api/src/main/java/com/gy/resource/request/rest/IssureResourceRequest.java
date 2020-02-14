package com.gy.resource.request.rest;

import com.gy.resource.request.TokenRequest;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author: gaolanyu
 * @date: 2020-02-13
 * @remark:
 *
 * 请求参数: 1.发布人id 2.消息标题 3.发布类型 4.资源标签 5.资源区域 6.内贸外贸 7.消息内容
 *
 * 返回参数: 1.资源id
 *
 * 逻辑:
 *
 * 消息标题：必填项，至少6个字符，最多不超过支持50字，字数不够发布信息按钮置灰不可点击
 *
 * 发布类型：必选项，单选项，默认无选中，选中其中一个，其他按钮变为未选中状态，若未选择，发布信息按钮置灰不可点击
 *
 * 资源标签：必选项，单选项，默认无选中，选中其中一个，其他按钮变为未选中状态，若未选择，发布信息按钮置灰不可点击
 *
 * 资源区域：必选项，单选项，默认无选中，选中其中一个，其他按钮变为未选中状态，若未选择，发布信息按钮置灰不可点击
 *
 * 内贸外贸：必选项，单选项，默认无选中，选中其中一个，其他按钮变为未选中状态，若未选择，发布信息按钮置灰不可点击
 *
 * 消息内容：必填项，至少6个字符，支持文本和表情，最多不超过500字，字数不够发布信息按钮置灰不可点击，点击发布信息按钮提交资源信息成功，然后自动跳转至首页
 */
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IssureResourceRequest extends TokenRequest {
    private static final long serialVersionUID = 6472584298779825446L;
    @ApiModelProperty(notes = "发布人id",required = true)
    private String issureId;

    @ApiModelProperty(notes = "资源标题",required = true)
    private String resourceTitle;

    @ApiModelProperty(notes = "资源发布类型 0求购 1出售",required = true)
    private String resourceType;

    @ApiModelProperty(notes = "资源标签 0乙二醇 1PTA 2成品油 3PVC 4甲醇 5塑料 6其他",required = true)
    private String resourceLabel;

    @ApiModelProperty(notes = "资源区域 0东北 1华东 2华北 3华中 4华南 5西南 6西北 7境外",required = true)
    private String resourceArea;

    @ApiModelProperty(notes = "内贸外贸 0内贸 1进口 2出口",required = true)
    private String tradeType;

    @ApiModelProperty(notes = "资源信息内容",required = true)
    private String resourceContent;
}
