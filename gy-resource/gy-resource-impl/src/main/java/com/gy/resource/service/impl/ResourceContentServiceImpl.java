package com.gy.resource.service.impl;

import com.gy.resource.entity.ResourceInfo;
import com.gy.resource.mapper.ResourceInfoMapper;
import com.gy.resource.service.ResourceContentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: gaolanyu
 * @date: 2020-02-18
 * @remark:
 */
@Service
@Slf4j
public class ResourceContentServiceImpl implements ResourceContentService {
    @Autowired
    ResourceInfoMapper resourceInfoMapper;
    @Override
    public void sensitiveFilter() {
        //TODO 正常应该分页查询，限制内存里的数据大小
        //TODO 正常应该加锁，防止多台机器一起处理数据，利用分片，分布式跑批
        List<ResourceInfo> fileterList=querySensiticeFilerList();
        if(CollectionUtils.isEmpty(fileterList)){
            return;
        }
    }

    public List<ResourceInfo> querySensiticeFilerList(){
        List<ResourceInfo> resourceInfoList=resourceInfoMapper.querySensitive();
        return resourceInfoList;
    }

    public void sensiticeFilter(ResourceInfo resourceInfo){
        log.debug("敏感词过滤开始,content:{}",resourceInfo.getContent());
    }
}
