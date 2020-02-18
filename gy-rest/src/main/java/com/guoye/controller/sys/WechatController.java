package com.guoye.controller.sys;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.guoye.base.BizAction;
import com.guoye.util.*;
import com.ning.http.util.Base64;
import net.sf.json.JSONObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.resource.util.StringUtils;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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


    //获取openid and session_key
    @ResponseBody
    @RequestMapping(value = "/getOpenid")
    public BaseResult getOpenid(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();

        try {

            String openids = UserUtil.getopenid(dto.getAsString("code"));
            JSONObject jsonObject = JSONObject.fromObject(openids);
            if (StringUtils.isNotEmpty(openids) && openids!="") {
                Dto udto=new BaseDto();
                  //获取openid
                String openid = jsonObject.getString("openid");
                //获取session_key
                String session_key=jsonObject.getString("session_key");
                //获取unionId
//                if(jsonObject.getString("unionId") != null && jsonObject.getString("unionId") != ""){
//                    String unionid=jsonObject.getString("unionId");
//                    redisService.setValue("unionId",unionid);
//                    udto.put("unionid",unionid);
//                }

                udto.put("openid",openid);
                udto.put("session_key",session_key);

                result.setData(udto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }



    //获取用户信息 保存到数据库 ---- 授权登录
    @ResponseBody
    @RequestMapping(value = "/wechatLogin")
    public BaseResult wechatLogin(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        DESWrapper Des = new DESWrapper();
        String password = "9588028820109132570743325311898426347857298773549468758875018579537757772163084478873699447306034466200616411960574122434059469100235892702736860872901247123456";

        try {
              String token = UUID.randomUUID().toString();
              Dto member =(BaseDto)bizService.queryForDto("sysUser.getInfo",new BaseDto("openid",dto.getAsString("openid")));
              if(null !=member){
                  member.put("mobile",Des.decrypt(member.getAsString("mobile"),password));//解密手机号
                  member.put("token",token);
                  redisService.setValue("mp_login_token:"+token, JSONArray.toJSONString(member), 7200l);
                  result.setData(member);
              }else {
                  if(StringUtils.isNotEmpty(dto.getAsString("nickName"))){
                      //保存用户信息
                      Dto udto=new BaseDto();
                      udto.put("tableName","sysUser");
                      udto.put("nickname",dto.getAsString("nickName"));
                      udto.put("head_pic",dto.getAsString("avatarUrl"));
                      udto.put("sex",dto.getAsString("gender"));//性别
                      udto.put("status",0);
                      udto.put("source",1);//来源
                      udto.put("identity_type",0);//前台用户
                      udto.put("editSum",0);//性别修改次数
                      udto.put("openid",dto.getAsString("openid"));
                      udto.put("unionid",dto.getAsString("unionid"));
                      bizService.saveInfo(udto);
                      udto.put("id",udto.getAsString("id"));//返回当前用户登录的id
                      udto.put("token",token);
                      redisService.setValue("mp_login_token:"+token, JSONArray.toJSONString(udto), 7200l);
                      result.setData(udto);

                  }
              }

        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 解密微信手机号 并更新到数据库
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/decryptWxMobile")
    public BaseResult decryptWxMobile(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result=new BaseResult();
        Dto resultParam = new BaseDto();
        DESWrapper Des = new DESWrapper();
        String password = "9588028820109132570743325311898426347857298773549468758875018579537757772163084478873699447306034466200616411960574122434059469100235892702736860872901247123456";

        try {
            String clt=dto.getAsString("clt");
            com.alibaba.fastjson.JSONObject userInfo = decryptUserInfo(dto.getAsString("encryptedData"), dto.getAsString("key"), dto.getAsString("iv"));
            //判断号码是否存在
            if(null !=userInfo){
                String mobile=userInfo.get("phoneNumber").toString();
                Dto mobDto=new BaseDto();
                mobDto.put("tableName","sysUser");
                mobDto.put("method","updateInfo");
                mobDto.put("id",dto.getAsString("id"));
                mobDto.put("mobile",Des.encrypt(mobile,password));//加密手机号
                bizService.update(mobDto);
                result.setData(mobDto);
                result.setMsg("操作成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }






    //返回个人信息
    @ResponseBody
    @RequestMapping(value = "/userInformation")
    public BaseResult userInformation(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();

        try {

            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            DESWrapper Des = new DESWrapper();
            String password = "9588028820109132570743325311898426347857298773549468758875018579537757772163084478873699447306034466200616411960574122434059469100235892702736860872901247123456";

            //小程序端唯一标识
            String unionid = dto.getAsString("unionid");
            dto.put("tableName", "sysUser");

            if (StringUtils.isNotEmpty(unionid) && unionid !="") {
                Dto udto=(BaseDto)bizService.queryForDto("sysUser.getInfo",new BaseDto("unionid",dto.getAsString("unionid")));
                if(udto !=null){
                    Dto userdto=(BaseDto)bizService.queryForDto("sysUser.getUserInfo",new BaseDto("id",udto.getAsString("id")));
                    userdto.put("mobile",Des.decrypt(userdto.getAsString("mobile"),password));
                    result.setData(userdto);
                }

            }else if(dto.getAsString("id") !="" && dto.getAsString("id") != "Undefined"){
                Dto userdto=(BaseDto)bizService.queryForDto("sysUser.getUserInfo",new BaseDto("id",dto.getAsString("id")));
                if(userdto !=null){
                    userdto.put("mobile",Des.decrypt(userdto.getAsString("mobile"),password));
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
    @ResponseBody
    @RequestMapping(value = "/editWeUser")
    public BaseResult editUser(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();

        try {
//            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
//            if(null ==member){
//                result.setCode("1000");
//                result.setData("登录失效,请重新登录");
//                return result;
//            }
            String id = dto.getAsString("id");
            dto.put("tableName", "sysUser");
            Dto udto=(BaseDto)bizService.queryForDto("sysUser.getInfo",new BaseDto("id",dto.getAsString("id")));
            // 修改
            if (StringUtils.isNotEmpty(id) && udto !=null) {
                if(udto.getAsInteger("editSum")==1 && dto.getAsString("sex")!=""){
                    throw new Exception("性别只可修改一次");
                }
                if (udto.getAsString("sex")!=null && udto.getAsString("sex")!=""){
                    dto.put("editSum",1);
                }
                dto.put("updator",dto.getAsString("id"));
                bizService.updateInfo(dto);
                result.setMsg("操作成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 退出登录
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/loginOut")
    public BaseResult outLogin(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            //防止重复提交操作(根据redis的key30秒来防止重复提交)
            String repeatToken = redisService.getValue("repeatToken_outLogin_" + dto.getAsString("token"));
            if (repeatToken != "") {
                return null;
            }
            redisService.delete(dto.getAsString("token"));
            redisService.delete("repeatToken_outLogin_" + dto.getAsString("token"));
        } catch (Exception e) {
            redisService.delete("repeatToken_outLogin_" + dto.getAsString("token"));
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 解密获取微信用户信息
     *
     * @param encryptedData
     * @param sessionkey
     * @param iv
     * @return
     */
    public com.alibaba.fastjson.JSONObject decryptUserInfo(String encryptedData, String sessionkey, String iv) {
        encryptedData = encryptedData.replaceAll(" ", "+");
        // 被加密的数据
        byte[] dataByte = org.bouncycastle.util.encoders.Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = org.bouncycastle.util.encoders.Base64.decode(sessionkey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }

            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, StandardCharsets.UTF_8);
                return JSON.parseObject(result);
            }
        } catch (Exception e) {
            throw new RuntimeException("获取unionId异常", e);
        }
        throw new RuntimeException("获取联合登陆用户信息失败");

    }




}
