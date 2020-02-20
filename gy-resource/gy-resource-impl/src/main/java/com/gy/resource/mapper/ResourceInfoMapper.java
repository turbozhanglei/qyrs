package com.gy.resource.mapper;

import com.gy.resource.entity.ResourceInfo;

import java.lang.Long;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;

/**
 * 资源信息
 * @author : gaoly
 * @email : 774329481@qq.com
 * @since : 2020-02-14 11:08:42
 * @version : v1.0.0
 */
@Repository
public interface  ResourceInfoMapper {

    long insert(ResourceInfo resourceInfo);

    long delete(Long id);

    long update(ResourceInfo resourceInfo);

    ResourceInfo queryByPrimaryKey(Long id);

    List<ResourceInfo> query(ResourceInfo resourceInfo);

    List<ResourceInfo> queryByCondition(@Param("resourceInfo")ResourceInfo resourceInfo,
                                        @Param("releaseTypeList")List<Integer> releaseTypeList,
                                        @Param("resourceLabelList")List<Integer> resourceLabelList,
                                        @Param("resourceAreaList")List<Integer> resourceAreaList,
                                        @Param("resourceTradeList")List<Integer> resourceTradeList,
                                        @Param("startIndex") Integer startIndex,
                                        @Param("limit") Integer limit,
                                        @Param("refType")Integer refType,
                                        @Param("sortType")Integer sortType);
    long queryByConditionCount(@Param("resourceInfo")ResourceInfo resourceInfo,
                               @Param("releaseTypeList")List<Integer> releaseTypeList,
                               @Param("resourceLabelList")List<Integer> resourceLabelList,
                               @Param("resourceAreaList")List<Integer> resourceAreaList,
                               @Param("resourceTradeList")List<Integer> resourceTradeList);

    List<ResourceInfo> queryPageOrderByAuditTime(@Param("startIndex") int startIndex,
                                                 @Param("limit") int limit,
                                                 @Param("resourceInfo") ResourceInfo resourceInfo,
                                                 @Param("createStartTime")Date createStartTime,
                                                 @Param("createEndTime")Date createEndTime);

    long queryPageCount(@Param("resourceInfo") ResourceInfo resourceInfo,
                        @Param("createStartTime")Date createStartTime,
                        @Param("createEndTime")Date createEndTime);

    //TODO 暂时写这 懒得生成了
    @Select("select mobile from g_user where id=#{id} and delete_flag='0'")
    String getMobile(@Param("id")long id);
    //head_pic
    @Select("select head_pic from g_user where id=#{id} and delete_flag='0'")
    String getHeadImage(@Param("id")long id);
    //nickname
    @Select("select nickname from g_user where id=#{id} and delete_flag='0'")
    String getNickName(@Param("id")long id);
    //company_name
    @Select("select company_name from g_user where id=#{id} and delete_flag='0'")
    String getCompanyName(@Param("id")long id);

    @Select("select * from g_resource_info where status='3' and sticky='1' and delete_flag='0' limit 10")
    List<ResourceInfo> queryTopResourceTen();

    @Select("select * from g_resource_info where (status ='3' or status='1') and sticky !='1' and delete_flag='0' order by audit_time desc  limit #{limit}")
    List<ResourceInfo> queryResourceCheckSuccess(@Param("limit")int limit);

    @Update("update g_resource_info set status=#{status},auditor=#{auditor},audit_time=now() where id=#{id} and delete_flag='0'")
    long check(@Param("status")Integer status,@Param("auditor")String auditor,@Param("id")long id);

    @Update({
            "<script> ",
            "update g_resource_info set status=#{status},auditor=#{auditor},audit_time=now() ",
            "<where> ",
            "id in",
            "<foreach item='item' index='index' collection='idList' open='(' separator=',' close=')'>",
            "#{item} ",
            "</foreach> ",
            "and delete_flag='0'",
            "</where>",
            "</script>"
    })
//    @Update("update g_resource_info set status=#{status},auditor=#{auditor},audit_time=now() where id in #{idList} and delete_flag='0'")
    long checkBatch(@Param("status")Integer status,@Param("auditor")String auditor,@Param("idList")List<Long> idList);

    @Update("update g_resource_info set sticky=#{sticky},auditor=#{auditor} where id=#{id} and delete_flag ='0'")
    long top(@Param("sticky")Integer sticky,@Param("auditor")String auditor,@Param("id")long id);

    @Select("select * from g_resource_info where status='0' and delete_flag='0'")
    List<ResourceInfo> querySensitive();

    @Select("select word from g_sensitive_word where delete_flag='0'")
    String querySensitiveOfManager();

    @Update("update g_resource_info set status='2',sensitive_flag='1',content=#{content},audit_time=now(),auditor=#{auditor} where id=#{id} and delete_flag='0'")
    long systemCheckFail(@Param("content")String content,@Param("auditor")String auditor,@Param("id")long id);

    @Select({
            "<script> ",
            "select * from ",
            "(select a.*,ifnull(b.brow_num,0) as brow_num,ifnull(c.share_num,0) as share_num from ",
            "(select * from g_resource_info ",
            "where ",
            "<trim suffixOverrides=\"and\"> ",
            "1=1 and ",
            "<if test=\"resourceInfo.id !=null\">id = #{resourceInfo.id} and </if>  ",
            "<if test=\"resourceInfo.userId !=null\">user_id = #{resourceInfo.userId} and</if> ",
            "<if test=\"resourceInfo.mobile\">mobile = #{resourceInfo.mobile} and</if>  ",
            "<if test=\"resourceInfo.title\"> title like concat(#{resourceInfo.title},'%') and </if> ",
            "<if test=\"resourceInfo.platform !=null\">latform = #{resourceInfo.platform} and </if>   ",
            "<if test=\"resourceInfo.releaseType !=null\">release_type = #{resourceInfo.releaseType} and  </if> ",
            "<if test=\"resourceInfo.resourceLabel !=null\">resource_label = #{resourceInfo.resourceLabel} and </if>  ",
            "<if test=\"resourceInfo.resourceArea !=null\"> resource_area = #{resourceInfo.resourceArea} and </if> ",
            "<if test=\"resourceInfo.resourceTrade !=null\">resource_trade = #{resourceInfo.resourceTrade} and </if>  ",
            "<if test=\"resourceInfo.status !=null\">status = #{resourceInfo.status} and </if> ",
            "<if test=\"resourceInfo.sticky !=null\">sticky = #{resourceInfo.sticky} and </if> ",
            "<if test=\"resourceInfo.sensitiveFlag !=null\">sensitive_flag = #{resourceInfo.sensitiveFlag} and </if> ",
            "<if test=\"resourceInfo.imageFlag !=null\">image_flag = #{resourceInfo.imageFlag} and </if> ",
            "<if test=\"resourceInfo.auditor\"> auditor = #{resourceInfo.auditor} and </if> ",
            "<if test=\"resourceInfo.auditTime\"> audit_time = #{resourceInfo.auditTime} and  </if> ",
            "<if test=\"resourceInfo.createStartTime !=null\">create_time &gt;= #{resourceInfo.createStartTime} and </if> ",
            "<if test=\"resourceInfo.createEndTime !=null\">create_time &lt;= #{resourceInfo.createEndTime} and </if> ",
            "<if test=\"resourceInfo.creator !=null\">creator = #{resourceInfo.creator} and </if> ",
            "<if test=\"resourceInfo.updator !=null\"> updator = #{resourceInfo.updator} and </if>",
            "</trim>",
            "and delete_flag != '1' order by create_time desc) a left join ",
            "(select ref_id,count(id) as brow_num from g_global_correlation where ref_type='1' group by ref_id) b ",
            "on a.id=b.ref_id  left join ",
            "(select ref_id,count(id) as share_num from g_global_correlation where ref_type='2' group by ref_id) c ",
            "on a.id=c.ref_id )d where 1=1 ",
            "<if test=\"resourceInfo.browNumStart!=null and resourceInfo.browNumEnd!=null\">and brow_num between #{resourceInfo.browNumStart} and #{resourceInfo.browNumEnd} </if>",
            "<if test=\"resourceInfo.shareNumStart!=null and resourceInfo.shareNumEnd!=null\">and share_num between #{resourceInfo.shareNumStart} and #{resourceInfo.shareNumEnd} </if>",
            "limit #{startIndex},#{limit} ",
            "</script>"
    })
    List<ResourceInfo> queryResourceInfoListManager(@Param("startIndex") int startIndex,
                                                    @Param("limit") int limit,
                                                    @Param("resourceInfo") ResourceInfo resourceInfo);
    @Select({
            "<script> ",
            "select count(1) from ",
            "(select a.*,ifnull(b.brow_num,0) as brow_num,ifnull(c.share_num,0) as share_num from ",
            "(select * from g_resource_info ",
            "where ",
            "<trim suffixOverrides=\"and\"> ",
            "1=1 and ",
            "<if test=\"resourceInfo.id !=null\">id = #{resourceInfo.id} and </if>  ",
            "<if test=\"resourceInfo.userId !=null\">user_id = #{resourceInfo.userId} and</if> ",
            "<if test=\"resourceInfo.mobile\">mobile = #{resourceInfo.mobile} and</if>  ",
            "<if test=\"resourceInfo.title\"> title like concat(#{resourceInfo.title},'%') and </if> ",
            "<if test=\"resourceInfo.platform !=null\">latform = #{resourceInfo.platform} and </if>   ",
            "<if test=\"resourceInfo.releaseType !=null\">release_type = #{resourceInfo.releaseType} and  </if> ",
            "<if test=\"resourceInfo.resourceLabel !=null\">resource_label = #{resourceInfo.resourceLabel} and </if>  ",
            "<if test=\"resourceInfo.resourceArea !=null\"> resource_area = #{resourceInfo.resourceArea} and </if> ",
            "<if test=\"resourceInfo.resourceTrade !=null\">resource_trade = #{resourceInfo.resourceTrade} and </if>  ",
            "<if test=\"resourceInfo.status !=null\">status = #{resourceInfo.status} and </if> ",
            "<if test=\"resourceInfo.sticky !=null\">sticky = #{resourceInfo.sticky} and </if> ",
            "<if test=\"resourceInfo.sensitiveFlag !=null\">sensitive_flag = #{resourceInfo.sensitiveFlag} and </if> ",
            "<if test=\"resourceInfo.imageFlag !=null\">image_flag = #{resourceInfo.imageFlag} and </if> ",
            "<if test=\"resourceInfo.auditor\"> auditor = #{resourceInfo.auditor} and </if> ",
            "<if test=\"resourceInfo.auditTime\"> audit_time = #{resourceInfo.auditTime} and  </if> ",
            "<if test=\"resourceInfo.createStartTime !=null\">create_time &gt;= #{resourceInfo.createStartTime} and </if> ",
            "<if test=\"resourceInfo.createEndTime !=null\">create_time &lt;= #{resourceInfo.createEndTime} and </if> ",
            "<if test=\"resourceInfo.creator !=null\">creator = #{resourceInfo.creator} and </if> ",
            "<if test=\"resourceInfo.updator !=null\"> updator = #{resourceInfo.updator} and </if>",
            "</trim>",
            "and delete_flag != '1' order by create_time desc) a left join ",
            "(select ref_id,count(id) as brow_num from g_global_correlation where ref_type='1' group by ref_id) b ",
            "on a.id=b.ref_id  left join ",
            "(select ref_id,count(id) as share_num from g_global_correlation where ref_type='2' group by ref_id) c ",
            "on a.id=c.ref_id )d where 1=1 ",
            "<if test=\"resourceInfo.browNumStart!=null and resourceInfo.browNumEnd!=null\">and brow_num between #{resourceInfo.browNumStart} and #{resourceInfo.browNumEnd} </if> ",
            "<if test=\"resourceInfo.shareNumStart!=null and resourceInfo.shareNumEnd!=null\">and share_num between #{resourceInfo.shareNumStart} and #{resourceInfo.shareNumEnd} </if> ",
            "</script>"
    })
    int queryResourceInfoListManagerCount(@Param("resourceInfo") ResourceInfo resourceInfo);
}