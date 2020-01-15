package com.dxtf.controller.sku;

import com.dxtf.util.BaseResult;
import com.dxtf.base.BizAction;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: WuMengChang
 * @Date: 2018/9/21 16:51
 * @Description: 产品分类
 */

@SuppressWarnings("all")
@RestController
@RequestMapping("/skuType")
public class SkuTypeController extends BizAction {
    /**
     * 获取当前店铺所有商品分类
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryProductType")
    public BaseResult queryProductType(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            //根据所属店铺查询商品类别
            if (StringUtils.isEmpty(dto.getAsString("shopid"))) {
                Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
                dto.put("shopid", member.getAsString("shopid"));
            }
            // 查询根目录下所有部门
            dto.put("parentid", dto.getAsString("id"));
            List<Dto> productTypes = (List<Dto>) bizService.queryForList("productType.queryList", dto);
            List<Dto> list = new ArrayList<Dto>();
            productTypes.addAll(querySonProductType(list, productTypes, dto));
            result.setData(productTypes);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    public List<Dto> querySonProductType(List<Dto> list, List<Dto> productTypes, Dto dto) {
        for (Dto productType : productTypes) {
            dto.put("parentid", productType.getAsString("id"));
            List<Dto> nodes = (List<Dto>) bizService.queryForList("productType.queryList", dto);
            if (nodes.size() != 0) {
                list.addAll(nodes);
                querySonProductType(list, nodes, dto);
            }
        }
        return list;
    }

}
