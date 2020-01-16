package com.guoye.service;

import org.g4studio.core.metatype.Dto;

/**
 * @Auther: wj
 * @Date: 2018/8/22 12:04
 * @Description: 产品删除
 */
public interface ProductService {
    /**
     * 删除产品相关
     * @param dto
     */
    int deleteInfo(Dto dto);
}
