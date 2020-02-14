package com.gy.resource.request.manager;

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
public class TopRequest implements Serializable {
    private static final long serialVersionUID = 2961265888993919289L;

    @ApiModelProperty(notes = "资源id")
    private String resourceIdList;

    @ApiModelProperty(notes = "置顶状态 0取消置顶 1置顶")
    private String topStatus;
}
