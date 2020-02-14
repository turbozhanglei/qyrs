package com.gy.resource.request.manager;

import java.io.Serializable;
import java.util.List;

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
public class CheckRequest implements Serializable {

    private static final long serialVersionUID = -4348674747346926405L;

    @ApiModelProperty(notes = "资源id")
    private String resourceIdList;

    @ApiModelProperty(notes = "检查状态 0审核通过 1审核不通过")
    private String checkStatus;
}
