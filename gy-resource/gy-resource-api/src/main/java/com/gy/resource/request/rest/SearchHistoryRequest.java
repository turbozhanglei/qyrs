package com.gy.resource.request.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 搜索历史
 *
 * @author xuyongliang
 * @since 2020-02-14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchHistoryRequest implements Serializable{

    /**
     * token
     */
    private String token;



}
