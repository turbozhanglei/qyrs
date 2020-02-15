package com.gy.resource.mapper;

import com.gy.resource.entity.DictionaryCodeModel;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: gaolanyu
 * @date: 2020-02-15
 * @remark:
 */
@Repository
public interface DictionaryMapper {
    @Select("select * from g_dictionary_code where category=#{category} and delete_flag != '1'")
    List<DictionaryCodeModel> queryDictionCodeModel(@Param("category")String category);
}
