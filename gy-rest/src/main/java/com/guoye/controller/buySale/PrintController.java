package com.guoye.controller.buySale;

import com.guoye.util.BaseResult;
import com.guoye.util.CommonUtil;
import com.guoye.util.JSONUtil;
import com.guoye.base.BizAction;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/doPrint")
public class PrintController extends BizAction {

    /**
     * 根据入库单号打印货物信息
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/qualityProductInfoPrint")
    public BaseResult qualityProductInfoPrint(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();

        String sql = dto.getAsString("sql");
        if (!StringUtils.isNotBlank(sql)) {
            sql = "queryList";
        }
        List paramList = bizService.queryForList(dto.getAsString("t") + "." + sql, dto);

        Map map = dealWithPrintParam(paramList);

        String number = dto.getAsString("number");
        if (StringUtils.isEmpty(number)) {
            number = "tray_number";
        }


        List printParams = createPrintParams(map, number, dto.getAsString("bodyKey"), dto.getAsString("rowsKey"));
        result.setData(printParams);
        return result;
    }


    /**
     * 设备打印
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/facilityPrint")
    public BaseResult facilityPrint(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();

        String sql = dto.getAsString("sql");
        if (!StringUtils.isNotBlank(sql)) {
            sql = "queryList";
        }
        List paramList = bizService.queryForList(dto.getAsString("t") + "." + sql, dto);


        String number = dto.getAsString("number");
        if (StringUtils.isEmpty(number)) {
            number = "tray_number";
        }


        List printParams = createPrintParams(paramList, number, dto.getAsString("bodyKey"), dto.getAsString("rowsKey"));
        result.setData(printParams);
        return result;
    }

    /**
     * 根据入库单号打印货物信息
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/qualityProductInfoPrintDetail")
    public BaseResult qualityProductInfoPrintDetail(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();

        String sql = dto.getAsString("sql");
        if (!StringUtils.isNotBlank(sql)) {
            sql = "queryList";
        }
        List paramList = bizService.queryForList(dto.getAsString("t") + "." + sql, dto);

        Map map = dealWithPrintParam(paramList);
        List printParams = createPrintParams(map, "batch_number", dto.getAsString("bodyKey"), dto.getAsString("rowsKey"));
        result.setData(printParams);
        return result;
    }


    /**
     * 处理打印数据
     *
     * @param paramList
     * @return
     */
    private List<Map<String, Object>> createPrintParams(Map<String, List<Dto>> paramList, String numberKey, String bodyTemp, String rowsTemp) {
        List<Map<String, Object>> resultList = new LinkedList<>();

        if (paramList != null && paramList.size() > 0) {

            if (StringUtils.isEmpty(bodyTemp)) {
                bodyTemp = "PRINT_BODY_TEMPLETE";
            }
            if (StringUtils.isEmpty(rowsTemp)) {
                rowsTemp = "PRINT_ROW_TEMPLETE";
            }

            String printBodyTemplete = CommonUtil.getSysConfig().getAsString(bodyTemp);
            String printRowTemplete = CommonUtil.getSysConfig().getAsString(rowsTemp);
            if (StringUtils.isNotEmpty(printBodyTemplete) && StringUtils.isNotEmpty(printRowTemplete)) {

                Iterator<Map.Entry<String, List<Dto>>> iterator = paramList.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, List<Dto>> next = iterator.next();
                    String key = next.getKey();

                    List<Dto> infoList = next.getValue();

                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("title", "入库");

                    List<Map<String, Object>> bodyTemplete = JSONUtil.parseJSON2List(printBodyTemplete);
                    Iterator<Map<String, Object>> body = bodyTemplete.iterator();
                    while (body.hasNext()) {
                        Map<String, Object> bodyNext = body.next();
                        String bodyKey = (String) bodyNext.get("key");
                        if (StringUtils.isEmpty(infoList.get(0).getAsString(bodyKey))) {
                            body.remove();
                        } else {
                            bodyNext.put("value", infoList.get(0).getAsString(bodyKey));
                            bodyNext.put("key", bodyNext.get("lable"));
                            bodyNext.remove("lable");
                            bodyNext.remove("sort");
                        }
                    }
                    resultMap.put("body", bodyTemplete);
                    resultList.add(resultMap);


                    List<List<Map<String, Object>>> rows = new LinkedList<>();
                    for (Dto dto : infoList) {
                        List<Map<String, Object>> rowTemplete = JSONUtil.parseJSON2List(printRowTemplete);
                        Iterator<Map<String, Object>> row = rowTemplete.iterator();
                        while (row.hasNext()) {
                            Map<String, Object> rowNext = row.next();
                            String rowKey = (String) rowNext.get("key");

                            if (StringUtils.isEmpty(dto.getAsString(rowKey))) {
                                row.remove();
                            } else {
                                rowNext.put("value", dto.getAsString(rowKey));
                                rowNext.put("key", rowNext.get("lable"));
                                rowNext.remove("lable");
                                rowNext.remove("sort");
                            }

                        }
                        rows.add(rowTemplete);

                    }
                    resultMap.put("rows", rows);

                    if (StringUtils.isNotEmpty(numberKey)) {
                        List<String> numberList = new LinkedList<>();
                        for (String name : numberKey.split("@")) {
                            if (StringUtils.isNotEmpty(name)) {
                                numberList.add(infoList.get(0).getAsString(name));
                            }
                        }
                        resultMap.put("number", String.join(",", numberList));
                    }

                }
            }


        }
        return resultList;
    }


    /**
     * 处理打印数据
     *
     * @param paramList
     * @return
     */
    private List<Map<String, Object>> createPrintParams(List<Dto> paramList, String numberKey, String bodyTemp, String rowsTemp) {
        List<Map<String, Object>> resultList = new LinkedList<>();

        if (paramList != null && paramList.size() > 0) {

            if (StringUtils.isEmpty(bodyTemp)) {
                bodyTemp = "PRINT_BODY_TEMPLETE";
            }
            if (StringUtils.isEmpty(rowsTemp)) {
                rowsTemp = "PRINT_ROW_TEMPLETE";
            }

            String printBodyTemplete = CommonUtil.getSysConfig().getAsString(bodyTemp);
            String printRowTemplete = CommonUtil.getSysConfig().getAsString(rowsTemp);
            if (StringUtils.isNotEmpty(printBodyTemplete) && StringUtils.isNotEmpty(printRowTemplete)) {

                Iterator<Dto> iterator = paramList.iterator();
                while (iterator.hasNext()) {
                    Dto next = iterator.next();


                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("title", "标识卡");

                    List<Map<String, Object>> bodyTemplete = JSONUtil.parseJSON2List(printBodyTemplete);
                    Iterator<Map<String, Object>> body = bodyTemplete.iterator();
                    while (body.hasNext()) {
                        Map<String, Object> bodyNext = body.next();
                        String bodyKey = (String) bodyNext.get("key");
                        if (StringUtils.isEmpty(next.getAsString(bodyKey))) {
                            body.remove();
                        } else {
                            bodyNext.put("value", next.getAsString(bodyKey));
                            bodyNext.put("key", bodyNext.get("lable"));
                            bodyNext.remove("lable");
                            bodyNext.remove("sort");
                        }
                    }
                    resultMap.put("body", bodyTemplete);
                    resultList.add(resultMap);


                    List<List<Map<String, Object>>> rows = new LinkedList<>();
                    resultMap.put("rows", rows);

                    if (StringUtils.isNotEmpty(numberKey)) {
                        List<String> numberList = new LinkedList<>();
                        for (String name : numberKey.split("@")) {
                            if (StringUtils.isNotEmpty(name)) {
                                numberList.add(next.getAsString(name));
                            }
                        }
                        resultMap.put("number", String.join(",", numberList));
                    }

                }
            }


        }
        return resultList;
    }


    /**
     * 以托盘为维度
     *
     * @param paramList
     * @return
     */
    private Map<String, List<Dto>> dealWithPrintParam(List<Dto> paramList) {

        Map<String, List<Dto>> result = new HashMap<String, List<Dto>>();

        if (paramList != null) {
            Iterator<Dto> iterator = paramList.iterator();
            while (iterator.hasNext()) {
                Dto next = iterator.next();
                //托盘编号
                String tray_number = next.getAsString("tray_number");
                if (StringUtils.isEmpty(tray_number)) {
                    continue;
                }
                if (result.get(tray_number) != null) {
                    result.get(tray_number).add(next);
                } else {
                    List<Dto> mapList = new ArrayList<>();
                    mapList.add(next);
                    result.put(tray_number, mapList);
                }
            }
        }
        return result;

    }
}
