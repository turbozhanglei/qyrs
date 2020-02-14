package com.gy.resource.controller.manager;

import com.gy.resource.api.manager.DictionaryApi;
import com.gy.resource.request.manager.QueryDictionaryRequest;
import com.gy.resource.response.manager.QueryDictionaryResponse;
import com.jic.common.base.vo.RestResult;

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
    @ApiOperation(value = "字典值查询")
    public RestResult<List<QueryDictionaryResponse>> getDictionaryByCode(@RequestBody QueryDictionaryRequest request) {
        return null;
    }
}
