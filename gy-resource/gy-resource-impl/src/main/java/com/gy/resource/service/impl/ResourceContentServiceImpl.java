package com.gy.resource.service.impl;

import com.gy.resource.constant.ResourceConstant;
import com.gy.resource.entity.ResourceInfo;
import com.gy.resource.mapper.ResourceInfoMapper;
import com.gy.resource.service.ResourceContentService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
        List<ResourceInfo> fileterList = querySensiticeFilerList();
        if (CollectionUtils.isEmpty(fileterList)) {
            return;
        }
        fileterList.stream().forEach(item->{
            sensiticeFilter(item);
        });
    }

    public List<ResourceInfo> querySensiticeFilerList() {
        List<ResourceInfo> resourceInfoList = resourceInfoMapper.querySensitive();
        return resourceInfoList;
    }

    public void sensiticeFilter(ResourceInfo resourceInfo) {
        log.debug("敏感词过滤开始,content:{}", resourceInfo.getContent());
        String word = resourceInfoMapper.querySensitiveOfManager();
        if(StringUtils.isBlank(word)) {
            log.info("没有配置敏感词库，请检查配置");
            return;
        }
        String content=resourceInfo.getContent();
        if(StringUtils.isBlank(content)){
            log.info("该资源内容为空,resourceId:{}",resourceInfo.getId());
            return;
        }
        String wordArray[]=word.split(",");
        List<String> sensiticeWordList=new ArrayList<>();
        Stream.of(wordArray).forEach(item->{
            if(content.contains(item)){
                sensiticeWordList.add(item);
            }
        });
        if(CollectionUtils.isEmpty(sensiticeWordList)){
            log.info("该资源内容没有敏感词 系统审核通过,resourceId:{}",resourceInfo.getId());
            long flag=resourceInfoMapper.check(ResourceConstant.check.system_check_success,ResourceConstant.check_person.system,resourceInfo.getId());
            if(flag<1){
                log.info("该资源内容审核异常，请检查数据，resourceId:{}",resourceInfo.getId());
            }
            return;
        }
        sensiticeWordList.stream().forEach(item -> {
            content.replace(item,"*");
        });
        long flag=resourceInfoMapper.systemCheckFail(content,ResourceConstant.check_person.system,resourceInfo.getId());
        if(flag<1){
            log.info("该资源内容审核异常，请检查数据，resourceId:{}",resourceInfo.getId());
        }
    }
}
