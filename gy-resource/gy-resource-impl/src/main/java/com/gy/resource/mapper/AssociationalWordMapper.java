package com.gy.resource.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.gy.resource.entity.AssociationalWordModel;
import java.util.List;
import java.util.Map;

/**
 * 资源标签联想词 Mapper 接口
 *
 * @author xuyongliang
 * @since 2020-02-14
 */
@Mapper
public interface AssociationalWordMapper{

    /**
     *  资源标签联想词 新增
     * @param model
     */
    Integer associationalWordAdd(AssociationalWordModel model);


    /**
     *  资源标签联想词 编辑
     * @param record
     * @param example
     */
    Integer associationalWordEdit(@Param("record") AssociationalWordModel record, @Param("example") AssociationalWordModel example);

    /**
     *  资源标签联想词 删除
     * @param map
     */
    Integer associationalWordDelete(Map map);

    /**
     *  资源标签联想词 查询详情
     * @param map
     */
    AssociationalWordModel associationalWordQuery(Map map);

    /**
     *  资源标签联想词 分页查询
     * @param startIndex
     * @param limit
     * @param example
     */
    List<AssociationalWordModel> associationalWordQueryPageList(
            @Param("startIndex") int startIndex,
            @Param("limit") int limit,
            @Param("example") AssociationalWordModel example);

    /**
     *  资源标签联想词 分页查询数量
     * @param example
     */
    Integer associationalWordQueryPageCount(
            @Param("example") AssociationalWordModel example);

    /**
     *  资源标签联想词 模糊查询
     * @param startIndex
     * @param limit
     * @param fuzzyWord
     */
    List<AssociationalWordModel> associationalWordFuzzyQuery(
            @Param("startIndex") int startIndex,
            @Param("limit") int limit,
            @Param("fuzzyWord")  String fuzzyWord);

    /**
    *  资源标签联想词 修改单据状态
    * @param map 可修改的参数列表 status 查询参数 id
    */
    Integer associationalWordChangeStatus(Map map);

    /**
     *  资源标签联想词 修改审批状态
     * @param map 可修改的参数列表 updator，updateName，updateTime  查询参数 id
     */
    Integer associationalWordChangeApproveStatus(Map map);
}
