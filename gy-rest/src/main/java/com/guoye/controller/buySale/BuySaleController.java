package com.guoye.controller.buySale;

import com.guoye.base.BizAction;
import com.guoye.util.BaseResult;
import com.guoye.util.JSONUtil;
import com.guoye.util.StatusConstant;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.util.G4Constants;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;


@RestController
@RequestMapping("/buySale")
public class BuySaleController extends BizAction {

    /*确认分配处理
   */
    @ResponseBody
    @RequestMapping(value = "/confirmReward")

    public BaseResult confirmReward(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        Dto storageAssign = new BaseDto();
        String ids[] = dto.getAsString("ids").split(",");
        storageAssign.put("tableName", "storageAssign");
        storageAssign.put("method", "saveStorageAssign");
        for (int i = 0; i < ids.length; i++) {
            storageAssign.put("order_id", ids[i]);
            storageAssign.put("user_id", dto.getAsString("user_id"));
            bizService.save(storageAssign);
        }

        Dto upStatus = new BaseDto();
        upStatus.put("tableName", "storageAssign");
        upStatus.put("method", "updateStatusInfo");
        for (int j = 0; j < ids.length; j++) {
            upStatus.put("status", "717");
            upStatus.put("id", ids[j]);
            bizService.update(upStatus);
        }


        return result;
    }

    /**
     * 查询退货单订单中的产品信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryReturnOrderBuySku")
    public BaseResult queryReturnOrderBuySku(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }

            String id = dto.getAsString("id");

            if (StringUtils.isNotEmpty(id)) {
                //查询订单信息
                List<Dto> list = bizService.queryList("returnGoods.getInfo", new BaseDto("id", id));
                if (!list.isEmpty()) {
                    Iterator<Dto> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Dto next = iterator.next();
                        //查询司机信息
                        List driverList = bizService.queryList("returnGoods.queryReturnOrderBuySku", new BaseDto("return_goods_id", next.getAsString("id")));
                        next.put("proData", driverList);
                    }

                }

                result.setData(JSONUtil.formatDateList(list, G4Constants.FORMAT_DateTime));
            }


        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
