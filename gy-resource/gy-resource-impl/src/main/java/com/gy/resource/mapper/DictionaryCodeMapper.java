package com.gy.resource.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.gy.resource.entity.DictionaryCodeModel;
import java.util.List;
import java.util.Map;

/**
 * 字典表 Mapper 接口
 *
 * @author xuyongliang
 * @since 2020-02-14
 */
@Mapper
public interface DictionaryCodeMapper{

    /**
     *  字典表 新增
     * @param model
     */
    Integer dictionaryCodeAdd(DictionaryCodeModel model);


    /**
     *  字典表 编辑
     * @param record
     * @param example
     */
    Integer dictionaryCodeEdit(@Param("record") DictionaryCodeModel record, @Param("example") DictionaryCodeModel example);

    /**
     *  字典表 删除
     * @param map
     */
    Integer dictionaryCodeDelete(Map map);

    /**
     *  字典表 查询详情
     * @param map
     */
    DictionaryCodeModel dictionaryCodeQuery(Map map);

    /**
     *  字典表 分页查询
     * @param startIndex
     * @param limit
     * @param example
     */
    List<DictionaryCodeModel> dictionaryCodeQueryPageList(
            @Param("startIndex") int startIndex,
            @Param("limit") int limit,
            @Param("example") DictionaryCodeModel example);

    /**
     *  字典表 分页查询数量
     * @param example
     */
    Integer dictionaryCodeQueryPageCount(
            @Param("example") DictionaryCodeModel example);

    /**
    *  字典表 修改单据状态
    * @param map 可修改的参数列表 status 查询参数 id
    */
    Integer dictionaryCodeChangeStatus(Map map);

    /**
     *  字典表 修改审批状态
     * @param map 可修改的参数列表 updator，updateName，updateTime  查询参数 id
     */
    Integer dictionaryCodeChangeApproveStatus(Map map);
}
