package com.gy.resource.controller.manager;

import com.gy.resource.api.manager.DictionaryApi;
import com.gy.resource.request.manager.QueryDictionaryRequest;
import com.gy.resource.response.manager.QueryDictionaryResponse;
import com.gy.resource.service.DictionaryService;
import com.jic.common.base.vo.RestResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gaolanyu
 * @date: 2020-02-14
 * @remark:
 */
@RestController
@RequestMapping("/resource-dictionary")
@Api(tags = {"资源后台管理接口"})
@Slf4j
public class DictionController implements DictionaryApi {
    @Autowired
    DictionaryService dictionaryService;

    @ApiOperation(value = "字典值查询")
    @PostMapping(value = "/query-dictionary")
    public RestResult<List<QueryDictionaryResponse>> getDictionaryByCode(@RequestBody QueryDictionaryRequest request) {
        return dictionaryService.getDictionaryByCode(request);
    }
}
