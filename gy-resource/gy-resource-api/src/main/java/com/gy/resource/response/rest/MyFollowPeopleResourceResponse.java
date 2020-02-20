package com.gy.resource.response.rest;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**n
 * @author zhuxiankun
 * @version V1.0
 * @description 我的关注人的最新资源
 * @date 2020/2/15
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyFollowPeopleResourceResponse implements Serializable{

    @ApiModelProperty(notes = "发布用户id")
    private Long userId;
    @ApiModelProperty(notes = "手机号")
    private String mobile;
    @ApiModelProperty(notes = "文章标题")
    private String title;
    @ApiModelProperty(notes = "资源内容")
    private String content;
    @ApiModelProperty(notes = "发布时间")
    private String auditTime;
    @ApiModelProperty(notes = "发布类型")
    private String releaseType;
    @ApiModelProperty(notes = "资源标签")
    private String resourceLabel;
    @ApiModelProperty(notes = "资源区域")
    private String resourceArea;
    @ApiModelProperty(notes = "进出口")
    private String resourceTrade;

}
