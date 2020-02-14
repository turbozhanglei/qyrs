package com.gy.resource.service.impl;


import com.gy.resource.entity.SearchHistoryModel;
import com.gy.resource.mapper.SearchHistoryMapper;
import com.gy.resource.service.PSearchHistoryService;
import com.jic.common.base.vo.Page;
import com.jic.common.base.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *  搜索历史 服务类
 *
 * @author xuyongliang
 * @since 2020-02-14
 */
@Service
public class SearchHistoryServiceImpl implements PSearchHistoryService{

    @Autowired
    private SearchHistoryMapper modelMapper;

    /**
     * 搜索历史 新增
     * @param modelEntity
     */
    @Override
    public Integer searchHistoryAdd(SearchHistoryModel modelEntity){
        return modelMapper.searchHistoryAdd(modelEntity);
    }

    /**
     * 搜索历史 编辑
     * @param modifyEntity
     * @param whereCondition
     */
    @Override
    public Integer searchHistoryEdit(SearchHistoryModel modifyEntity, SearchHistoryModel whereCondition){
        return modelMapper.searchHistoryEdit(modifyEntity, whereCondition);
    }

    /**
     * 搜索历史 删除
     * @param \
     */
    @Override
    public Integer searchHistoryDelete(Map map){
        return modelMapper.searchHistoryDelete(map);
    }

    /**
     * 搜索历史 查询详情
     * @param
     */
    @Override
    public SearchHistoryModel searchHistoryQuery(Map map){
        return modelMapper.searchHistoryQuery(map);
    }

    /**
     * 搜索历史 分页查询
     * @param
     */
    @Override
    public PageResult<SearchHistoryModel> searchHistoryQueryPageList(SearchHistoryModel modelEntity, Page pageQuery){
        //计算下标
        int startIndex = (pageQuery.getStart() - 1) * pageQuery.getLimit();
        List<SearchHistoryModel> list = modelMapper.searchHistoryQueryPageList(startIndex, pageQuery.getLimit(), modelEntity);
        long count = modelMapper.searchHistoryQueryPageCount(modelEntity);
        PageResult pageResult = new PageResult();
        pageResult.setRows(list);
        pageResult.setTotal(count);
        return pageResult;
    }

    /**
    *  搜索历史 修改单据状态
    *  @param \
    */
    @Override
    public Integer searchHistoryChangeStatus(Map map){
        return modelMapper.searchHistoryChangeStatus(map);
    }

    /**
     *  搜索历史 修改审批状态
     * @param \
     */
    @Override
    public Integer searchHistoryChangeApproveStatus(Map map){
        return modelMapper.searchHistoryChangeApproveStatus(map);
    }

}