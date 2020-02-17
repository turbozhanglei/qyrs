package com.gy.resource.response.rest;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xuyongliang
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
    private Long headPic;
    @ApiModelProperty(notes = "关注用户昵称")
    private Long nickname;

}
