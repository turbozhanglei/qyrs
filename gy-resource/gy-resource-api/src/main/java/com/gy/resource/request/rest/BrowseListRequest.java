package com.gy.resource.request.rest;

import java.io.Serializable;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author xuyongliang
 * @version V1.0
 * @className BrowseListRequest
 * @description TODO
 * @date 2020/2/17
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrowseListRequest implements Serializable {

    private static final long serialVersionUID = -6067189626808022797L;

    /**
     * 用户 token
     */
    private String token;

}
