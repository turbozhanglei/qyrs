package com.gy.resource.response.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 搜索历史记录
 *
 * @author zhuxiankun
 * @since 2020-02-14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchHistoryResponse implements Serializable{

    /**
     * word
     */
    private String word;


}
