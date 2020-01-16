package com.guoye.controller.order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.guoye.util.BaseResult;
import com.guoye.base.BizAction;
import com.guoye.util.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.util.G4Constants;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 订货单业务处理
 *
 * @author Wang Junwei
 * @see
 * @since 22点21分
 */
@RestController
@RequestMapping("/order")
public class OrderManageController extends BizAction {
    /**
     * 添加订货单信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addOrder")
    public BaseResult contractManage(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (member != null) {
                if (inDto.getAsLong("id") != null) {
                    inDto.put("updator", member == null ? "" : member.get("id"));
                    bizService.updateInfo(inDto);
                } else {
                    // 创建时间戳
                    long millis = System.currentTimeMillis();
                    // 得到redis中的编号
                    String value = redisService.getValue("order_no");
                    if (StringUtils.isNotEmpty(value)) {
                        int parseInt = Integer.parseInt(value);
                        // 编号加1重新赋值
                        redisService.setValue("order_no", String.valueOf(parseInt + 1));
                        inDto.put("order_no", millis + String.format("%0" + 5 + "d", parseInt + 1));
                    } else {
                        redisService.setValue("number", "1");
                        inDto.put("order_no", millis + String.format("%0" + 5 + "d", 1));
                    }
                    inDto.put("creator", member == null ? "" : member.get("id"));
                    inDto.put("updator", member == null ? "" : member.get("id"));
                    bizService.saveInfo(inDto);
                }
            } else {
                result.setCode("9999");
                result.setMsg("服务异常,请稍后重试！");
                return result;
            }
            result.setData(inDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 添加订货单商品信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addOrderSku")
    public BaseResult addOrderSku(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (member != null) {
                if (inDto.getAsLong("id") != null) {
                    inDto.put("updator", member == null ? "" : member.get("id"));
                    bizService.update(inDto);
                } else {
                    // 保存订单的商品信息
                    inDto.put("creator", member == null ? "" : member.get("id"));
                    inDto.put("updator", member == null ? "" : member.get("id"));
                    bizService.save(inDto);
                    // 订单商品资金合计
                    Dto info = (BaseDto) bizService.queryForDto(inDto.getAsString("t") + ".sumOrderSkuInfo", inDto);
                    inDto.put("amount", info.getAsString("amount"));
                    inDto.put("order_id", info.getAsString("order_id"));
                    // 对订单表进行更新
                    bizService.updateInfo(inDto);
                    inDto.clear();
                    inDto.put("order_id", info.getAsString("order_id"));
                    result.setData(inDto);
                }
            } else {
                result.setCode("9999");
                result.setMsg("服务异常,请稍后重试！");
                return result;
            }
            result.setData(inDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 添加补货单商品信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/replenishmentSku")
    public BaseResult replenishmentSku(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (member != null) {
                // 保存补单的商品信息
                inDto.put("creator", member == null ? "" : member.get("id"));
                inDto.put("updator", member == null ? "" : member.get("id"));
                bizService.save(inDto);
            } else {
                result.setCode("9999");
                result.setMsg("服务异常,请稍后重试！");
                return result;
            }
            result.setData(inDto);
        } catch (

                Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 入库质检
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveQualityInfo")
    public BaseResult saveQualityInfo(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (member != null) {
                // 保存补单的商品信息
                inDto.put("creator", member == null ? "" : member.get("id"));
                inDto.put("updator", member == null ? "" : member.get("id"));
                inDto.put("method", inDto.getAsString("sql"));
                bizService.save(inDto);
                //修改订单状态
                Dto cdto = new BaseDto();
                cdto.put("id", inDto.getAsString("order_id"));
                cdto.put("status", 716);
                cdto.put("tableName", "orderBuy");
                cdto.put("method", "updateInfo");

                bizService.update(cdto);
            } else {
                result.setCode("9999");
                result.setMsg("服务异常,请稍后重试！");
                return result;
            }
            result.setData(inDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 质检员接收
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveReceiveInfo")
    public BaseResult saveReceiveInfo(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (member != null) {
                if (inDto.getAsString("ispass").equals("718")) {
                    //修改订单状态
                    Dto cdto = new BaseDto();
                    cdto.put("status", 718);
                    cdto.put("tableName", "orderBuy");
                    cdto.put("method", "updateInfo");
                    String[] ids = inDto.getAsString("ids").split(",");
                    for (int i = 0; i < ids.length; i++) {
                        cdto.put("id", ids[i]);
                        bizService.update(cdto);
                    }
                } else if (inDto.getAsString("ispass").equals("719")) {
                    //修改订单状态
                    Dto cdto = new BaseDto();
                    cdto.put("status", 717);
                    cdto.put("tableName", "orderBuy");
                    cdto.put("method", "updateInfo");
                    String[] ids = inDto.getAsString("ids").split(",");
                    for (int i = 0; i < ids.length; i++) {
                        cdto.put("id", ids[i]);
                        bizService.update(cdto);
                    }
                }


            } else {
                result.setCode("9999");
                result.setMsg("服务异常,请稍后重试！");
                return result;
            }
            result.setData(inDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 上架
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveShelfInfo")
    public BaseResult saveShelfInfo(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (member != null) {
                //修改订单状态
                Dto cdto = new BaseDto();
                cdto.put("status", 720);
                cdto.put("tableName", "orderBuy");
                cdto.put("method", "updateInfo");
                String[] ids = inDto.getAsString("ids").split(",");
                for (int i = 0; i < ids.length; i++) {
                    cdto.put("id", ids[i]);
                    bizService.update(cdto);
                }

            } else {
                result.setCode("9999");
                result.setMsg("服务异常,请稍后重试！");
                return result;
            }
            result.setData(inDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 分页
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryForReceive")
    public Dto queryPage(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        Dto retDto = new BaseDto();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            dto.put("userid", member == null ? "" : member.get("id"));
            List orderList = bizService.queryForList("orderBuy.queryListForUser", new BaseDto("user_id", member.getAsString("id")));

            List<Dto> paramList = bizService.queryForPage(dto.getAsString("t") + ".queryList", dto);
            System.out.println("分页查询：   " + paramList.size());
            retDto.put("rows", JSONUtil.formatDateList(paramList, G4Constants.FORMAT_DateTime));
            retDto.put("total", bizService.queryForList(dto.getAsString("t") + ".queryList", dto).size());
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return retDto;
    }
}
