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
import java.util.List;
import java.util.Map;

/**
 * 商品
 * Created by wj on 2018/8/21.
 */

@SuppressWarnings("all")
@RestController
@RequestMapping("/waybill")
public class orderWaybillController extends BizAction {

    @Autowired
    private BaseMapper g4Dao;

    /**
     * 保存波次计划关联发货单
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveWaybillPackage")
    @Transactional
    public BaseResult saveWaybillPackage(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            if (StringUtils.isNotEmpty(dto.getAsString("data"))) {
                JSONArray jsonArr = JSONArray.fromObject(dto.getAsString("data"));//转换成JSONArray 格式
                List<Dto> goodBeanList = JSONArray.toList(jsonArr, Dto.class);//获得产品数组
                for (Map id : goodBeanList) {
                    //更新库存
                    Dto param = new BaseDto();
                    param.put("id", id.get("id"));
                    param.put("status", "1");
                    g4Dao.updateInfo("package.updateInfo", param);
                    //添加波次分配记录
                    Dto saveParam = new BaseDto();
                    saveParam.put("business_id", id.get("id"));
                    saveParam.put("waybill_id", dto.get("waybill_id"));
                    g4Dao.saveInfo("orderWaybillSku.saveInfo", saveParam);
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}