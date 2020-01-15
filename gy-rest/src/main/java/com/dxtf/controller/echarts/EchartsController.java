package com.dxtf.controller.echarts;

import com.dxtf.base.BizAction;
import com.dxtf.util.BaseResult;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/echarts")
public class EchartsController extends BizAction {


    /**
     * 统计产品库存
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/countStoreSku")
    public BaseResult countStoreSku(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            List<Dto> list = bizService.queryList("echarts.queryCountStoreSku", dto);
            List<String> resultData = new ArrayList<String>();
            List<Integer> seriesData = new ArrayList<Integer>();
            list.forEach(element -> {
                resultData.add(element.getAsString("name"));
                seriesData.add(element.getAsInteger("total"));
            });

            Map<String, Object> resultMap = new HashMap<String, Object>();

            resultMap.put("series", seriesData);
            resultMap.put("xAxis", resultData);

            result.setData(resultMap);

        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 入库产品统计
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/countOrderInStore")
    public BaseResult countOrderInStore(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            List<Dto> list = bizService.queryList("echarts.queryCountOrderInStore", dto);
            List<String> resultData = new ArrayList<String>();
            List<Integer> seriesData = new ArrayList<Integer>();
            list.forEach(element -> {
                resultData.add(element.getAsString("name"));
                seriesData.add(element.getAsInteger("total"));
            });

            Map<String, Object> resultMap = new HashMap<String, Object>();

            resultMap.put("series", seriesData);
            resultMap.put("xAxis", resultData);

            result.setData(resultMap);

        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 出库产品统计
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/countOrderOutStore")
    public BaseResult countOrderOutStore(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            List<Dto> resuktList = bizService.queryList("echarts.queryCountOrderOutStore", dto);
            List<String> resultData = new ArrayList<String>();
            List<Integer> seriesData = new ArrayList<Integer>();
            resuktList.forEach(element -> {
                resultData.add(element.getAsString("name"));
                seriesData.add(element.getAsInteger("total"));
            });

            Map<String, Object> resultMap = new HashMap<String, Object>();

            resultMap.put("series", seriesData);
            resultMap.put("xAxis", resultData);

            result.setData(resultMap);

        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 出库产品统计
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/commonCount")
    public BaseResult commonCount(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            String t = dto.getAsString("t");
            String sql = dto.getAsString("sql");
            List<Dto> resuktList = bizService.queryList(t + "." + sql, dto);
            List<String> resultData = new ArrayList<String>();
            List<Integer> seriesData = new ArrayList<Integer>();
            resuktList.forEach(element -> {
                resultData.add(element.getAsString("name"));
                seriesData.add(element.getAsInteger("total"));
            });

            for (int i = resuktList.size(); i < 10; i++) {
                resultData.add("");
                seriesData.add(0);
            }

            Map<String, Object> resultMap = new HashMap<String, Object>();

            resultMap.put("series", seriesData);
            resultMap.put("xAxis", resultData);

            result.setData(resultMap);

        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

}
