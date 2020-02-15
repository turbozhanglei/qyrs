package com.gy.resource.request.rest;

import com.gy.resource.request.TokenRequest;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author: gaolanyu
 * @date: 2020-02-13
 * @remark: 请求参数: 1.发布人id 2.消息标题 3.发布类型 4.资源标签 5.资源区域 6.内贸外贸 7.消息内容
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
    @ApiModelProperty(notes = "发布人id", required = true)
    private String issureId;

    @ApiModelProperty(notes = "资源标题", required = true)
    @NotNull(message = "资源标题必填")
    private String resourceTitle;

    @ApiModelProperty(notes = "资源发布类型 -1全部 0求购 1出售", required = true,example ="0")
    private String resourceType;

    @ApiModelProperty(notes = "资源标签 -1全部 1乙二醇 2PTA 3成品油 4PVC 5甲醇 6塑料 0其他", required = true,example = "1")
    private String resourceLabel;

    @ApiModelProperty(notes = "资源区域 -1全部 1东北 2华东 3华北 4华中 5华南 6西南 7西北 8境外", required = true,example = "1")
    private String resourceArea;

    @ApiModelProperty(notes = "内贸外贸 -1全部 1内贸 2进口 3出口", required = true,example = "1")
    private String tradeType;

    @ApiModelProperty(notes = "资源信息内容", required = true,example = "")
    private String resourceContent;
}
