package com.gy.resource.request.rest;

import com.gy.resource.request.TokenRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: gaolanyu
 * @date: 2020-02-27
 * @remark:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "拨打用户开关实体")
public class SetPhoneSwitchRequest extends TokenRequest {
    @ApiModelProperty(notes = "用户id",required = true)
    private String userId;

    @ApiModelProperty(notes = "0可以拨打 1不可以拨打",required = true)
    private String phoneStatus;
}
