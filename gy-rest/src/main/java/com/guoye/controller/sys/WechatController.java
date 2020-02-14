package com.guoye.controller.sys;

import com.alibaba.fastjson.JSON;
import com.guoye.base.BizAction;
import com.guoye.bean.Wxpoid;
import com.guoye.util.BaseResult;
import com.guoye.util.HttpClientUtil;
import com.guoye.util.StatusConstant;
import net.sf.json.JSONObject;
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
            Map<String, String> param = new HashMap<>();
            param.put("appid", "wx8638e80c7186b393");
            param.put("secret", "f2b40a6cb42b71deb901e8ef180697a4");
            param.put("js_code", dto.getAsString("code"));
            param.put("grant_type", "authorization_code");
            Map<String, String> head = new HashMap<>();
            head.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            String s = HttpClientUtil.doRequestGet("https://api.weixin.qq.com/sns/jscode2session", param, head);
            if (StringUtils.isNotEmpty(s)) {
                JSONObject jsonObject = JSONObject.fromObject(s);
                  //获取openid
                String openid = jsonObject.getString("openid");
                redisService.setValue("openid",openid);
                //获取session_key
                String session_key=jsonObject.getString("session_key");


            }

        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    //返回个人信息
    @RequestMapping(value = "/userInformation")
    public BaseResult userInformation(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();

        try {

            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);

            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }
            //小程序端唯一标识
            String unionid = dto.getAsString("unionid");
            dto.put("tableName", "sysUser");


            if (StringUtils.isNotEmpty(unionid)) {
                Dto udto=(BaseDto)bizService.queryForDto("sysUser.getInfo",new BaseDto("unionid",dto.getAsString("unionid")));
                if(udto !=null){
                    Dto userdto=(BaseDto)bizService.queryForDto("sysUser.getUserInfo",new BaseDto("id",udto.getAsString("id")));
                    result.setData(userdto);
                }

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
            if (StringUtils.isNotEmpty(id) && udto !=null) {
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
