package com.gy.resource.response.rest;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

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
    @ApiModelProperty(notes = "发布时间---按天分组")
    private String createDate;
    @ApiModelProperty(notes = "浏览记录-按天")
    private List<MyBrowseResponse> myBrowseResponseList;

}
