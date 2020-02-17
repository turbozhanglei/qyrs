package com.gy.resource.response.rest;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author xuyongliang
 * @version V1.0
 * @description 我的关注
 * @date 2020/2/15
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyFollowResponse implements Serializable{

    @ApiModelProperty(notes = "关注用户列表")
    private List<MyFollowUserInfoResponse> myFollowUserInfoResponses;

    @ApiModelProperty(notes = "关注用户人数")
    private Integer total;
}
