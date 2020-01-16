package com.guoye.controller.common;

import com.guoye.base.BizAction;
import com.guoye.util.*;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.util.G4Constants;
import org.g4studio.core.util.G4Utils;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 通用业务处理
 *
 * @author mcl
 * @see
 * @since 16点36分
 */
@RestController
@RequestMapping("/cmn")
public class CommonController extends BizAction {

    /**
     * 查询对象
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryInfo")
    public BaseResult queryInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto info = (BaseDto) bizService.queryForDto(dto.getAsString("t") + ".getInfo", dto);
            if (StringUtils.isNotEmpty(dto.getAsString("isNotDate"))) {
                result.setData(info);
            } else {
                result.setData(JSONUtil.formatDateObject(info, G4Constants.FORMAT_DateTime));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
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
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member && !"sysConfig".equals(dto.getAsString("t"))) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }

            //查询总网点
            String sql = dto.getAsString("sql");
            if (!StringUtils.isNotBlank(sql)) {
                sql = "queryList";
            }
            List paramList = bizService.queryForList(dto.getAsString("t") + "." + sql, dto);

            if (paramList.isEmpty()) {
            } else {
                result.setData(JSONUtil.formatDateList(paramList, G4Constants.FORMAT_DateTime));
            }
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 查询对象 数据模糊
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryInfoRole")
    public BaseResult queryInfoRole(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            Dto info;
            String sql = dto.getAsString("sql");
            if (StringUtils.isNotBlank(sql)) {
                info = (BaseDto) bizService.queryForDto(dto.getAsString("t") + "." + sql, dto);
            } else {
                info = (BaseDto) bizService.queryForDto(dto.getAsString("t") + ".getInfo", dto);
            }
            result.setData(info);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
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
    @RequestMapping(value = "/queryListRole")
    public BaseResult queryListRole(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            dto.put("userid", member == null ? "" : member.get("id"));
            List paramList = null;
            String sql = dto.getAsString("sql");
            if (StringUtils.isNotBlank(sql)) {
                paramList = bizService.queryForList(dto.getAsString("t") + "." + sql, dto);
            } else {
                paramList = bizService.queryForList(dto.getAsString("t") + ".queryList", dto);
            }
            result.setData(JSONUtil.formatDateList(paramList, G4Constants.FORMAT_DateTime));
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
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
    @RequestMapping(value = "/queryPage")
    public Dto queryPage(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        Dto retDto = new BaseDto();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            dto.put("userid", member == null ? "" : member.get("id"));
            List<Dto> paramList = bizService.queryForPage(dto.getAsString("t") + ".queryList", dto);
            System.out.println("分页查询：   " + paramList.size());

            if (!paramList.isEmpty()) {
                retDto.put("rows", JSONUtil.formatDateList(paramList, G4Constants.FORMAT_DateTime));
            }
            retDto.put("total", bizService.queryForList(dto.getAsString("t") + ".queryList", dto).size());
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return retDto;
    }


    /**
     * 分页
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryPages")
    public Dto queryPages(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        Dto retDto = new BaseDto();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                retDto.put("code", StatusConstant.CODE_4000);
                retDto.setMsg("请登录");
                return retDto;
            }
            String sql = dto.getAsString("sql");
            if (!StringUtils.isNotBlank(sql)) {
                sql = "queryList";
            }

            List<Dto> paramList = bizService.queryForPageCenter(dto.getAsString("t") + "." + sql, dto);
            if (StringUtils.isNotEmpty(dto.getAsString("isNotDate"))) {
                retDto.put("rows", paramList);
            } else {
                if (!paramList.isEmpty()) {
                    retDto.put("rows", JSONUtil.formatDateList(paramList, G4Constants.FORMAT_DateTime));
                }
            }
            String sqlCount = dto.getAsString("sqlCount");
            if (!StringUtils.isNotBlank(sqlCount)) {
                sqlCount = "queryListCount";
            }
            List<Dto> totalCount = bizService.queryForList(dto.getAsString("t") + "." + sqlCount, dto);
            if (null != totalCount && totalCount.size() > 0) {
                retDto.put("total", totalCount.get(0).getAsInteger("total"));
            } else {
                retDto.put("total", 0);
            }
            retDto.put("code", "0000");
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return retDto;
    }


    /**
     * 分页
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryPageRole")
    public Dto queryPageRole(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        Dto retDto = new BaseDto();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            dto.put("userid", member == null ? "" : member.get("id"));
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

    /**
     * 保存信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/editInfo")
    public BaseResult editInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        System.out.println("nmsl 不进SQL");
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
            if (inDto.getAsLong("id") != null) {
                // 修改
                inDto.put("updator", member == null ? "" : member.get("id"));
                bizService.updateInfo(inDto);
            } else {
                // 新增
                if (member != null) {
                    inDto.put("deptid ", member.get("deptid "));
                }
                inDto.put("creator", member == null ? "" : member.get("id"));
                inDto.put("updator", member == null ? "" : member.get("id"));
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
     * 删除信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteInfo")
    public BaseResult deleteInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            bizService.deleteInfo(inDto);
            result.setData(new BaseDto("msg", "数据操作成功"));
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 批量删除信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/batchDeleteInfo")
    public BaseResult batchDeleteInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            String ids = inDto.getAsString("ids");
            if (StringUtils.isEmpty(ids)) {
                throw new Exception("id不能为空");

            }

            for (String id : ids.split(",")) {
                if (StringUtils.isNotEmpty(id)) {
                    inDto.put("id", id);
                    bizService.deleteInfo(inDto);
                }
            }

            result.setData(new BaseDto("msg", "数据操作成功"));
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 发送短信验证码
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendMsg")
    public BaseResult sendMsg(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto config = (Dto) bizService.queryForDto("sysConfig.getInfo", new BaseDto("id", 6));
            String phone = config.getAsString("val");
            Integer code = new Random().nextInt(999999) % (999999 - 100000 + 1) + 100000;
            System.out.println("---------------" + code);
            redisService.setValue("SAFE_CODE_" + inDto.getAsString("token"), code + "", 120L);
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 发送短信验证码校验
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkCode")
    public BaseResult checkCode(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            String code = redisService.getValue("SAFE_CODE_" + inDto.getAsString("token"));
            if (!code.equals(inDto.getAsString("msgcode"))) {
                throw new Exception("验证码不正确");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 修改安全手机新手机发送验证码
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendCode")
    public BaseResult sendCode(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            String newMobilePhoneNum = inDto.getAsString("newmobile");
            Integer code = new Random().nextInt(999999) % (999999 - 100000 + 1) + 100000;
            redisService.setValue("SAFE_MOBILE_NEW_" + inDto.getAsString("token"), newMobilePhoneNum, 120L);
            redisService.setValue("SAFE_CODE_NEW_" + inDto.getAsString("token"), String.valueOf(code), 120L);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 获取token
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getVtoken")
    public BaseResult getVtoken(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            result.setData(UUID.randomUUID().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 生成图形验证码
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createImgCode")
    public void createImgCode(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getCodeImageParamAsDto(request);
        BaseResult result = new BaseResult();
        String vtoken = inDto.getAsString("vtoken");
        String verifyCode = null;
        try {
            // 获取app传过来的宽高 , 如果没有证明是pc
            String h = inDto.getAsString("h");
            String w = inDto.getAsString("w");
            if (StringUtils.isNotBlank(h) && StringUtils.isNotBlank(w)) {
                verifyCode = GraphicHelper.createImge(Integer.valueOf(w), Integer.valueOf(h), "png",
                        response.getOutputStream());
            } else {
                verifyCode = GraphicHelper.createImge(130, 36, "png", response.getOutputStream());
            }
            // 图片验证码存放到缓存
            redisService.delete(vtoken + "_IMAGE_CODE");
            redisService.setValue(vtoken + "_IMAGE_CODE", verifyCode);
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
            result = reduceErr("生成验证码失败");
        }
    }

    /**
     * 上传图片
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/imgUpload", method = RequestMethod.POST)
    public BaseResult imgUpload(HttpServletRequest request, Long bizid, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("file");
            if (file.isEmpty()) {
                throw new Exception("文件不存在！");
            }
            String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String imgUrl = FileUtils.storeImg(file.getInputStream(),
                    G4Utils.getCurrentTime("yyyyMMddhhmmss") + System.currentTimeMillis() + type,
                    DateUtil.getStringFromDate(new Date(), "yyyyMM"));
            result.setData(new BaseDto("imgUrl", imgUrl));
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 获取当前登陆用户
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryLoginUser")
    public BaseResult queryLoginUser(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult retDto = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                retDto.setCode(StatusConstant.CODE_4000);
                retDto.setMsg("请登录");
                return retDto;
            }
            retDto.setData(member.getAsString("roleid"));
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return retDto;
    }


    /**
     * 清除参数配置缓存
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteSysCache")
    public BaseResult deleteSysCache(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult retDto = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (null == member) {
                retDto.setCode(StatusConstant.CODE_4000);
                retDto.setMsg("请登录");
                return retDto;
            }
            redisService.delete("THMJ_" + StatusConstant.CONFIG_SYSTEM);
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return retDto;
    }

}
