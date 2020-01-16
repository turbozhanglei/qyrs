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

@SuppressWarnings("all")
@RestController
@RequestMapping("/sku")
public class PickingController extends BizAction {
    @Autowired
    private BaseMapper g4Dao;

    @ResponseBody
    @RequestMapping(value = "/updatePicking")
    public BaseResult updatePicking(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            if (StringUtils.isNotEmpty(dto.getAsString("picking_id"))) {
                dto.put("id", dto.getAsString("picking_id"));
                dto.put("status", "727");
                g4Dao.updateInfo("picking.updatePickingInfo", dto);
                Dto param = new BaseDto();
                param.put("status", "727");
                param.put("id", dto.getAsString("allot_id"));
                g4Dao.updateInfo("picking.updateSaleByPickingId", param);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/updatePackage")
    public BaseResult updatePackage(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            if (StringUtils.isNotEmpty(dto.getAsString("picking_id"))) {
                dto.put("id", dto.getAsString("picking_id"));
                dto.put("status", "729");
                g4Dao.updateInfo("package.updateInfo", dto);
                JSONArray jsonArr = JSONArray.fromObject(dto.getAsString("data"));//转换成JSONArray 格式
                List<Dto> goodBeanList = JSONArray.toList(jsonArr, Dto.class);//获得产品数组
                for (Map id : goodBeanList) {
                    Dto param = new BaseDto();
                    param.put("status", "729");
                    param.put("id", id.get("picking_id"));
                    g4Dao.updateInfo("package.updateSaleByPackageId", param);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/updatePickStatus")
    public BaseResult updatePickStatus(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            if (StringUtils.isNotEmpty(dto.getAsString("id"))) {
                dto.put("id", dto.getAsString("id"));
                dto.put("status", "728");
                g4Dao.updateInfo("picking.updatePickingInfo", dto);
                Dto param = new BaseDto();
                param.put("status", "728");
                param.put("id", dto.getAsString("id"));
                g4Dao.updateInfo("picking.updatePickStatus", param);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/updatePackStatus")
    @Transactional
    public BaseResult updatePackStatus(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            if (StringUtils.isNotEmpty(dto.getAsString("id"))) {
                dto.put("id", dto.getAsString("id"));
                dto.put("status", "730");
                g4Dao.updateInfo("package.updatePackingInfo", dto);
                Dto param = new BaseDto();
                param.put("status", "730");
                param.put("id", dto.getAsString("id"));
                g4Dao.updateInfo("package.updatePackStatus", param);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}
