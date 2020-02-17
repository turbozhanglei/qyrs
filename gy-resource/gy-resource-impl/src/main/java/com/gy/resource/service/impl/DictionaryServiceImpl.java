package com.gy.resource.service.impl;

import com.gy.resource.entity.DictionaryCodeModel;
import com.gy.resource.mapper.DictionaryMapper;
import com.gy.resource.request.manager.QueryDictionaryRequest;
import com.gy.resource.response.manager.QueryDictionaryResponse;
import com.gy.resource.service.DictionaryService;
import com.jic.common.base.vo.RestResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: gaolanyu
 * @date: 2020-02-15
 * @remark:
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    DictionaryMapper dictionaryMapper;

    @Override
    public RestResult<List<QueryDictionaryResponse>> getDictionaryByCode(QueryDictionaryRequest request) {
        List<DictionaryCodeModel> codeModels = dictionaryMapper.queryDictionCodeModel(request.getCode());
        if (CollectionUtils.isEmpty(codeModels)) {
            return RestResult.success(new ArrayList<>());
        }
        List<QueryDictionaryResponse> responseList = new ArrayList<>();
        for (DictionaryCodeModel model : codeModels) {
            QueryDictionaryResponse response=new QueryDictionaryResponse();
            response.setDictionaryKey(model.getCode().toString());
            response.setDictionaryValue(model.getDesc());

            responseList.add(response);
        }
        return RestResult.success(responseList);
    }
}
