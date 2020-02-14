package com.gy.resource.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.gy.resource.entity.GlobalCorrelationModel;
import java.util.List;
import java.util.Map;

/**
 * 全局关联信息表 Mapper 接口
 *
 * @author xuyongliang
 * @since 2020-02-14
 */
@Mapper
public interface GlobalCorrelationMapper{

    /**
     *  全局关联信息表 新增
     * @param model
     */
    Integer globalCorrelationAdd(GlobalCorrelationModel model);


    /**
     *  全局关联信息表 编辑
     * @param record
     * @param example
     */
    Integer globalCorrelationEdit(@Param("record") GlobalCorrelationModel record, @Param("example") GlobalCorrelationModel example);

    /**
     *  全局关联信息表 删除
     * @param map
     */
    Integer globalCorrelationDelete(Map map);

    /**
     *  全局关联信息表 查询详情
     * @param map
     */
    GlobalCorrelationModel globalCorrelationQuery(Map map);

    /**
     *  全局关联信息表 分页查询
     * @param startIndex
     * @param limit
     * @param example
     */
    List<GlobalCorrelationModel> globalCorrelationQueryPageList(
            @Param("startIndex") int startIndex,
            @Param("limit") int limit,
            @Param("example") GlobalCorrelationModel example);

    /**
     *  全局关联信息表 分页查询数量
     * @param example
     */
    Integer globalCorrelationQueryPageCount(
            @Param("example") GlobalCorrelationModel example);

    /**
    *  全局关联信息表 修改单据状态
    * @param map 可修改的参数列表 status 查询参数 id
    */
    Integer globalCorrelationChangeStatus(Map map);

    /**
     *  全局关联信息表 修改审批状态
     * @param map 可修改的参数列表 updator，updateName，updateTime  查询参数 id
     */
    Integer globalCorrelationChangeApproveStatus(Map map);
}
