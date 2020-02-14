package com.gy.resource.service;
import com.gy.resource.entity.AssociationalWordModel;
import com.jic.common.base.vo.Page;
import com.jic.common.base.vo.PageResult;

import java.util.Map;

/**
 *  资源标签联想词 服务类
 *
 * @author xuyongliang
 * @since 2020-02-14
 */

public interface PAssociationalWordService {

    /**
     * 资源标签联想词 新增
     * @param modelEntity
     */
    Integer associationalWordAdd(AssociationalWordModel modelEntity);

    /**
     * 资源标签联想词 编辑
     * @param modifyEntity
     * @param whereCondition
     */
    Integer associationalWordEdit(AssociationalWordModel modifyEntity, AssociationalWordModel whereCondition);

    /**
     * 资源标签联想词 删除
     * @param \
     */
    Integer associationalWordDelete(Map map);

    /**
     * 资源标签联想词 查询详情
     * @param
     */
    AssociationalWordModel associationalWordQuery(Map map);

    /**
     * 资源标签联想词 分页查询
     * @param
     */
    PageResult<AssociationalWordModel> associationalWordQueryPageList(AssociationalWordModel modelEntity, Page pageQuery);

    /**
    *  资源标签联想词 修改单据状态
    *  @param \
    */
    Integer associationalWordChangeStatus(Map map);

    /**
     *  资源标签联想词 修改审批状态
     * @param \
     */
    Integer associationalWordChangeApproveStatus(Map map);

}