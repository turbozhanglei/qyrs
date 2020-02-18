package com.gy.resource.service;
import com.gy.resource.entity.GlobalCorrelationModel;
import com.jic.common.base.vo.Page;
import com.jic.common.base.vo.PageResult;

import java.util.Map;

/**
 *  全局关联信息表 服务类
 *
 * @author xuyongliang
 * @since 2020-02-14
 */

public interface PGlobalCorrelationService {

    /**
     * 全局关联信息表 新增
     * @param modelEntity
     */
    Integer globalCorrelationAdd(GlobalCorrelationModel modelEntity);

    /**
     * 全局关联信息表 编辑
     * @param modifyEntity
     * @param whereCondition
     */
    Integer globalCorrelationEdit(GlobalCorrelationModel modifyEntity, GlobalCorrelationModel whereCondition);

    /**
     * 全局关联信息表 删除
     * @param \
     */
    Integer globalCorrelationDelete(Map map);

    /**
     * 全局关联信息表 查询详情
     * @param
     */
    GlobalCorrelationModel globalCorrelationQuery(Map map);

    /**
     * 全局关联信息表 分页查询
     * @param
     */
    PageResult<GlobalCorrelationModel> globalCorrelationQueryPageList(GlobalCorrelationModel modelEntity, Page pageQuery);

    /**
     * 全局关联信息表 数量
     * @param
     */
    Integer globalCorrelationQueryCount(GlobalCorrelationModel modelEntity);

    /**
    *  全局关联信息表 修改单据状态
    *  @param \
    */
    Integer globalCorrelationChangeStatus(Map map);

    /**
     *  全局关联信息表 修改审批状态
     * @param \
     */
    Integer globalCorrelationChangeApproveStatus(Map map);

    /**
     *  增加记录
     * @param userId    用户id
     * @param refId     资源id
     * @param refType  关联类型，0、关注用户，1、资源浏览数，2、资源分享数，3、资源拨打电话数，4、资讯文章分享数，5、资讯文章点赞数，6、资讯文章浏览数
     * @return
     */
    Boolean addBrowse(Long userId, Long refId, Integer refType);

}