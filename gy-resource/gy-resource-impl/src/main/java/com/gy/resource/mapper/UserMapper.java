package com.gy.resource.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author: gaolanyu
 * @date: 2020-02-27
 * @remark:
 */
@Repository
public interface UserMapper {
    @Update("update g_user set phone_switch=#{phoneSwitch} where id=#{userId} and delete_flag='0'")
    int setPhoneSwitch(@Param("phoneSwitch")String phoneSwitch,@Param("userId")long userId);

    @Select("select phone_switch from g_user where id=#{userId}")
    String queryPhoneSwitch(@Param("userId")long userId);
}
