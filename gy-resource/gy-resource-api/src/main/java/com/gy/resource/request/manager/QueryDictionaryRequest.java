package com.gy.resource.request.manager;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: gaolanyu
 * @date: 2020-02-13
 * @remark:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryDictionaryRequest implements Serializable {
    private static final long serialVersionUID = 922988962144567822L;
    @ApiModelProperty(notes = "字典码")
    private String code;
}
