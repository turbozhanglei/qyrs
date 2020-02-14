package com.gy.resource.request.manager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
public class DownloadRequest implements Serializable {
    private static final long serialVersionUID = -1254917447870927467L;
    @ApiModelProperty(notes = "要导出的数据集合")
    List<Map<String,String>> param;
}
