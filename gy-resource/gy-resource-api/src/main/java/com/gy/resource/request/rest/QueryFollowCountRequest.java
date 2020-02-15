package com.gy.resource.request.rest;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xuyongliang
 * @version V1.0
 * @className FollowUserRequest
 * @description TODO
 * @date 2020/2/15
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryFollowCountRequest implements Serializable{

    private static final long serialVersionUID = 1653942711059438738L;

    @ApiModelProperty(notes = "用户 token")
    private String token;

    @ApiModelProperty(notes = "资源id、文章id、用户id ")
    private Long refId;

    @ApiModelProperty(notes = "关联类型，0、关注用户，1、资源浏览数，2、资源分享数，3、资源拨打电话数，4、资讯文章分享数，5、资讯文章点赞数，6、资讯文章浏览数 ")
    private Integer refType;
}
