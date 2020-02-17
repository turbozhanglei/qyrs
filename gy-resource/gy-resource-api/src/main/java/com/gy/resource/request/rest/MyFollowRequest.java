package com.gy.resource.request.rest;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xuyongliang
 * @version V1.0
 * @description TODO
 * @date 2020/2/15
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyFollowRequest implements Serializable{

    @ApiModelProperty(notes = "用户 token")
    private String token;
    @ApiModelProperty(notes = "分页开始下标")
     private Integer start;
    @ApiModelProperty(notes = "每页条数")
    private Integer limit;
}
