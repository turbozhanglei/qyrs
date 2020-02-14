package com.guoye.controller.sys;

import com.alibaba.fastjson.JSON;
import com.guoye.base.BizAction;
import com.guoye.bean.Wxpoid;
import com.guoye.util.BaseResult;
import com.guoye.util.StatusConstant;
import com.guoye.util.UserUtil;
import com.sun.deploy.net.HttpUtils;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.resource.util.StringUtils;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信用户业务处理
 *
 * @author zxl
 * @see SysUserController
 * @since 2017年6月20日15:49:06
 */
@RestController
@RequestMapping("/wechat")
public class WechatController extends BizAction {


    //微信登录接口
    @RequestMapping(value = "/wechatLogin")
    public BaseResult wechatLogin(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();

        try {

            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            redisService.delete("512");
            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }
            String openid = null;
            if (StringUtils.isNotEmpty(dto.getAsString("code"))) {
                String url = "https://api.weixin.qq.com/sns/jscode2session";
                Dto param=new BaseDto();
                openid = UserUtil.getopenid(dto.getAsString("code"));
                Wxpoid json = JSON.parseObject(openid, Wxpoid.class);
//                param.put("appid", WxApp.APPID);
//                param.put("secret", WxApp.SECRET);
//                param.put("js_code", code);
//                param.put("grant_type", "authorization_code");
//
//                String wxResult = HttpUtils.doGet(url, param);
//                log.info(wxResult);
//
//                WXSessionModel model = JsonUtils.jsonToPojo(wxResult, WXSessionModel.class);
//
//                log.info(model.toString());
//                // 存入session到redis
//                redis.set("user-redis-session:" + model.getOpenid(),
//                        model.getSession_key(),
//                        1000 * 60 * 30);
//
//                return JSONResult.ok();
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    //个人信息修改
    @RequestMapping(value = "/editWeUser")
    public BaseResult editUser(HttpServletRequest request) {
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
            dto.put("tableName", "sysUser");

            Dto udto=(BaseDto)bizService.queryForDto("sysUser.getInfo",new BaseDto("id",dto.getAsString("id")));

            // 修改
            if (StringUtils.isNotEmpty(id)) {

                if(udto.getAsInteger("editSum")==1){
                    throw new Exception("性别只可修改一次");
                }
                if (udto.getAsString("sex")!=null && udto.getAsString("sex")!=""){
                    dto.put("editSum",1);
                }

                dto.put("updator", member == null ? "" : member.get("id"));
                bizService.updateInfo(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


}
