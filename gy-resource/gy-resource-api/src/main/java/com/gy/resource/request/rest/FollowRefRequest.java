package com.gy.resource.request.rest;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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
public class FollowRefRequest implements Serializable{

    @ApiModelProperty(notes = "用户 token")
    private String token;

    @ApiModelProperty(notes = "资源id、文章id、用户id ")
    private Long refId;

    @ApiModelProperty(notes = "关注还是取消关注标示   0取消关注、取关，1 关注、点赞")
    private Integer followType;

    @ApiModelProperty(notes = "关联类型，0、关注用户，1、资源浏览数，2、资源分享数，3、资源拨打电话数，4、资讯文章分享数，5、资讯文章点赞数，6、资讯文章浏览数 ")
    private Integer refType;
}
