package com.gy.resource.request.manager;

import com.gy.resource.request.TokenRequest;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

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
public class CheckBatchRequest extends TokenRequest {
    private static final long serialVersionUID = -4348674747346926405L;

    @ApiModelProperty(notes = "审核人Id")
    private String checkUserId;

    @ApiModelProperty(notes = "资源id集合")
    private List<String> resourceIdList;

    @ApiModelProperty(notes = "检查状态 3人工审核通过，4人工审核不通过")
    @NotBlank(message = "检查状态不能传空")
    private String checkStatus;
}
