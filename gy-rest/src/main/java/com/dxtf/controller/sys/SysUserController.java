package com.dxtf.controller.sys;

import com.alibaba.fastjson.JSONArray;
import com.dxtf.util.*;
import com.dxtf.service.BizService;
import com.dxtf.base.BizAction;
import net.sf.json.JSONObject;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.resource.util.StringUtils;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * 后台用户业务处理
 *
 * @author mcl
 * @see SysUserController
 * @since 2017年6月20日15:49:06
 */
@RestController
@RequestMapping("/suser")
public class SysUserController extends BizAction {


    /**
     * 登录
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login")
    public BaseResult login(HttpServletRequest request,
                            HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            if (!StringUtils.isNotEmpty(dto.getAsString("nocode"))) {
                if (!redisService.getValue(dto.getAsString("vtoken") + "_IMAGE_CODE").equalsIgnoreCase(dto.getAsString("verifyCode"))) {
                    throw new Exception("验证码错误。");
                }
            }
            Dto member = (BaseDto) bizService.queryForObject(
                    "sysUser.loginByAccount", dto);
            if (member != null && member.getAsLong("id") != null) {
                if (member.getAsString("status").equals("0")) {
                    throw new Exception("用户已离职，帐号失效");
                }
                String dept_type = member.getAsString("dept_type");
                String dept_is_delete = member.getAsString("dept_is_delete");
                Dto customer = null;
                if (StringUtils.isNotEmpty(dept_type)) {
                    if ("Y".equalsIgnoreCase(dept_is_delete)) {
                        throw new Exception("用户权限已失效，请重新配置");
                    }

                    if (!dept_type.equals("20")) {
                        customer = (Dto) bizService.queryForDto("customerRelation.getCustomerByDeptId", new BaseDto("deptid", member.getAsString("deptid")));
                        if (customer != null) {
                            member.put("customer", customer);
                        } else {
                            throw new Exception("用户公司无效，登录失败");
                        }
                    }


                } else {
                    throw new Exception("用户未分配有效权限");
                }


                String token = UUID.randomUUID().toString();
                String loginChannel = dto.getAsString("loginChannel");
                Dto sysConfig = CommonUtil.getSysConfig();
                redisService.setValue(token, JSONArray.toJSONString(member),
                        sysConfig.getAsLong(loginChannel));

                Dto chatMap = new BaseDto();
                chatMap.put("userId", member.get("id"));
                chatMap.put("username", member.get("username"));
                chatMap.put("portraitUri", member.get("pic"));
                chatMap.put("chat_token", member.get("token"));
                chatMap.put("rytoken", member.get("rytoken"));
                member.put("token", token);
                member.put("chatUser", chatMap);
                if (customer != null) {
                    member.put("customer", customer);
                }
                member.put("isAuth", true);
                member.put("result", "图形验证码正确");

                result.setData(member);
            } else {
                throw new Exception("用户名或密码错误。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 微信登录
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/weclogin")
    public BaseResult weclogin(HttpServletRequest request,
                               HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {

            if (!org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getAsString("account"))) {
                throw new Exception("账号不能为空!");
            }

            if (!org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getAsString("password"))) {
                throw new Exception("密码不能为空!");
            }

            Dto member = (BaseDto) bizService.queryForObject(
                    "sysUser.loginByAccount", dto);
            if (member != null && member.getAsLong("id") != null) {
                if (member.getAsString("status").equals("0")) {
                    throw new Exception("用户已离职，帐号失效");
                }


                String openid = dto.getAsString("openid");
                if (StringUtils.isNotEmpty(openid) && !openid.equals(member.getAsString("wopenid"))) {
                    //更新用户信息
                    member.put("tableName", "sysUser");
                    member.put("wopenid", openid);
                    bizService.updateInfo(member);
                }


                String dept_type = member.getAsString("dept_type");
                String dept_is_delete = member.getAsString("dept_is_delete");
                if (StringUtils.isNotEmpty(dept_type)) {
                    if ("Y".equalsIgnoreCase(dept_is_delete)) {
                        throw new Exception("用户权限已失效，请重新配置");
                    }

                    if (!dept_type.equals("20")) {
                        Dto customer = (Dto) bizService.queryForDto("customerRelation.getCustomerByDeptId", new BaseDto("deptid", member.getAsString("deptid")));
                        if (customer != null) {
                            member.put("customer", customer);
                        } else {
                            throw new Exception("用户公司无效，登录失败");
                        }
                    }


                } else {
                    throw new Exception("用户未分配有效权限");
                }

                String token = UUID.randomUUID().toString();
                String loginChannel = dto.getAsString("loginChannel");
                Dto sysConfig = CommonUtil.getSysConfig();
                redisService.setValue(token, JSONArray.toJSONString(member),
                        sysConfig.getAsLong(loginChannel));

                Dto chatMap = new BaseDto();
                chatMap.put("userId", member.get("id"));
                chatMap.put("username", member.get("username"));
                chatMap.put("portraitUri", member.get("pic"));
                chatMap.put("chat_token", member.get("token"));
                chatMap.put("rytoken", member.get("rytoken"));
                member.put("token", token);
                member.put("chatUser", chatMap);

                result.setData(member);
            } else {
                throw new Exception("用户名或密码错误。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 微信退出
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delOpenid")
    public BaseResult delOpenid(HttpServletRequest request,
                                HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {


            String openid = dto.getAsString("openid");
            String username = dto.getAsString("username");
            if (StringUtils.isNotEmpty(openid) && StringUtils.isNotEmpty(username)) {
                Dto member = new BaseDto();
                //更新用户信息
                member.put("tableName", "sysUser");
                member.put("method", "updateWxOpenid");
                member.put("wopenid", openid);
                member.put("username", username);
                bizService.update(member);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 修改密码
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/editUserPwd")
    public BaseResult editUserPwd(HttpServletRequest request,
                                  HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            String password = dto.getAsString("password");
            String newpwd = dto.getAsString("newpwd");
            String id = dto.getAsString("id");
            if (org.apache.commons.lang3.StringUtils.isEmpty(password)) {
                throw new Exception("密码不能为空。");
            }
            if (org.apache.commons.lang3.StringUtils.isEmpty(newpwd)) {
                throw new Exception("新密码不能为空。");
            }
            if (org.apache.commons.lang3.StringUtils.isEmpty(password)) {
                throw new Exception("用户名或密码错误。");
            }
            BaseDto search = new BaseDto("id", id);
            search.put("password", password);

            Dto member = (BaseDto) bizService.queryForObject("sysUser.getInfo", search);
            if (member == null) {
                throw new Exception("用户名密码错误。");
            }
            member.put("tableName", "sysUser");
            member.put("password", newpwd);

            bizService.updateInfo(member);

            //更新用户信息
            member.put("tableName", "sysUser");
            member.put("method", "updateWxOpenid");
            bizService.update(member);

        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 用户编辑
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/editUserInfo")
    public BaseResult editUserInfo(HttpServletRequest request,
                                   HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto repeatUser = (BaseDto) bizService.queryForDto("sysUser.getInfo", new BaseDto("account", inDto.getAsString("account")));//登录名
            if (inDto.getAsLong("id") != null) {// 修改
                if (repeatUser != null && !repeatUser.getAsString("id").equals(inDto.getAsString("id"))) {
                    throw new Exception("您输入的登录账号已经存在，请重新输入。");
                }
                Dto repeatWN = (BaseDto) bizService.queryForDto("sysUser.getInfo", new BaseDto("number1", inDto.getAsString("number")));//工号
                if (repeatWN != null && !repeatWN.getAsString("id").equals(inDto.getAsString("id"))) {
                    throw new Exception("您输入的工号已经存在，请重新输入。");
                }
            } else {// 新增
                if (repeatUser != null) {
                    throw new Exception("您输入的登录账号已经存在，请重新输入。");
                }
                Dto repeatWN = (BaseDto) bizService.queryForDto("sysUser.getInfo", new BaseDto("number", inDto.getAsString("number")));//工号
                if (repeatWN != null) {
                    throw new Exception("您输入的工号已经存在，请重新输入。");
                }
            }

            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
//			inDto.put("creator", member == null ? "" : member.get("id"));
            if (inDto.getAsLong("id") != null) {// 修改
                // 获取上级id
                if (StringUtils.isNotEmpty(inDto.getAsString("numbername"))) {
                    String number = inDto.getAsString("numbername").split("-")[0];
                    Dto pmember = (BaseDto) bizService.queryForObject("sysUser.getInfo", new BaseDto("number", number));
                    if (pmember == null) {
                        throw new Exception("您输入的汇报上级工号不存在，请重新输入。");
                    }
                    if (pmember.getAsString("id").equals(inDto.getAsString("id"))) {
                        throw new Exception("汇报上级不能是自己。");
                    }
                    if (pmember.getAsString("pid").equals(inDto.getAsString("id"))) {
                        throw new Exception("汇报上级不能是自己下级。");
                    }
                    Dto proUser = (BaseDto) bizService.queryForDto("sysUser.getInfo", new BaseDto("id", inDto.getAsString("id")));
                    if (proUser.getAsString("password").equals(inDto.getAsString("password"))) {// 如果相等 password未修改
                        inDto.put("password", null);
                    }
                    inDto.put("pid", pmember.get("id"));
                }
                bizService.updateInfo(inDto);
            } else {// 新增
                // 获取上级id
                if (StringUtils.isNotEmpty(inDto.getAsString("numbername"))) {
                    String number = inDto.getAsString("numbername").split("-")[0];
                    Dto pmember = (BaseDto) bizService.queryForObject("sysUser.getInfo", new BaseDto("number", number));
                    if (pmember == null) {
                        throw new Exception("您输入的汇报上级工号不存在，请重新输入。");
                    }
                    inDto.put("pid", pmember.get("id"));
                }
//				inDto.put("pic",SYS_USER_URL);
                inDto.put("token", UUID.randomUUID().toString());
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
     * 获取用户菜单权限
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMenuByUser")
    public BaseResult getMenuByUser(HttpServletRequest request,
                                    HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"),
                    BaseDto.class);
            if (member != null) {
                List<Dto> menulist = new ArrayList<>();
                if (menulist == null || menulist.size() == 0) {
                    dto.put("types", "0");
                    // 查询出所有模块列表 因为权限页面主要是按照模块设置的
                    List<Dto> localMenulist = bizService.queryForList("sysMenu.queryList", dto);
                    for (Dto module : localMenulist) {
                        List<Dto> roles = new ArrayList<Dto>();
                        // 查看该模块下是否有子菜单
                        dto.put("types", "1");
                        dto.put("pid", module.get("id"));
                        dto.put("roleid", member.get("roleid"));
                        List<Dto> menus = bizService.queryForList("sysMenu.queryMenuList", dto);
                        if (menus != null && menus.size() > 0) {
                            module.put("menus", menus);
                            menulist.add(module);
                        }
                    }
                    redisService.setValue("INIT_MENU_ROLE_" + member.get("roleid"), JSONArray.toJSONString(menulist));
                }
                result.setData(menulist);
            } else {
                result.setCode("4000");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 获取用户操作权限
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getOperationByUser")
    public BaseResult getOperationByUser(HttpServletRequest request,
                                         HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (member != null) {
                List<Dto> operationlist = (List<Dto>) redisService.getList("INIT_OPERATION_ROLE_" + member.get("roleid"), BaseDto.class);
                if (operationlist == null) {
                    dto.put("types", "0");
                    // 查询出所有模块列表 因为权限页面主要是按照模块设置的
                    operationlist = bizService.queryForList("sysMenu.queryList", dto);
                    for (Dto module : operationlist) {
                        // 查看该模块下是否有按钮
                        dto.put("types", "2");
                        dto.put("roleid", member.get("roleid"));
                        List<Dto> bts = bizService.queryForList("sysMenu.queryList", dto);
                        module.put("operates", bts);
                    }
                }
                result.setData(operationlist);
                redisService.setValue("INIT_OPERATION_ROLE_" + member.get("roleid"), JSONArray.toJSONString(operationlist));
            } else {
                result.setCode("4000");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 获取用户数据权限
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDataByUser")
    public BaseResult getDataByUser(HttpServletRequest request,
                                    HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (member != null) {
                List<Dto> datas = (List<Dto>) redisService.getList("INIT_DATA_ROLE_" + member.get("roleid"), BaseDto.class);
                if (datas == null) {
                    dto.put("types", "0");
                    // 查询出所有模块列表 因为权限页面主要是按照模块设置的
                    datas = bizService.queryForList("sysMenu.queryList", dto);
                    for (Dto module : datas) {
                        // 查看该模块下是否有设置权限归属
                        dto.put("menuid", module.get("id"));
                        dto.put("roleid", member.get("roleid"));
                        List<Dto> brs = bizService.queryForList("sysMenu.queryBranchCodeList", dto);
                        module.put("datas", brs);
                    }
                }
                result.setData(datas);
                redisService.setValue("INIT_DATA_ROLE_" + member.get("roleid"), JSONArray.toJSONString(datas));
            } else {
                result.setCode("4000");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 新增系统用户
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addUsrInfo")
    public BaseResult addUsrInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            dto.put("token", UUID.randomUUID());
            bizService.saveInfo(dto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 获取用户信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getUserInfo")
    public BaseResult getUserInfo(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            if (dto.getAsString("token") != null) {
                Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
                if (member != null) {
                    Dto dbmember = (BaseDto) bizService.queryForObject("sysUser.getInfo", new BaseDto("id", member.get("id")));
                    result.setData(dbmember);
                } else {
                    result.setCode("4000");
                }
            } else {
                result.setCode("4000");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 设置员工离职
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/leaveInfo")
    public BaseResult leaveInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            inDto.put("tableName", "sysUser");
            inDto.put("method", "leaveInfo");
            bizService.update(inDto);
            result.setMsg("数据操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 离职重新分配上级
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/reAllotUsers")
    public BaseResult reAllotUsers(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            if (inDto.getAsString("proinfoId").equals(inDto.getAsString("userid"))) {
                throw new Exception("离职分配不能为自己！");
            }
            // 所选中的客户id集合
            String[] ids = inDto.getAsString("ids").split(";");
            // 要分配的经纪人ID
            String userid = inDto.getAsString("userid");
            Dto param = new BaseDto("tableName", "sysUser");
            param.put("method", "setPid");
            param.put("pid", userid);
            for (String id : ids) {
                if (StringUtils.isNotEmpty(id)) {// 设置成公共客户
                    param.put("id", id);
                    bizService.update(param);
                }
            }
            result.setData("");
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 查询修改用户
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryUserInfo")
    public BaseResult queryInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto info = (BaseDto) bizService.queryForDto(dto.getAsString("t") + ".queryUserList", dto);
            result.setData(info);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 生成图形验证码
     *
     * @param request
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/getVtoken")
    public BaseResult getVtoken(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            result.setData(UUID.randomUUID());
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 生成图形验证码
     *
     * @param request
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/getLoginCode")
    public void createAuthCode(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getCodeImageParamAsDto(request);
        BaseResult result = new BaseResult();
        String vtoken = inDto.getAsString("vtoken");
        try {
            String verifyCode = GraphicHelper.createImge(130, 36, "png", response.getOutputStream());
            // 图片验证码存放到缓存60秒
            redisService.delete(vtoken + "_IMAGE_CODE");
            redisService.setValue(vtoken + "_IMAGE_CODE", verifyCode, StatusConstant.VERIFICATION_TIME);
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
            result = reduceErr("生成验证码失败");
        }

    }

    /**
     * 登录
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/loginByToken")
    public BaseResult loginByToken(HttpServletRequest request,
                                   HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = (BaseDto) bizService.queryForObject(
                    "sysUser.loginByAccount", dto);
            if (member != null && member.getAsLong("id") != null) {
                String token = dto.getAsString("token");
                String loginChannel = dto.getAsString("loginChannel");
                Dto sysConfig = CommonUtil.getSysConfig();
                redisService.setValue(token, JSONArray.toJSONString(member),
                        sysConfig.getAsLong(loginChannel));
                Dto chatMap = new BaseDto();
                chatMap.put("userId", member.get("id"));
                chatMap.put("username", member.get("username"));
                chatMap.put("portraitUri", member.get("pic"));
                chatMap.put("chat_token", member.get("token"));
                chatMap.put("rytoken", member.get("rytoken"));
                member.put("token", token);
                member.put("chatUser", chatMap);
                member.put("isAuth", true);
                member.put("result", "图形验证码正确");
                result.setData(member);
            } else {
                throw new Exception("用户名或密码错误。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 获取openid
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getOpenid")
    public BaseResult getOpenid(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try {
            Map<String, String> param = new HashMap<>();
            //param.put("appid", "wxb94b370bc6206016");
            //param.put("secret", "4a4204ad795c0bd8dcb8ebd77a995465");
            param.put("appid", "wxc6c82fbb7cf16644");
            param.put("secret", "f2b40a6cb42b71deb901e8ef180697a4");
            param.put("js_code", dto.getAsString("code"));
            param.put("grant_type", "authorization_code");
            Map<String, String> head = new HashMap<>();
            head.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            String s = HttpClientUtil.doRequestGet("https://api.weixin.qq.com/sns/jscode2session", param, head);
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(s)) {
                JSONObject jsonObject = JSONObject.fromObject(s);
                String openid = jsonObject.getString("openid");
                Dto paramData = new BaseDto();
                paramData.put("wopenid", openid);
                Dto resultData = (Dto) bizService.queryForObject("sysUser.getInfo", paramData);
                if (null != resultData) {
                    Dto resultParam = new BaseDto();
                    resultParam.put("username", resultData.getAsString("username"));
                    String uuid = UUID.randomUUID().toString();
                    resultParam.put("token", uuid);
                    resultParam.put("id", resultData.getAsString("id"));
                    resultParam.put("wopenid", resultData.getAsString("wopenid"));

                    String dept_type = resultData.getAsString("dept_type");
                    String dept_is_delete = resultData.getAsString("dept_is_delete");
                    if (StringUtils.isNotEmpty(dept_type)) {
                        if ("Y".equalsIgnoreCase(dept_is_delete)) {
                            throw new Exception("用户权限已失效，请重新配置");
                        }

                        if (!dept_type.equals("20")) {
                            Dto customer = (Dto) bizService.queryForDto("customerRelation.getCustomerByDeptId", new BaseDto("deptid", resultData.getAsString("deptid")));
                            if (customer != null) {
                                resultParam.put("customer", customer);
                            } else {
                                throw new Exception("用户公司无效，登录失败");
                            }
                        }


                    } else {
                        throw new Exception("用户未分配有效权限");
                    }


                    redisService.setValue(uuid, JSONArray.toJSONString(resultParam), 7200l);
                    result.setData(resultParam);
                } else {
                    Dto resultParam = new BaseDto();
                    resultParam.put("openid", openid);
                    resultParam.put("flg", "1");
                    result.setData(resultParam);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    public String getOpenids(String code) {
        Map<String, String> param = new HashMap<>();
        param.put("appid", "wxb94b370bc6206016");
        param.put("secret", "4a4204ad795c0bd8dcb8ebd77a995465");
        param.put("js_code", code);
        param.put("grant_type", "authorization_code");
        Map<String, String> head = new HashMap<>();
        head.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        String s = null;
        try {
            s = HttpClientUtil.doRequestGet("https://api.weixin.qq.com/sns/jscode2session", param, head);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String openid = "";
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(s)) {
            JSONObject jsonObject = JSONObject.fromObject(s);
            openid = jsonObject.getString("openid");
        }
        return openid;
    }
}
