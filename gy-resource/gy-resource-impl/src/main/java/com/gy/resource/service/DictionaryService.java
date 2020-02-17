package com.gy.resource.service;

import com.gy.resource.request.manager.QueryDictionaryRequest;
import com.gy.resource.response.manager.QueryDictionaryResponse;
import com.jic.common.base.vo.RestResult;

import java.util.List;

/**
 * @author: gaolanyu
 * @date: 2020-02-15
 * @remark:
 */
public interface DictionaryService {
    public RestResult<List<QueryDictionaryResponse>> getDictionaryByCode(QueryDictionaryRequest request);
}
