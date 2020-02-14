package com.gy.resource.service.impl;


import com.gy.resource.entity.DictionaryCodeModel;
import com.gy.resource.mapper.DictionaryCodeMapper;
import com.gy.resource.service.PDictionaryCodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.jic.common.base.vo.Page;
import com.jic.common.base.vo.PageResult;

/**
 *  字典表 服务类
 *
 * @author xuyongliang
 * @since 2020-02-14
 */
@Service
public class DictionaryCodeServiceImpl implements PDictionaryCodeService{

    @Autowired
    private DictionaryCodeMapper modelMapper;

    /**
     * 字典表 新增
     * @param modelEntity
     */
    @Override
    public Integer dictionaryCodeAdd(DictionaryCodeModel modelEntity){
        return modelMapper.dictionaryCodeAdd(modelEntity);
    }

    /**
     * 字典表 编辑
     * @param modifyEntity
     * @param whereCondition
     */
    @Override
    public Integer dictionaryCodeEdit(DictionaryCodeModel modifyEntity, DictionaryCodeModel whereCondition){
        return modelMapper.dictionaryCodeEdit(modifyEntity, whereCondition);
    }

    /**
     * 字典表 删除
     * @param \
     */
    @Override
    public Integer dictionaryCodeDelete(Map map){
        return modelMapper.dictionaryCodeDelete(map);
    }

    /**
     * 字典表 查询详情
     * @param
     */
    @Override
    public DictionaryCodeModel dictionaryCodeQuery(Map map){
        return modelMapper.dictionaryCodeQuery(map);
    }

    /**
     * 字典表 分页查询
     * @param
     */
    @Override
    public PageResult<DictionaryCodeModel> dictionaryCodeQueryPageList(DictionaryCodeModel modelEntity, Page pageQuery){
        //计算下标
        int startIndex = (pageQuery.getStart() - 1) * pageQuery.getLimit();
        List<DictionaryCodeModel> list = modelMapper.dictionaryCodeQueryPageList(startIndex, pageQuery.getLimit(), modelEntity);
        long count = modelMapper.dictionaryCodeQueryPageCount(modelEntity);
        PageResult pageResult = new PageResult();
        pageResult.setRows(list);
        pageResult.setTotal(count);
        return pageResult;
    }

    /**
    *  字典表 修改单据状态
    *  @param \
    */
    @Override
    public Integer dictionaryCodeChangeStatus(Map map){
        return modelMapper.dictionaryCodeChangeStatus(map);
    }

    /**
     *  字典表 修改审批状态
     * @param \
     */
    @Override
    public Integer dictionaryCodeChangeApproveStatus(Map map){
        return modelMapper.dictionaryCodeChangeApproveStatus(map);
    }

}