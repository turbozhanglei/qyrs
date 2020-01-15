package com.dxtf.controller.excel;

import com.dxtf.base.BizAction;
import com.dxtf.service.GuestService;
import com.dxtf.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/excel")
public class ExcelManagerController extends BizAction {

    @Autowired
    GuestService guestService;

    @Autowired
    OrderNumberUtils orderNumberUtils;

    /**
     * 产品类别导入
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "ajaxUploadSkuTypeExcel", method = {RequestMethod.POST})
    public BaseResult ajaxUploadSkuTypeExcel(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try {

            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);

            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }


            //防止重复提交操作(根据redis的key30秒来防止重复提交)
            String repeatToken = redisService.getValue("repeatToken_ajaxUploadSkuTypeExcel_" + dto.getAsString("token"));
            if (repeatToken != "") {
                return null;
            }

            redisService.setValue("repeatToken_ajaxUploadSkuTypeExcel_" + dto.getAsString("token"), dto.getAsString("token"), 30L);

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            InputStream in = null;
            MultipartFile file = multipartRequest.getFile("file");
            if (file == null || file.isEmpty()) {
                throw new Exception("文件不存在！");
            }
            in = file.getInputStream();

            String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

            Workbook wb = null;
            if (".xls".equals(type)) {
                wb = new HSSFWorkbook(in);
            } else if (".xlsx".equals(type)) {
                wb = new XSSFWorkbook(in);
            } else {
                result.setCode(StatusConstant.CODE_9999);
                result.setMsg("请导入xls,xlsx文件 ");
                return result;
            }
            String columns[] = {"food_add_type", "number", "text"};
            List<Dto> excelDto = ExcelUtils.getListByExcel(wb, columns);
            if (excelDto != null && excelDto.size() > 0) {
                Iterator<Dto> iterator = excelDto.iterator();
                while (iterator.hasNext()) {
                    Dto next = iterator.next();

                    String number = next.getAsString("number");
                    if (StringUtils.isNotEmpty(number)) {
                        Dto search = new BaseDto();
                        search.put("number", number);
                        Object o = bizService.queryForDto("skuType.getSkuTypeInfo", search);
                        if (o != null) {
                            iterator.remove();
                        } else {
                            next.put("tableName", "skuType");
                            next.put("method", "saveInfo");
                            next.put("creator", member.getAsString("id"));
                            next.put("updator", member.getAsString("id"));
                        }

                    } else {
                        iterator.remove();
                    }
                }
            }

            //批量导入
            bizService.insertBatch(excelDto);

            result.setData("导入成功");
            redisService.delete("repeatToken_ajaxUploadSkuTypeExcel_" + dto.getAsString("token"));
        } catch (Exception e) {
            redisService.delete("repeatToken_ajaxUploadSkuTypeExcel_" + dto.getAsString("token"));
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 产品信息导入
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "ajaxUploadSkuInfoExcel", method = {RequestMethod.POST})
    public BaseResult ajaxUploadSkuInfoExcel(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try {

            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }


            //防止重复提交操作(根据redis的key30秒来防止重复提交)
            String repeatToken = redisService.getValue("repeatToken_ajaxUploadSkuInfoExcel_" + dto.getAsString("token"));
            if (repeatToken != "") {
                return null;
            }

            redisService.setValue("repeatToken_ajaxUploadSkuInfoExcel_" + dto.getAsString("token"), dto.getAsString("token"), 30L);

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            InputStream in = null;
            MultipartFile file = multipartRequest.getFile("file");
            if (file == null || file.isEmpty()) {
                throw new Exception("文件不存在！");
            }
            in = file.getInputStream();

            String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

            Workbook wb = null;
            if (".xls".equals(type)) {
                wb = new HSSFWorkbook(in);
            } else if (".xlsx".equals(type)) {
                wb = new XSSFWorkbook(in);
            } else {
                result.setCode(StatusConstant.CODE_9999);
                result.setMsg("请导入xls,xlsx文件 ");
                return result;
            }
            String columns[] = {"name", "level", "number", "typename", "unit_volume", "unit_weight", "storage_temp"};
            List<Dto> excelDto = ExcelUtils.getListByExcel(wb, columns);
            if (excelDto != null && excelDto.size() > 0) {


                //查询当前产品有效数据
                Dto skuSize = (Dto) bizService.queryForDto("sku.queryListCount", new BaseDto());
                Integer total = skuSize.getAsInteger("total");

                Map<String, String> tempMap = new HashMap<String, String>();

                Iterator<Dto> iterator = excelDto.iterator();
                while (iterator.hasNext()) {
                    Dto next = iterator.next();

                    String name = next.getAsString("name");
                    if (StringUtils.isNotEmpty(name)) {
                        Dto search = new BaseDto();
                        search.put("_name", name);
                        search.put("_level", next.getAsString("level"));
                        List list = bizService.queryForList("sku.queryList", search);
                        if (list != null && list.size() > 0) {
                            iterator.remove();
                        } else {
                            //类别信息
                            String typename = next.getAsString("typename");
                            if (StringUtils.isEmpty(typename)) {

                            } else {
                                //查询信息
                                Dto query = new BaseDto();
                                query.put("text", typename);
                                Dto skytype = (Dto) bizService.queryForDto("skuType.getSkuTypeInfo", query);
                                if (skytype == null) {
                                    //iterator.remove();
                                } else {
                                    next.put("type_id", skytype.getAsString("id"));
                                }
                            }

                            String key = name + next.getAsString("level");
                            if (tempMap.containsKey(key)) {
                                iterator.remove();
                                continue;
                            } else {
                                tempMap.put(key, key);
                            }

                            total++;
                            next.put("number", orderNumberUtils.intToString(total, 5));
                            next.put("tableName", "sku");
                            next.put("method", "saveInfo");
                            next.put("creator", member.getAsString("id"));
                            next.put("updator", member.getAsString("id"));
                        }

                    } else {
                        iterator.remove();
                    }
                }
            }

            //批量导入
            bizService.insertBatch(excelDto);

            result.setData("导入成功");
            redisService.delete("repeatToken_ajaxUploadSkuInfoExcel_" + dto.getAsString("token"));
        } catch (Exception e) {
            redisService.delete("repeatToken_ajaxUploadSkuInfoExcel_" + dto.getAsString("token"));
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 司机信息导入
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "ajaxUploadDriverExcel", method = {RequestMethod.POST})
    public BaseResult ajaxUploadDriverExcel(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try {

            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }


            //防止重复提交操作(根据redis的key30秒来防止重复提交)
            String repeatToken = redisService.getValue("repeatToken_ajaxUploadDriverExcel_" + dto.getAsString("token"));
            if (repeatToken != "") {
                return null;
            }

            redisService.setValue("repeatToken_ajaxUploadDriverExcel_" + dto.getAsString("token"), dto.getAsString("token"), 30L);

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            InputStream in = null;
            MultipartFile file = multipartRequest.getFile("file");
            if (file == null || file.isEmpty()) {
                throw new Exception("文件不存在！");
            }
            in = file.getInputStream();

            String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

            Workbook wb = null;
            if (".xls".equals(type)) {
                wb = new HSSFWorkbook(in);
            } else if (".xlsx".equals(type)) {
                wb = new XSSFWorkbook(in);
            } else {
                result.setCode(StatusConstant.CODE_9999);
                result.setMsg("请导入xls,xlsx文件 ");
                return result;
            }
            String columns[] = {"drivername", "mobile", "driver_type", "driver_number", "remark"};
            List<Dto> excelDto = ExcelUtils.getListByExcel(wb, columns);
            if (excelDto != null && excelDto.size() > 0) {
                Iterator<Dto> iterator = excelDto.iterator();
                while (iterator.hasNext()) {
                    Dto next = iterator.next();

                    String driver_number = next.getAsString("driver_number");
                    if (StringUtils.isNotEmpty(driver_number)) {
                        Dto search = new BaseDto();
                        search.put("driver_number", driver_number);
                        Dto list = (Dto) bizService.queryForDto("wdriver.getInfo", search);
                        if (list != null) {
                            iterator.remove();
                        } else {

                            next.put("tableName", "wdriver");
                            next.put("method", "saveInfo");
                            next.put("status", "0");
                            next.put("creator", member.getAsString("id"));
                            next.put("updator", member.getAsString("id"));
                        }

                    } else {
                        iterator.remove();
                    }
                }
            }

            //批量导入
            bizService.insertBatch(excelDto);

            result.setData("导入成功");
            redisService.delete("repeatToken_ajaxUploadDriverExcel_" + dto.getAsString("token"));
        } catch (Exception e) {
            redisService.delete("repeatToken_ajaxUploadDriverExcel_" + dto.getAsString("token"));
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 客户信息导入
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "ajaxUploadCustomerExcel", method = {RequestMethod.POST})
    public BaseResult ajaxUploadCustomerExcel(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try {

            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }


            //防止重复提交操作(根据redis的key30秒来防止重复提交)
            String repeatToken = redisService.getValue("repeatToken_ajaxUploadCustomerExcel_" + dto.getAsString("token"));
            if (repeatToken != "") {
                return null;
            }

            redisService.setValue("repeatToken_ajaxUploadCustomerExcel_" + dto.getAsString("token"), dto.getAsString("token"), 30L);

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            InputStream in = null;
            MultipartFile file = multipartRequest.getFile("file");
            if (file == null || file.isEmpty()) {
                throw new Exception("文件不存在！");
            }
            in = file.getInputStream();

            String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

            Workbook wb = null;
            if (".xls".equals(type)) {
                wb = new HSSFWorkbook(in);
            } else if (".xlsx".equals(type)) {
                wb = new XSSFWorkbook(in);
            } else {
                result.setCode(StatusConstant.CODE_9999);
                result.setMsg("请导入xls,xlsx文件 ");
                return result;
            }
            String columns[] = {"customerno", "client_name", "socialcode", "registered_capital", "legalname", "paperwork_type", "paperwork_no", "effective_time", "manager_name",
                    "manager_paperwork", "manager_papertype", "manager_effectivetime", "customer_type", "trade_type", "manager_mobile", "manager_email", "isdeal", "warningday", "remark", "business_date",
                    "businesslicense"};
            List<Dto> excelDto = ExcelUtils.getListByExcel(wb, columns);
            if (excelDto != null && excelDto.size() > 0) {
                Iterator<Dto> iterator = excelDto.iterator();
                while (iterator.hasNext()) {
                    Dto next = iterator.next();

                    String client_name = next.getAsString("client_name");
                    if (StringUtils.isNotEmpty(client_name)) {
                        Dto search = new BaseDto();
                        search.put("client_name", client_name);
                        Dto list = (Dto) bizService.queryForDto("wcustomer.getCustomerInfo", search);
                        if (list != null) {
                            iterator.remove();
                        } else {

                            //客户性质
                            String customer_type = next.getAsString("customer_type");

                            //查询sys_config
                            List<Dto> customer_type_list = bizService.queryForList("sysConfig.queryList", new BaseDto("type", "customer_type"));
                            if (!customer_type_list.isEmpty() && StringUtils.isNotEmpty(customer_type)) {

                                boolean isSave = true;
                                Iterator<Dto> typeiterator = customer_type_list.iterator();
                                while (typeiterator.hasNext()) {
                                    Dto customer_type_sys = typeiterator.next();
                                    if (StringUtils.equals(customer_type, customer_type_sys.getAsString("val"))) {
                                        next.put("customer_type", customer_type_sys.getAsString("key"));
                                        isSave = false;
                                        break;
                                    } else {
                                    }
                                }

                                if (isSave) {
                                    //插入数据库
                                    Dto save = new BaseDto();
                                    save.put("key", customer_type_list.size() + 1);
                                    save.put("val", customer_type);
                                    save.put("type", "customer_type");
                                    save.put("remark", "客户性质");
                                    save.put("tableName", "sysConfig");
                                    save.put("creator", member.getAsString("id"));
                                    save.put("updator", member.getAsString("id"));
                                    bizService.saveInfo(save);

                                    next.put("customer_type", customer_type_list.size() + 1);
                                }


                            }

                            //法人证件类型
                            String paperwork_type = next.getAsString("paperwork_type");


                            //查询sys_config
                            List<Dto> paperwork_type_list = bizService.queryForList("sysConfig.queryList", new BaseDto("type", "paperwork_type"));
                            if (!paperwork_type_list.isEmpty() && StringUtils.isNotEmpty(paperwork_type)) {

                                Iterator<Dto> typeiterator = paperwork_type_list.iterator();
                                boolean isSave = true;
                                while (typeiterator.hasNext()) {
                                    Dto customer_type_sys = typeiterator.next();
                                    if (StringUtils.equals(paperwork_type, customer_type_sys.getAsString("val"))) {
                                        next.put("paperwork_type", customer_type_sys.getAsString("key"));
                                        isSave = false;
                                        break;
                                    } else {

                                    }
                                }

                                if (isSave) {
                                    //插入数据库
                                    Dto save = new BaseDto();
                                    save.put("key", paperwork_type_list.size() + 1);
                                    save.put("val", paperwork_type);
                                    save.put("type", "paperwork_type");
                                    save.put("remark", "证件类型");
                                    save.put("tableName", "sysConfig");
                                    save.put("creator", member.getAsString("id"));
                                    save.put("updator", member.getAsString("id"));
                                    bizService.saveInfo(save);

                                    next.put("paperwork_type", paperwork_type_list.size() + 1);
                                }
                            }

                            //经办人证件类型
                            String manager_papertype = next.getAsString("manager_papertype");
                            //查询sys_config
                            List<Dto> manager_papertype_list = bizService.queryForList("sysConfig.queryList", new BaseDto("type", "paperwork_type"));
                            if (!manager_papertype_list.isEmpty() && StringUtils.isNotEmpty(manager_papertype)) {

                                Iterator<Dto> typeiterator = manager_papertype_list.iterator();
                                boolean isSave = true;
                                while (typeiterator.hasNext()) {
                                    Dto customer_type_sys = typeiterator.next();
                                    if (StringUtils.equals(manager_papertype, customer_type_sys.getAsString("val"))) {
                                        next.put("manager_papertype", customer_type_sys.getAsString("key"));
                                        isSave = false;
                                        break;
                                    } else {

                                    }
                                }

                                if (isSave) {
                                    //插入数据库
                                    Dto save = new BaseDto();
                                    save.put("key", manager_papertype_list.size() + 1);
                                    save.put("val", manager_papertype);
                                    save.put("type", "paperwork_type");
                                    save.put("remark", "证件类型");
                                    save.put("tableName", "sysConfig");
                                    save.put("creator", member.getAsString("id"));
                                    save.put("updator", member.getAsString("id"));
                                    bizService.saveInfo(save);

                                    next.put("manager_papertype", manager_papertype_list.size() + 1);
                                }
                            }

                            //行业类型
                            String trade_type = next.getAsString("trade_type");
                            //查询sys_config
                            List<Dto> trade_type_list = bizService.queryForList("sysConfig.queryList", new BaseDto("type", "trade_type"));
                            if (!trade_type_list.isEmpty() && StringUtils.isNotEmpty(trade_type)) {

                                Iterator<Dto> typeiterator = trade_type_list.iterator();
                                boolean isSave = true;
                                while (typeiterator.hasNext()) {
                                    Dto customer_type_sys = typeiterator.next();
                                    if (StringUtils.equals(trade_type, customer_type_sys.getAsString("val"))) {
                                        next.put("trade_type", customer_type_sys.getAsString("key"));
                                        isSave = false;
                                        break;
                                    } else {

                                    }

                                }
                                if (isSave) {
                                    //插入数据库
                                    Dto save = new BaseDto();
                                    save.put("key", trade_type_list.size() + 1);
                                    save.put("val", trade_type);
                                    save.put("type", "trade_type");
                                    save.put("remark", "行业类型");
                                    save.put("tableName", "sysConfig");
                                    save.put("creator", member.getAsString("id"));
                                    save.put("updator", member.getAsString("id"));
                                    bizService.saveInfo(save);

                                    next.put("trade_type", trade_type_list.size() + 1);

                                }
                            }


                            //是否签了合同 0 是
                            String isdeal = next.getAsString("isdeal");
                            if (StringUtils.equals("是", isdeal)) {
                                next.put("isdeal", 0);
                            } else {
                                next.put("isdeal", 1);
                            }

                            //有效期预警天数
                            String warningday = next.getAsString("warningday");
                            //判断warningday存在则计算warningday_num
                            if (StringUtils.isNotEmpty(warningday)) {
                                double warningday_num = guestService.getWarningdayNum(warningday);
                                next.put("warningday_num", 1 - warningday_num);
                            }

                            next.put("tableName", "wcustomer");
                            next.put("method", "saveInfo");
                            next.put("status", "0");
                            next.put("creator", member.getAsString("id"));
                            next.put("updator", member.getAsString("id"));

                            String cuntomerNo = orderNumberUtils.getCuntomerNo();
                            next.put("customerno", "cuntomerNo");
                        }

                    } else {
                        iterator.remove();
                    }
                }
            }

            //批量导入
            bizService.insertBatch(excelDto);

            redisService.delete("THMJ_" + StatusConstant.CONFIG_SYSTEM);

            result.setData("导入成功");
            redisService.delete("repeatToken_ajaxUploadCustomerExcel_" + dto.getAsString("token"));
        } catch (Exception e) {
            redisService.delete("repeatToken_ajaxUploadCustomerExcel_" + dto.getAsString("token"));
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 托盘信息导入
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "ajaxUploadTrayExcel", method = {RequestMethod.POST})
    public BaseResult ajaxUploadTrayExcel(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try {

            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }


            //防止重复提交操作(根据redis的key30秒来防止重复提交)
            String repeatToken = redisService.getValue("repeatToken_ajaxUploadTrayExcel_" + dto.getAsString("token"));
            if (repeatToken != "") {
                return null;
            }

            redisService.setValue("repeatToken_ajaxUploadTrayExcel_" + dto.getAsString("token"), dto.getAsString("token"), 30L);

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            InputStream in = null;
            MultipartFile file = multipartRequest.getFile("file");
            if (file == null || file.isEmpty()) {
                throw new Exception("文件不存在！");
            }
            in = file.getInputStream();

            String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

            Workbook wb = null;
            if (".xls".equals(type)) {
                wb = new HSSFWorkbook(in);
            } else if (".xlsx".equals(type)) {
                wb = new XSSFWorkbook(in);
            } else {
                result.setCode(StatusConstant.CODE_9999);
                result.setMsg("请导入xls,xlsx文件 ");
                return result;
            }
            String columns[] = {"name", "norm", "maxweight", "volume", "railway_platform", "trayphoto", "dept_name", "manager", "manager_mobile", "tray_type_name", "remark", "num"};
            List<Dto> excelDto = ExcelUtils.getListByExcel(wb, columns);
            //获取所有部门信息
            List<Dto> deptList = bizService.queryForList("sysDept.queryList", new BaseDto("type", 20));
            Map<String, Integer> deptMap = new HashMap<>();
            for (Dto dept : deptList) {
                deptMap.put(dept.getAsString("dept_name"), dept.getAsInteger("id"));
            }


            //查询当前托盘数量
            Dto countDto = (Dto) bizService.queryForDto("wtray.queryTrayCount", new BaseDto());
            Integer total = countDto.getAsInteger("total");

            if (excelDto != null && excelDto.size() > 0) {
                List<Dto> insertList = new LinkedList<>();
                Iterator<Dto> iterator = excelDto.iterator();
                while (iterator.hasNext()) {
                    Dto next = iterator.next();

                    String name = next.getAsString("name");
                    if (StringUtils.isNotEmpty(name)) {


                        String dept_name = next.getAsString("dept_name");

                        if (StringUtils.isNotEmpty(dept_name) && deptMap.containsKey(dept_name)) {
                            next.put("dept_id", deptMap.get(dept_name));
                        }


                        String tray_type_name = next.getAsString("tray_type_name");
                        if (StringUtils.isNotEmpty(tray_type_name)) {
                            if (tray_type_name.equals("木制托盘")) {
                                next.put("tray_type", 0);
                            } else if (tray_type_name.equals("塑料托盘")) {
                                next.put("tray_type", 1);
                            } else if (tray_type_name.equals("钢制托盘")) {
                                next.put("tray_type", 2);
                            }

                        }

                        String numExcel = next.getAsString("num");
                        int num = 1;
                        if (StringUtils.isNotEmpty(numExcel)) {
                            num = Double.valueOf(numExcel).intValue();
                        }

                        for (int i = 0; i < num; i++) {
                            total++;
                            String number = "DX-TP-" + orderNumberUtils.intToString(total, 3);
                            next.put("tableName", "wtray");
                            next.put("method", "saveInfo");
                            next.put("status", "0");
                            next.put("creator", member.getAsString("id"));
                            next.put("updator", member.getAsString("id"));
                            next.put("number", number);
                            insertList.add(JSONUtil.parseJSON2Dto(next.toJson()));
                        }


                    } else {
                        iterator.remove();
                    }
                }
                //批量导入
                bizService.insertBatch(insertList);

            }


            result.setData("导入成功");
            redisService.delete("repeatToken_ajaxUploadTrayExcel_" + dto.getAsString("token"));
        } catch (Exception e) {
            redisService.delete("repeatToken_ajaxUploadTrayExcel_" + dto.getAsString("token"));
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }



}
