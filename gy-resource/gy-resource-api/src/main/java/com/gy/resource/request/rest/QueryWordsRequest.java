package com.gy.resource.request.rest;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author xuyongliang
 * @version V1.0
 * @className QueryWordsRequest
 * @description 查询联想词list
 * @date 2020/2/14
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryWordsRequest implements Serializable {

    private static final long serialVersionUID = 7642873520891576163L;

    /**
     * title 搜索
     */
    @ApiModelProperty(notes = "搜索")
    private String title;


}
