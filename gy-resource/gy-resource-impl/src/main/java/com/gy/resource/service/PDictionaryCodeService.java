package com.gy.resource.service;
import com.gy.resource.entity.DictionaryCodeModel;
import com.jic.common.base.vo.Page;
import com.jic.common.base.vo.PageResult;

import java.util.Map;

/**
 *  字典表 服务类
 *
 * @author xuyongliang
 * @since 2020-02-14
 */

public interface PDictionaryCodeService {

    /**
     * 字典表 新增
     * @param modelEntity
     */
    Integer dictionaryCodeAdd(DictionaryCodeModel modelEntity);

    /**
     * 字典表 编辑
     * @param modifyEntity
     * @param whereCondition
     */
    Integer dictionaryCodeEdit(DictionaryCodeModel modifyEntity, DictionaryCodeModel whereCondition);

    /**
     * 字典表 删除
     * @param \
     */
    Integer dictionaryCodeDelete(Map map);

    /**
     * 字典表 查询详情
     * @param
     */
    DictionaryCodeModel dictionaryCodeQuery(Map map);

    /**
     * 字典表 分页查询
     * @param
     */
    PageResult<DictionaryCodeModel> dictionaryCodeQueryPageList(DictionaryCodeModel modelEntity, Page pageQuery);

    /**
    *  字典表 修改单据状态
    *  @param \
    */
    Integer dictionaryCodeChangeStatus(Map map);

    /**
     *  字典表 修改审批状态
     * @param \
     */
    Integer dictionaryCodeChangeApproveStatus(Map map);

}