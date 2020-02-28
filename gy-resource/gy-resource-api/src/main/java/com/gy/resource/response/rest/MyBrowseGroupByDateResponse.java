package com.gy.resource.response.rest;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**n
 * @author zhuxiankun
 * @version V1.0
 * @description 我的浏览记录  根据时间分组
 * @date 2020/2/15
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyBrowseGroupByDateResponse implements Serializable{
    @ApiModelProperty(notes = "浏览记录-按天")
    private Map<String,List<MyBrowseResponse>> myBrowseResponseList;
}
