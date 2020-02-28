package com.gy.resource.service.impl;


import com.gy.resource.entity.AssociationalWordModel;
import com.gy.resource.mapper.AssociationalWordMapper;
import com.gy.resource.service.PAssociationalWordService;
import com.jic.common.base.vo.Page;
import com.jic.common.base.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *  资源标签联想词 服务类
 *
 * @author xuyongliang
 * @since 2020-02-14
 */
@Service
public class AssociationalWordServiceImpl implements PAssociationalWordService{



    @Autowired
    private AssociationalWordMapper modelMapper;

    /**
     * 资源标签联想词 新增
     * @param modelEntity
     */
    @Override
    public Integer associationalWordAdd(AssociationalWordModel modelEntity){
        return modelMapper.associationalWordAdd(modelEntity);
    }

    /**
     * 资源标签联想词 编辑
     * @param modifyEntity
     * @param whereCondition
     */
    @Override
    public Integer associationalWordEdit(AssociationalWordModel modifyEntity, AssociationalWordModel whereCondition){
        return modelMapper.associationalWordEdit(modifyEntity, whereCondition);
    }

    /**
     * 资源标签联想词 删除
     * @param \
     */
    @Override
    public Integer associationalWordDelete(Map map){
        return modelMapper.associationalWordDelete(map);
    }

    /**
     * 资源标签联想词 查询详情
     * @param
     */
    @Override
    public AssociationalWordModel associationalWordQuery(Map map){
        return modelMapper.associationalWordQuery(map);
    }

    /**
     * 资源标签联想词 分页查询
     * @param
     */
    @Override
    public PageResult<AssociationalWordModel> associationalWordQueryPageList(AssociationalWordModel modelEntity, Page pageQuery){
        //计算下标
        int startIndex = (pageQuery.getStart() - 1) * pageQuery.getLimit();
        List<AssociationalWordModel> list = modelMapper.associationalWordQueryPageList(startIndex, pageQuery.getLimit(), modelEntity);
        long count = modelMapper.associationalWordQueryPageCount(modelEntity);
        PageResult pageResult = new PageResult();
        pageResult.setRows(list);
        pageResult.setTotal(count);
        return pageResult;
    }

    @Override
    public List<AssociationalWordModel> associationalWordFuzzyWordQuery(String fuzzyWord) {
        int startIndex =0;
        int limit =10;
        return modelMapper.associationalWordFuzzyQuery(startIndex, limit, fuzzyWord);
    }

    /**
    *  资源标签联想词 修改单据状态
    *  @param \
    */
    @Override
    public Integer associationalWordChangeStatus(Map map){
        return modelMapper.associationalWordChangeStatus(map);
    }

    /**
     *  资源标签联想词 修改审批状态
     * @param \
     */
    @Override
    public Integer associationalWordChangeApproveStatus(Map map){
        return modelMapper.associationalWordChangeApproveStatus(map);
    }

}