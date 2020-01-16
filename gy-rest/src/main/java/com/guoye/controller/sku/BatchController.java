package com.guoye.controller.sku;

import com.guoye.base.BaseMapper;
import com.guoye.base.BizAction;
import com.guoye.util.BaseResult;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品
 * Created by wj on 2018/8/21.
 */

@SuppressWarnings("all")
@RestController
@RequestMapping("/batch")
public class BatchController extends BizAction {

    @Autowired
    private BaseMapper g4Dao;

    /**
     * 保存波次计划关联发货单
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveBatchOrder")
    public BaseResult saveBatchOrder(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            if (StringUtils.isNotEmpty(dto.getAsString("ids"))) {
                for (String id : dto.getAsString("ids").split(",")) {
                    dto.put("order_id", id);
                    g4Dao.saveInfo("outBatchSale.saveInfo", dto);
                }
            }
            if (StringUtils.isNotEmpty(dto.getAsString("ids"))) {
                for (String id : dto.getAsString("ids").split(",")) {
                    dto.put("id", id);
                    dto.put("status", "726");
                    g4Dao.updateInfo("outBatch.updateStatusInfo", dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 保存波次计划关联发货单
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteInfoByBaAndOrder")
    public BaseResult deleteInfoByBaAndOrder(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            if (StringUtils.isNotEmpty(dto.getAsString("ids"))) {
                for (String id : dto.getAsString("ids").split(",")) {
                    dto.put("order_id", id);
                    g4Dao.delete("outBatchSale.deleteInfoByBaAndOrder", dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 保存波次计划关联发货单
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveBatchAllotInfo")
    @Transactional
    public BaseResult saveBatchAllotInfo(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            if (StringUtils.isNotEmpty(dto.getAsString("data"))) {
                JSONArray jsonArr = JSONArray.fromObject(dto.getAsString("data"));//转换成JSONArray 格式
                List<Dto> goodBeanList = JSONArray.toList(jsonArr, Dto.class);//获得产品数组
                for (Map id : goodBeanList) {
                    //更新库存
                    Dto param = new BaseDto();
                    param.put("addstock", id.get("val"));
                    param.put("sku_id", id.get("key"));
                    g4Dao.updateInfo("orderStoreSku.updateStockInfo", param);
                    //添加波次分配记录
                    Dto saveParam = new BaseDto();
                    saveParam.put("out_stock_num", id.get("val"));
                    saveParam.put("sku_id", id.get("key"));
                    saveParam.put("batch_id", dto.getAsString("id"));
                    saveParam.put("allot_no", new Date().getTime());
                    saveParam.put("sale_order_id", id.get("sale_order_id"));
                    g4Dao.saveInfo("wBatchAllot.saveInfo", saveParam);
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}