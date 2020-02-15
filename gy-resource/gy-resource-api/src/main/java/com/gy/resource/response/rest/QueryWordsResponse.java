package com.gy.resource.response.rest;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author xuyongliang
 * @version V1.0
 * @className QueryWordsResponse
 * @description 查询联想词
 * @date 2020/2/14
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryWordsResponse implements Serializable {

    private static final long serialVersionUID = -4404144769689320754L;

    /**
     * 联想词
     */
    @ApiModelProperty(notes = "联想词")
    private String word;

    /**
     * 品类code
     */
    @ApiModelProperty(notes = "品类code")
    private Integer code;

    /**
     * 品类
     */
    @ApiModelProperty(notes = "品类")
    private String codeName;

}
