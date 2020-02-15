package com.gy.resource.service.impl;


import com.gy.resource.entity.GlobalCorrelationModel;
import com.gy.resource.mapper.GlobalCorrelationMapper;
import com.gy.resource.service.PGlobalCorrelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.jic.common.base.vo.Page;
import com.jic.common.base.vo.PageResult;

/**
 *  全局关联信息表 服务类
 *
 * @author xuyongliang
 * @since 2020-02-14
 */
@Service
public class GlobalCorrelationServiceImpl implements PGlobalCorrelationService{

    @Autowired
    private GlobalCorrelationMapper modelMapper;

    /**
     * 全局关联信息表 新增
     * @param modelEntity
     */
    @Override
    public Integer globalCorrelationAdd(GlobalCorrelationModel modelEntity){
        return modelMapper.globalCorrelationAdd(modelEntity);
    }

    /**
     * 全局关联信息表 编辑
     * @param modifyEntity
     * @param whereCondition
     */
    @Override
    public Integer globalCorrelationEdit(GlobalCorrelationModel modifyEntity, GlobalCorrelationModel whereCondition){
        return modelMapper.globalCorrelationEdit(modifyEntity, whereCondition);
    }

    /**
     * 全局关联信息表 删除
     * @param \
     */
    @Override
    public Integer globalCorrelationDelete(Map map){
        return modelMapper.globalCorrelationDelete(map);
    }

    /**
     * 全局关联信息表 查询详情
     * @param
     */
    @Override
    public GlobalCorrelationModel globalCorrelationQuery(Map map){
        return modelMapper.globalCorrelationQuery(map);
    }

    /**
     * 全局关联信息表 分页查询
     * @param
     */
    @Override
    public PageResult<GlobalCorrelationModel> globalCorrelationQueryPageList(GlobalCorrelationModel modelEntity, Page pageQuery){
        //计算下标
        int startIndex = (pageQuery.getStart() - 1) * pageQuery.getLimit();
        List<GlobalCorrelationModel> list = modelMapper.globalCorrelationQueryPageList(startIndex, pageQuery.getLimit(), modelEntity);
        long count = modelMapper.globalCorrelationQueryPageCount(modelEntity);
        PageResult pageResult = new PageResult();
        pageResult.setRows(list);
        pageResult.setTotal(count);
        return pageResult;
    }

    @Override
    public Integer globalCorrelationQueryCount(GlobalCorrelationModel modelEntity) {
        Integer count = modelMapper.globalCorrelationQueryPageCount(modelEntity);
        return count;
    }

    /**
    *  全局关联信息表 修改单据状态
    *  @param \
    */
    @Override
    public Integer globalCorrelationChangeStatus(Map map){
        return modelMapper.globalCorrelationChangeStatus(map);
    }

    /**
     *  全局关联信息表 修改审批状态
     * @param \
     */
    @Override
    public Integer globalCorrelationChangeApproveStatus(Map map){
        return modelMapper.globalCorrelationChangeApproveStatus(map);
    }

}