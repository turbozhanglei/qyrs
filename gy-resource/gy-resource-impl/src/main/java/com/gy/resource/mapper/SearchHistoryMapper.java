package com.gy.resource.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.gy.resource.entity.SearchHistoryModel;
import java.util.List;
import java.util.Map;

/**
 * 搜索历史 Mapper 接口
 *
 * @author xuyongliang
 * @since 2020-02-14
 */
@Mapper
public interface SearchHistoryMapper{

    /**
     *  搜索历史 新增
     * @param model
     */
    Integer searchHistoryAdd(SearchHistoryModel model);


    /**
     *  搜索历史 编辑
     * @param record
     * @param example
     */
    Integer searchHistoryEdit(@Param("record") SearchHistoryModel record, @Param("example") SearchHistoryModel example);

    /**
     *  搜索历史 删除
     * @param map
     */
    Integer searchHistoryDelete(Map map);

    /**
     *  搜索历史 查询详情
     * @param map
     */
    SearchHistoryModel searchHistoryQuery(Map map);

    /**
     *  搜索历史 分页查询
     * @param startIndex
     * @param limit
     * @param example
     */
    List<SearchHistoryModel> searchHistoryQueryPageList(
            @Param("startIndex") int startIndex,
            @Param("limit") int limit,
            @Param("example") SearchHistoryModel example);

    /**
     *  搜索历史 分页查询数量
     * @param example
     */
    Integer searchHistoryQueryPageCount(
            @Param("example") SearchHistoryModel example);

    /**
    *  搜索历史 修改单据状态
    * @param map 可修改的参数列表 status 查询参数 id
    */
    Integer searchHistoryChangeStatus(Map map);

    /**
     *  搜索历史 修改审批状态
     * @param map 可修改的参数列表 updator，updateName，updateTime  查询参数 id
     */
    Integer searchHistoryChangeApproveStatus(Map map);
}
