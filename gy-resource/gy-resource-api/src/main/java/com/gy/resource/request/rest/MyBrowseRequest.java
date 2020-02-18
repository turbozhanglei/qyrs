package com.gy.resource.request.rest;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhuxiankun
 * @version V1.0
 * @description TODO
 * @date 2020/2/15
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyBrowseRequest implements Serializable{
    @ApiModelProperty(notes = "用户 token")
    private String token;
}
