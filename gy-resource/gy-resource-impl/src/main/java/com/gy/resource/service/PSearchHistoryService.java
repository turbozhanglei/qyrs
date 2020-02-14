package com.gy.resource.service;
import com.gy.resource.entity.SearchHistoryModel;
import com.jic.common.base.vo.Page;
import com.jic.common.base.vo.PageResult;

import java.util.Map;

/**
 *  搜索历史 服务类
 *
 * @author xuyongliang
 * @since 2020-02-14
 */

public interface PSearchHistoryService {

    /**
     * 搜索历史 新增
     * @param modelEntity
     */
    Integer searchHistoryAdd(SearchHistoryModel modelEntity);

    /**
     * 搜索历史 编辑
     * @param modifyEntity
     * @param whereCondition
     */
    Integer searchHistoryEdit(SearchHistoryModel modifyEntity, SearchHistoryModel whereCondition);

    /**
     * 搜索历史 删除
     * @param \
     */
    Integer searchHistoryDelete(Map map);

    /**
     * 搜索历史 查询详情
     * @param
     */
    SearchHistoryModel searchHistoryQuery(Map map);

    /**
     * 搜索历史 分页查询
     * @param
     */
    PageResult<SearchHistoryModel> searchHistoryQueryPageList(SearchHistoryModel modelEntity, Page pageQuery);

    /**
    *  搜索历史 修改单据状态
    *  @param \
    */
    Integer searchHistoryChangeStatus(Map map);

    /**
     *  搜索历史 修改审批状态
     * @param \
     */
    Integer searchHistoryChangeApproveStatus(Map map);

}