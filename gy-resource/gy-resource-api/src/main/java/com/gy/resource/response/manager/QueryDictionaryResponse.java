package com.gy.resource.response.manager;

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
public class QueryDictionaryResponse implements Serializable {
    private static final long serialVersionUID = 8828417916633594207L;
    @ApiModelProperty(notes = "字典key")
    private String dictionaryKey;

    @ApiModelProperty(notes = "字典value")
    private String dictionaryValue;

}
