package com.gy.resource.response.rest;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhuxiankun
 * @version V1.0
 * @description 我的关注用户信息
 * @date 2020/2/15
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyFollowUserInfoResponse implements Serializable{

    @ApiModelProperty(notes = "关注用户id")
    private Long userId;
    @ApiModelProperty(notes = "关注用户头像")
    private String headpic;
    @ApiModelProperty(notes = "关注用户昵称")
    private String nickname;
    @ApiModelProperty(notes = "用户最新发布的资源")
    private MyFollowPeopleResourceResponse myFollowPeopleResourceResponse;
}
