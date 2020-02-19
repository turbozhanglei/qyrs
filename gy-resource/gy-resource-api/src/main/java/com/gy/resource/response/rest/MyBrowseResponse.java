package com.gy.resource.response.rest;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**n
 * @author zhuxiankun
 * @version V1.0
 * @description 我的浏览记录
 * @date 2020/2/15
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyBrowseResponse implements Serializable{
    @ApiModelProperty(notes = "当前资源id")
    private Long resourceId;
    @ApiModelProperty(notes = "发布用户id")
    private Long userId;
    @ApiModelProperty(notes = "文章标题")
    private String title;
    @ApiModelProperty(notes = "发布时间")
    private String createTime;

    @ApiModelProperty(notes = "当前文章人的头像")
    private String headPic;

    @ApiModelProperty(notes = "当前文章人的昵称")
    private String nickname;

    @ApiModelProperty(notes = "当前文章浏览数")
    private Integer browses;

    @ApiModelProperty(notes = "当前文章发布人的电话号码")
    private String  mobile;
}
