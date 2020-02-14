package com.gy.resource.request.manager;

import com.gy.resource.request.TokenRequest;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: gaolanyu
 * @date: 2020-02-14
 * @remark:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopRequest extends TokenRequest {
    private static final long serialVersionUID = 2961265888993919289L;

    @ApiModelProperty(notes = "操作人id")
    private String operaId;

    @ApiModelProperty(notes = "资源id")
    private String resourceIdList;

    @ApiModelProperty(notes = "置顶状态 0取消置顶 1置顶")
    private String topStatus;
}
