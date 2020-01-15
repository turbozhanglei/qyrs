package com.dxtf.controller.sku;

import com.dxtf.base.BaseMapper;
import com.dxtf.base.BizAction;
import com.dxtf.service.ProductService;
import com.dxtf.util.BaseResult;
import com.dxtf.util.JSONUtil;
import com.dxtf.util.NumLengthUtil;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.util.G4Constants;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品
 * Created by wj on 2018/8/21.
 */

@SuppressWarnings("all")
@RestController
@RequestMapping("/sku")
public class SkuController extends BizAction {

    @Autowired
    private BaseMapper g4Dao;

    @Autowired
    ProductService productService;

    /**
     * 获取所有商品树状数据结构
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllProductType")
    public BaseResult queryAllProductType(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            //根据所属店铺查询商品类别
            if (StringUtils.isEmpty(dto.getAsString("shopid"))) {
                Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
                dto.put("shopid", member.getAsString("shopid"));
            }
            // 查询根目录下所有部门
            dto.put("parent_id", 0);
            List<Dto> productTypes = (List<Dto>) bizService.queryForList("productType.queryList", dto);
            for (Dto productType : productTypes) {
                dto.put("parent_id", productType.get("id"));
                List<Dto> nodes = (List<Dto>) bizService.queryForList("productType.queryList", dto);
                for (Dto node : nodes) {
                    dto.put("parent_id", node.get("id"));
                    List<Dto> dnodes = (List<Dto>) bizService.queryForList("productType.queryList", dto);
                    node.put("nodes", dnodes);
                }
                productType.put("nodes", nodes);
            }
            result.setData(productTypes);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 保存信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/editProductTypeInfo")
    public BaseResult editProductTypeInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            inDto.put("parent_id", inDto.getAsString("parent_id").split("-")[0]);
            inDto.put("type_id", inDto.getAsString("type_id").split("-")[0]);
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (inDto.getAsLong("id") != null) {//修改
                inDto.put("updator", member == null ? "" : member.get("id"));
                bizService.updateInfo(inDto);
            } else {//新增
                inDto.put("creator", member == null ? "" : member.get("id"));
                bizService.saveInfo(inDto);
            }
            result.setData(inDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 保存信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveBath")
    public BaseResult saveBath(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            //查询商铺
            if (inDto.getAsInteger("shopId") == 1) {
                return null;
            }
            List<Dto> list = bizService.queryForList(inDto.getAsString("t") + ".queryList", new BaseDto("shopid", 1));
            list.forEach(shopList -> {
                shopList.put("shopid", inDto.getAsString("shopId"));
            });
            g4Dao.saveInfo(inDto.getAsString("t") + ".saveBatch", list);
            result.setData(inDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * * 删除信息
     */
    @ResponseBody
    @RequestMapping(value = "/deleteProductTypeInfo")
    public BaseResult deleteProductTypeInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            int flag = productService.deleteInfo(inDto);
            if (flag == -1) {
                return returnMsg("9999", "已有商品关联该类别，删除失败！");
            } else if (flag == -2) {
                return returnMsg("9999", "已有商品关联该属性，删除失败！");
            } else if (flag == -3) {
                return returnMsg("9999", "已有商品关联该品牌，删除失败！");
            }
            result.setData(new BaseDto("msg", "数据操作成功"));
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 查询列表
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryList")
    public BaseResult queryList(HttpServletRequest request, HttpServletResponse response) {
        String t = request.getParameter("t");
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            //根据所属店铺查询商品类别
            if (StringUtils.isEmpty(dto.getAsString("shopid")) && !"1".equals(member.getAsString("id"))) {
                dto.put("shopid", member.getAsString("shopid"));
            }
            dto.put("userid", member == null ? "" : member.get("id"));
            List paramList = bizService.queryForList(dto.getAsString("t") + ".queryList", dto);
            result.setData(JSONUtil.formatDateList(paramList, G4Constants.FORMAT_DateTime));
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 查询商品属性
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryAttributesList")
    public BaseResult queryAttributesList(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Map<String, List<Dto>> returnMap = new HashMap<>();
            List<Dto> paramList = bizService.queryForList("productPropertyShop.queryList", dto);
            paramList.forEach(proty -> {
                if (returnMap.get(proty.getAsString("propertname")) == null) {
                    List<Dto> addDto = new ArrayList<Dto>();
                    addDto.add(proty);
                    returnMap.put(proty.getAsString("propertname"), addDto);
                } else {
                    returnMap.get(proty.getAsString("propertname")).add(proty);
                    returnMap.put(proty.getAsString("propertname"), returnMap.get(proty.getAsString("propertname")));
                }
            });
            result.setData(returnMap);
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/queryProInfo")
    public BaseResult queryProInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto info = (BaseDto) bizService.queryForDto("product.getInfo", dto);
            result.setData(JSONUtil.formatDateObject(info, G4Constants.FORMAT_DateTime));
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/queryProPerIdInfo")
    public BaseResult queryProPerIdInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            List<Dto> list = bizService.queryForList("productPropertyShop.queryList", dto);
            result.setData(JSONUtil.formatDateList(list, G4Constants.FORMAT_DateTime));
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 待收货明细选择
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryAsnReceGoodsLists")
    public BaseResult queryAsnReceGoodsLists(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        Dto resultData = new BaseDto();
        List<Dto> listThree = new ArrayList<>();
        List<Dto> listTwo = new ArrayList<>();
        try {
            List<Dto> list = bizService.queryForList("wOrderAsnSku.queryNoGoodOwnerLists", dto);
            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Dto dtoOne = list.get(i);
                    listTwo = bizService.queryForList("wOrderAsnSku.queryNoGoodOwnerTwoLists", new BaseDto("provider_owner_id", dtoOne.get("id")));
                    if (null != listTwo && listTwo.size() > 0) {
                        for (int j = 0; j < listTwo.size(); j++) {
                            Dto dtoThree = new BaseDto();
                            if (listTwo.get(j).getAsString("id").length() > 1) {
                                dtoThree.put("id", NumLengthUtil.getString(("0" + (i + 1) + listTwo.get(j).getAsString("id") + "0" + (j + 1)), 6));
                                listTwo.get(j).put("id", NumLengthUtil.getString(("0" + (i + 1) + listTwo.get(j).getAsString("id")), 6));
                            } else {
                                dtoThree.put("id", NumLengthUtil.getString(("0" + (i + 1) + "0" + listTwo.get(j).getAsString("id") + "0" + (j + 1)), 6));
                                listTwo.get(j).put("id", NumLengthUtil.getString(("0" + (i + 1) + "0" + listTwo.get(j).getAsString("id")), 6));
                            }
                            dtoThree.put("name", listTwo.get(j).getAsString("num"));
                            listThree.add(dtoThree);
                        }
                    }
                    if (dtoOne.getAsString("id").length() > 1) {
                        dtoOne.put("id", (i + 1));
                    } else {
                        dtoOne.put("id", ("0" + (i + 1)));
                    }


                    dtoOne.put("id", NumLengthUtil.getString(dtoOne.getAsString("id"), 6));
                }
            }
            resultData.put("province_list", list);
            resultData.put("city_list", listTwo);
            resultData.put("county_list", listThree);
            result.setData(resultData);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}