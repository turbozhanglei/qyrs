package com.dxtf.controller.sys;

import com.dxtf.base.BizAction;
import com.dxtf.util.BaseResult;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 后台用户业务处理
 *
 * @author mcl
 * @see SysController
 * @since 2017年6月20日15:49:06
 */
@RestController
@RequestMapping("/sys")
public class SysController extends BizAction {


    /**
     * 保存通知消息管理
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveMsgConfig")
    public BaseResult saveMsgConfig(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            dto.put("updator", member == null ? "" : member.get("id"));
            // 找出总开关
            Boolean upFlag = true;
            for (Object key : dto.keySet()) {
                if (key.toString().equals("msgset") &&
                        dto.getAsString(key.toString()).equals("0")) {// 总开关如果关闭，则所有通知均关闭
                    Dto pDto = new BaseDto("tableName", "sysMsgConfig");
                    pDto.put("method", "disableAll");
                    bizService.update(pDto);
                    upFlag = false;
                    break;
                }
            }
            if (upFlag) {// 总开关未关闭  则针对每一个设置进行修改
                for (Object key : dto.keySet()) {
                    if (key.toString().contains("times_")) {
                        Dto pDto = new BaseDto("tableName", "sysMsgConfig");
                        pDto.put("times", dto.getAsString(key.toString()));
                        pDto.put("id", key.toString().substring(6));
                        pDto.put("status", "1");
                        bizService.updateInfo(pDto);
                    }
                }
                for (Object key : dto.keySet()) {
                    if (key.toString().contains("config_")) {
                        Dto pDto = new BaseDto("tableName", "sysMsgConfig");
                        pDto.put("status", dto.getAsString(key.toString()).equals("0") ?
                                "" : dto.getAsString(key.toString()));
                        pDto.put("id", key.toString().substring(7));
                        if (key.toString().substring(7).equals("2") || key.toString().substring(7).equals("3")
                                || key.toString().substring(7).equals("14") || key.toString().substring(7).equals("17")) {
                            if (dto.getAsString(key.toString()).equals("0")) {
                                pDto.put("times", "0");
                            }
                        }
                        bizService.updateInfo(pDto);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 保存系统安全配置信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveSafeConfig")
    public BaseResult saveSafeConfig(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            // 找出总开关
            Boolean upFlag = true;
            for (Object key : dto.keySet()) {
                if (key.toString().equals("msgset") &&
                        dto.getAsString(key.toString()).equals("0")) {// 总开关如果关闭，则所有通知均关闭
                    Dto pDto = new BaseDto("tableName", "sysSafeConfig");
                    pDto.put("method", "disableAll");
                    bizService.update(pDto);
                    upFlag = false;
                    break;
                }
            }
            if (upFlag) {// 总开关未关闭  则针对每一个设置进行修改
                for (Object key : dto.keySet()) {
                    if (key.toString().contains("config_")) {
                        Dto pDto = new BaseDto("tableName", "sysSafeConfig");
                        pDto.put("status", dto.getAsString(key.toString()).equals("0") ?
                                "" : dto.getAsString(key.toString()));
                        pDto.put("id", key.toString().substring(7));
                        bizService.updateInfo(pDto);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 修改安全配置手机号
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/doUpdateMobile")
    public BaseResult doUpdateMobile(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            String oldCodeFromWeb = dto.getAsString("pcode");
            // 校验老手机短信验证码是否正确
            String oldCode = redisService.getValue("SAFE_CODE_" + dto.getAsString("token"));
            if (StringUtils.isEmpty(oldCode)) {
                throw new Exception("原验证码已经失效");
            }
            if (!oldCode.equals(oldCodeFromWeb)) {
                throw new Exception("原验证码不匹配");
            }
            String newCodeFromWeb = dto.getAsString("code");
            String newMobileFromWeb = dto.getAsString("newmobile");
            // 校验新手机短信验证码是否正确
            String newMobile = redisService.getValue("SAFE_MOBILE_NEW_" + dto.getAsString("token"));
            String newCode = redisService.getValue("SAFE_CODE_NEW_" + dto.getAsString("token"));
            if (StringUtils.isEmpty(newCode) || StringUtils.isEmpty(newMobile)) {
                throw new Exception("新验证码已经失效");
            }
            if (!newCodeFromWeb.equals(newCode)) {
                throw new Exception("新验证码不匹配");
            }
            if (!newMobileFromWeb.equals(newMobile)) {
                throw new Exception("修改手机不匹配");
            }
            // 修改手机
            Dto upDto = new BaseDto("tableName", "sysConfig");
            upDto.put("id", 6);
            Dto config = (Dto) bizService.queryForDto("sysConfig.getInfo", upDto);
            String phone = config.getAsString("val");
            if (phone.equals(newMobileFromWeb)) {
                throw new Exception("原手机号与新手机号不能相同");
            }

            config.put("val", newMobile);
            config.put("tableName", "sysConfig");
            bizService.updateInfo(config);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 修改长琴币配置手机号
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/savecoinConfig")
    public BaseResult savecoinConfig(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            for (Object key : dto.keySet()) {
                if (key.toString().contains("config_")) {
                    Dto pDto = new BaseDto("tableName", "sysConfig");
                    pDto.put("val", dto.getAsString(key.toString()));
                    pDto.put("id", key.toString().substring(7));
                    bizService.updateInfo(pDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 获取客服聊天token
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getKftoken")
    public BaseResult getKftoken(HttpServletRequest request) {
        BaseResult result = new BaseResult();
        try {
            result.setData(bizService.queryForDto("sysUser.getKftoken", null));
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 将通知 置为已读
     *
     * @param
     * @return
     */
    /*@ResponseBody
    @RequestMapping(value="/readNotice")
	public BaseResult readNotice(HttpServletRequest request) {
		BaseResult result = new BaseResult();
		Dto dto = WebUtils.getParamAsDto(request);
		try {
			dto.put("tableName", "cmsgNotice");
			Dto member = redisService.getObject(dto.getAsString("token"),BaseDto.class);
			dto.put("updator", member==null?"":member.get("id"));
			String[] ids = dto.getAsString("ids").split(";");
			for(String id:ids){
				if(!id.isEmpty()){
					dto.put("id", id);
					dto.put("status", 1);
					bizService.updateInfo(dto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = reduceErr(e.getLocalizedMessage());
		}
		return result;
	}*/
}

